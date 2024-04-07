package com.knight.kotlin.module_video.utils.videocache;

import static com.knight.kotlin.module_video.utils.videocache.Preconditions.checkAllNotNull;
import static com.knight.kotlin.module_video.utils.videocache.Preconditions.checkNotNull;

import android.content.Context;
import android.net.Uri;


import com.knight.kotlin.module_video.utils.videocache.file.DiskUsage;
import com.knight.kotlin.module_video.utils.videocache.file.FileNameGenerator;
import com.knight.kotlin.module_video.utils.videocache.file.Md5FileNameGenerator;
import com.knight.kotlin.module_video.utils.videocache.file.TotalCountLruDiskUsage;
import com.knight.kotlin.module_video.utils.videocache.file.TotalSizeLruDiskUsage;
import com.knight.kotlin.module_video.utils.videocache.headers.EmptyHeadersInjector;
import com.knight.kotlin.module_video.utils.videocache.headers.HeaderInjector;
import com.knight.kotlin.module_video.utils.videocache.sourcestorage.SourceInfoStorage;
import com.knight.kotlin.module_video.utils.videocache.sourcestorage.SourceInfoStorageFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




/**
 * Simple lightweight proxy server with file caching support that handles HTTP requests.
 * Typical usage:
 * <pre><code>
 * public onCreate(Bundle state) {
 *      super.onCreate(state);
 *
 *      HttpProxyCacheServer proxy = getProxy();
 *      String proxyUrl = proxy.getProxyUrl(VIDEO_URL);
 *      videoView.setVideoPath(proxyUrl);
 * }
 *
 * private HttpProxyCacheServer getProxy() {
 * // should return single instance of HttpProxyCacheServer shared for whole app.
 * }
 * </code></pre>
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class HttpProxyCacheServer {

    private static final Logger LOG = LoggerFactory.getLogger("HttpProxyCacheServer");
    private static final String PROXY_HOST = "127.0.0.1";

    private final Object clientsLock = new Object();
    /**固定线程池，最多同时可支持8个核心线程，主要用户执行客户端任务*/
    private final ExecutorService socketProcessor = Executors.newFixedThreadPool(8);
    /**存储代理客户端*/
    private final Map<String, HttpProxyCacheServerClients> clientsMap = new ConcurrentHashMap<>();
    /**服务端Socket*/
    private final ServerSocket serverSocket;
    /**服务端端口号*/
    private final int port;
    /**等待客户端连接服务端线程，此线程用于等待每一个客户端服务的链接*/
    private final Thread waitConnectionThread;
    /**配置*/
    private final Config config;
    /**看ip地址是否能够ping通*/
    private final Pinger pinger;

    public HttpProxyCacheServer(Context context) {
        this(new Builder(context).buildConfig());
    }

    private HttpProxyCacheServer(Config config) {
        this.config = checkNotNull(config);
        try {
            InetAddress inetAddress = InetAddress.getByName(PROXY_HOST);
            /**ServerSocket(port,backlog,inetAddress)参数说明：
             * port：第一个参数为端口号，如果填写0，则系统自动会分配一个端口号
             * backlog：第二个参数：最多同时可支持多少个链接
             * inetAddress：主机地址
             * */
            this.serverSocket = new ServerSocket(0, 8, inetAddress);
            //获取随机分配的端口号
            this.port = serverSocket.getLocalPort();
            IgnoreHostProxySelector.install(PROXY_HOST, port);
            /**信号量：其目的是为了让waitConnectionThread的run方法先执行*/
            CountDownLatch startSignal = new CountDownLatch(1);
            /**创建并开启一个一直等待客户端连接的线程*/
            this.waitConnectionThread = new Thread(new WaitRequestsRunnable(startSignal));
            this.waitConnectionThread.start();
            startSignal.await(); // freeze thread, wait for server starts
            //创建一个ping实例
            this.pinger = new Pinger(PROXY_HOST, port);
            LOG.info("Proxy cache server started. Is it alive? " + isAlive());
        } catch (IOException | InterruptedException e) {
            socketProcessor.shutdown();
            throw new IllegalStateException("Error starting local proxy server", e);
        }
    }

    /**
     * Returns url that wrap original url and should be used for client (MediaPlayer, ExoPlayer, etc).
     * <p>
     * If file for this url is fully cached (it means method {@link #isCached(String)} returns {@code true})
     * then file:// uri to cached file will be returned.
     * <p>
     * Calling this method has same effect as calling {@link #getProxyUrl(String, boolean)} with 2nd parameter set to {@code true}.
     *
     * @param url a url to file that should be cached.
     * @return a wrapped by proxy url if file is not fully cached or url pointed to cache file otherwise.
     */
    public String getProxyUrl(String url) {
        return getProxyUrl(url, true);
    }

    /**
     * Returns url that wrap original url and should be used for client (MediaPlayer, ExoPlayer, etc).
     * <p>
     * If parameter {@code allowCachedFileUri} is {@code true} and file for this url is fully cached
     * (it means method {@link #isCached(String)} returns {@code true}) then file:// uri to cached file will be returned.
     *
     * @param url                a url to file that should be cached.
     * @param allowCachedFileUri {@code true} if allow to return file:// uri if url is fully cached
     * @return a wrapped by proxy url if file is not fully cached or url pointed to cache file otherwise (if {@code allowCachedFileUri} is {@code true}).
     */
    public String getProxyUrl(String url, boolean allowCachedFileUri) {
        //如果缓存中已经有这个url，那就取出此url
        if (allowCachedFileUri && isCached(url)) {
            File cacheFile = getCacheFile(url);
            touchFileSafely(cacheFile);
            return Uri.fromFile(cacheFile).toString();
        }
        //看能否ping的通server服务，可以就用proxy url，否则用原始url
        return isAlive() ? appendToProxyUrl(url) : url;
    }

    public void registerCacheListener(CacheListener cacheListener, String url) {
        checkAllNotNull(cacheListener, url);
        synchronized (clientsLock) {
            try {
                getClients(url).registerCacheListener(cacheListener);
            } catch (ProxyCacheException e) {
                LOG.warn("Error registering cache listener", e);
            }
        }
    }

    public void unregisterCacheListener(CacheListener cacheListener, String url) {
        checkAllNotNull(cacheListener, url);
        synchronized (clientsLock) {
            try {
                getClients(url).unregisterCacheListener(cacheListener);
            } catch (ProxyCacheException e) {
                LOG.warn("Error registering cache listener", e);
            }
        }
    }

    public void unregisterCacheListener(CacheListener cacheListener) {
        checkNotNull(cacheListener);
        synchronized (clientsLock) {
            for (HttpProxyCacheServerClients clients : clientsMap.values()) {
                clients.unregisterCacheListener(cacheListener);
            }
        }
    }

    /**
     * Checks is cache contains fully cached file for particular url.
     *
     * @param url an url cache file will be checked for.
     * @return {@code true} if cache contains fully cached file for passed in parameters url.
     */
    public boolean isCached(String url) {
        checkNotNull(url, "Url can't be null!");
        return getCacheFile(url).exists();
    }

    public void shutdown() {
        LOG.info("Shutdown proxy server");

        shutdownClients();

        config.sourceInfoStorage.release();

        waitConnectionThread.interrupt();
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            onError(new ProxyCacheException("Error shutting down proxy server", e));
        }
    }

    private boolean isAlive() {
        return pinger.ping(3, 70);   // 70+140+280=max~500ms
    }

    private String appendToProxyUrl(String url) {
        return String.format(Locale.US, "http://%s:%d/%s", PROXY_HOST, port, ProxyCacheUtils.encode(url));
    }

    public File getCacheFile(String url) {
        File cacheDir = config.cacheRoot;
        String fileName = config.fileNameGenerator.generate(url);
        return new File(cacheDir, fileName);
    }

    private void touchFileSafely(File cacheFile) {
        try {
            config.diskUsage.touch(cacheFile);
        } catch (IOException e) {
            LOG.error("Error touching file " + cacheFile, e);
        }
    }

    private void shutdownClients() {
        synchronized (clientsLock) {
            for (HttpProxyCacheServerClients clients : clientsMap.values()) {
                clients.shutdown();
            }
            clientsMap.clear();
        }
    }

    /**
     * 死循环通过serverSocket.accept()一直阻塞等待客户端连接
     */
    private void waitForRequest() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                //一直等待有客户端连接进来
                Socket socket = serverSocket.accept();
                LOG.info("Accept new socket " + socket);
                //连接进来之后立马提交给线程池执行
                socketProcessor.submit(new SocketProcessorRunnable(socket));
            }
        } catch (IOException e) {
            onError(new ProxyCacheException("Error during waiting connection", e));
        }
    }

    /**
     * 客户端连接上服务端后的处理逻辑
     * @param socket
     */
    private void processSocket(Socket socket) {
        try {
            GetRequest request = GetRequest.read(socket.getInputStream());
            LOG.debug("Request to cache proxy:" + request);
            String url = ProxyCacheUtils.decode(request.uri);
            LOG.info("此时的url为："+url);
            if (pinger.isPingRequest(url)) {
                pinger.responseToPing(socket);
            } else {
                HttpProxyCacheServerClients clients = getClients(url);
                clients.processRequest(request, socket);
            }
        } catch (SocketException e) {
            // There is no way to determine that client closed connection http://stackoverflow.com/a/10241044/999458
            // So just to prevent log flooding don't log stacktrace
            LOG.debug("Closing socket… Socket is closed by client.");
        } catch (ProxyCacheException | IOException e) {
            onError(new ProxyCacheException("Error processing request", e));
        } finally {
            releaseSocket(socket);
            LOG.debug("Opened connections: " + getClientsCount());
        }
    }

    private HttpProxyCacheServerClients getClients(String url) throws ProxyCacheException {
        synchronized (clientsLock) {
            HttpProxyCacheServerClients clients = clientsMap.get(url);
            if (clients == null) {
                clients = new HttpProxyCacheServerClients(url, config);
                clientsMap.put(url, clients);
            }
            return clients;
        }
    }

    private int getClientsCount() {
        synchronized (clientsLock) {
            int count = 0;
            for (HttpProxyCacheServerClients clients : clientsMap.values()) {
                count += clients.getClientsCount();
            }
            return count;
        }
    }

    private void releaseSocket(Socket socket) {
        closeSocketInput(socket);
        closeSocketOutput(socket);
        closeSocket(socket);
    }

    private void closeSocketInput(Socket socket) {
        try {
            if (!socket.isInputShutdown()) {
                socket.shutdownInput();
            }
        } catch (SocketException e) {
            // There is no way to determine that client closed connection http://stackoverflow.com/a/10241044/999458
            // So just to prevent log flooding don't log stacktrace
            LOG.debug("Releasing input stream… Socket is closed by client.");
        } catch (IOException e) {
            onError(new ProxyCacheException("Error closing socket input stream", e));
        }
    }

    private void closeSocketOutput(Socket socket) {
        try {
            if (!socket.isOutputShutdown()) {
                socket.shutdownOutput();
            }
        } catch (IOException e) {
            LOG.warn("Failed to close socket on proxy side: {}. It seems client have already closed connection.", e.getMessage());
        }
    }

    private void closeSocket(Socket socket) {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            onError(new ProxyCacheException("Error closing socket", e));
        }
    }

    private void onError(Throwable e) {
        LOG.error("HttpProxyCacheServer error", e);
    }

    /**
     * 一直等待客户端连接的线程
     */
    private final class WaitRequestsRunnable implements Runnable {

        private final CountDownLatch startSignal;

        public WaitRequestsRunnable(CountDownLatch startSignal) {
            this.startSignal = startSignal;
        }

        @Override
        public void run() {
            startSignal.countDown();
            waitForRequest();
        }
    }

    private final class SocketProcessorRunnable implements Runnable {

        private final Socket socket;

        public SocketProcessorRunnable(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            processSocket(socket);
        }
    }

    /**
     * Builder for {@link HttpProxyCacheServer}.
     */
    public static final class Builder {

        private static final long DEFAULT_MAX_SIZE = 512 * 1024 * 1024;

        private File cacheRoot;
        private FileNameGenerator fileNameGenerator;
        private DiskUsage diskUsage;
        private SourceInfoStorage sourceInfoStorage;
        private HeaderInjector headerInjector;

        public Builder(Context context) {
            this.sourceInfoStorage = SourceInfoStorageFactory.newSourceInfoStorage(context);
            this.cacheRoot = StorageUtils.getIndividualCacheDirectory(context);
            this.diskUsage = new TotalSizeLruDiskUsage(DEFAULT_MAX_SIZE);
            this.fileNameGenerator = new Md5FileNameGenerator();
            this.headerInjector = new EmptyHeadersInjector();
        }

        /**
         * Overrides default cache folder to be used for caching files.
         * <p>
         * By default AndroidVideoCache uses
         * '/Android/data/[app_package_name]/cache/video-cache/' if card is mounted and app has appropriate permission
         * or 'video-cache' subdirectory in default application's cache directory otherwise.
         * </p>
         * <b>Note</b> directory must be used <b>only</b> for AndroidVideoCache files.
         *
         * @param file a cache directory, can't be null.
         * @return a builder.
         */
        public Builder cacheDirectory(File file) {
            this.cacheRoot = checkNotNull(file);
            return this;
        }

        /**
         * Overrides default cache file name generator {@link Md5FileNameGenerator} .
         *
         * @param fileNameGenerator a new file name generator.
         * @return a builder.
         */
        public Builder fileNameGenerator(FileNameGenerator fileNameGenerator) {
            this.fileNameGenerator = checkNotNull(fileNameGenerator);
            return this;
        }

        /**
         * Sets max cache size in bytes.
         * <p>
         * All files that exceeds limit will be deleted using LRU strategy.
         * Default value is 512 Mb.
         * </p>
         * Note this method overrides result of calling {@link #maxCacheFilesCount(int)}
         *
         * @param maxSize max cache size in bytes.
         * @return a builder.
         */
        public Builder maxCacheSize(long maxSize) {
            this.diskUsage = new TotalSizeLruDiskUsage(maxSize);
            return this;
        }

        /**
         * Sets max cache files count.
         * All files that exceeds limit will be deleted using LRU strategy.
         * Note this method overrides result of calling {@link #maxCacheSize(long)}
         *
         * @param count max cache files count.
         * @return a builder.
         */
        public Builder maxCacheFilesCount(int count) {
            this.diskUsage = new TotalCountLruDiskUsage(count);
            return this;
        }

        /**
         * Set custom DiskUsage logic for handling when to keep or clean cache.
         *
         * @param diskUsage a disk usage strategy, cant be {@code null}.
         * @return a builder.
         */
        public Builder diskUsage(DiskUsage diskUsage) {
            this.diskUsage = checkNotNull(diskUsage);
            return this;
        }

        /**
         * Add headers along the request to the server
         *
         * @param headerInjector to inject header base on url
         * @return a builder
         */
        public Builder headerInjector(HeaderInjector headerInjector) {
            this.headerInjector = checkNotNull(headerInjector);
            return this;
        }

        /**
         * Builds new instance of {@link HttpProxyCacheServer}.
         *
         * @return proxy cache. Only single instance should be used across whole app.
         */
        public HttpProxyCacheServer build() {
            Config config = buildConfig();
            return new HttpProxyCacheServer(config);
        }

        private Config buildConfig() {
            return new Config(cacheRoot, fileNameGenerator, diskUsage, sourceInfoStorage, headerInjector);
        }

    }
    public File getTempCacheFile(String url) {
        File cacheDir = config.cacheRoot;
        String fileName = config.fileNameGenerator.generate(url) + ".download";
        return new File(cacheDir, fileName);
    }

    public File getCacheRoot() {
        return config.cacheRoot;
    }
}
