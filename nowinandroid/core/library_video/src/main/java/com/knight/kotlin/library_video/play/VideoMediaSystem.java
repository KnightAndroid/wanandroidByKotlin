package com.knight.kotlin.library_video.play;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Author:Knight
 * Time:2024/5/11 15:37
 * Description:VideoMediaSystem
 */
public class VideoMediaSystem extends VideoMediaInterface implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {

    public MediaPlayer mediaPlayer;

    public VideoMediaSystem(OkPlayer okPlayer) {
        super(okPlayer);
    }

    @Override
    public void prepare() {
        release();
        mMediaHandlerThread = new HandlerThread("JZVD");
        mMediaHandlerThread.start();
        mMediaHandler = new Handler(mMediaHandlerThread.getLooper());//主线程还是非主线程，就在这里
        handler = new Handler();

        mMediaHandler.post(() -> {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(okPlayer.jzDataSource.looping);
                mediaPlayer.setOnPreparedListener(VideoMediaSystem.this);
                mediaPlayer.setOnCompletionListener(VideoMediaSystem.this);
                mediaPlayer.setOnBufferingUpdateListener(VideoMediaSystem.this);
                mediaPlayer.setScreenOnWhilePlaying(true);
                mediaPlayer.setOnSeekCompleteListener(VideoMediaSystem.this);
                mediaPlayer.setOnErrorListener(VideoMediaSystem.this);
                mediaPlayer.setOnInfoListener(VideoMediaSystem.this);
                mediaPlayer.setOnVideoSizeChangedListener(VideoMediaSystem.this);
                Class<MediaPlayer> clazz = MediaPlayer.class;
                //如果不用反射，没有url和header参数的setDataSource函数
                Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
                method.invoke(mediaPlayer, okPlayer.jzDataSource.getCurrentUrl().toString(), okPlayer.jzDataSource.headerMap);
                mediaPlayer.prepareAsync();
                mediaPlayer.setSurface(new Surface(SAVED_SURFACE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void start() {
        mMediaHandler.post(() -> mediaPlayer.start());
    }

    @Override
    public void pause() {
        mMediaHandler.post(() -> mediaPlayer.pause());
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        mMediaHandler.post(() -> {
            try {
                mediaPlayer.seekTo((int) time);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void release() {//not perfect change you later
        if (mMediaHandler != null && mMediaHandlerThread != null && mediaPlayer != null) {//不知道有没有妖孽
            HandlerThread tmpHandlerThread = mMediaHandlerThread;
            MediaPlayer tmpMediaPlayer = mediaPlayer;
            VideoMediaInterface.SAVED_SURFACE = null;

            mMediaHandler.post(() -> {
                tmpMediaPlayer.setSurface(null);
                tmpMediaPlayer.release();
                tmpHandlerThread.quit();
            });
            mediaPlayer = null;
        }
    }

    //TODO 测试这种问题是否在threadHandler中是否正常，所有的操作mediaplayer是否不需要thread，挨个测试，是否有问题
    @Override
    public long getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    @Override
    public long getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        if (mMediaHandler == null) return;
        mMediaHandler.post(() -> {
            if (mediaPlayer != null) mediaPlayer.setVolume(leftVolume, rightVolume);
        });
    }

    @Override
    public void setSpeed(float speed) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            PlaybackParams pp = mediaPlayer.getPlaybackParams();
            pp.setSpeed(speed);
            mediaPlayer.setPlaybackParams(pp);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        handler.post(() -> okPlayer.onPrepared());//如果是mp3音频，走这里
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        handler.post(() -> okPlayer.onCompletion());
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, final int percent) {
        handler.post(() -> okPlayer.setBufferProgress(percent));
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        handler.post(() -> okPlayer.onSeekComplete());
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, final int what, final int extra) {
        handler.post(() -> okPlayer.onError(what, extra));
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, final int what, final int extra) {
        handler.post(() -> okPlayer.onInfo(what, extra));
        return false;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
        handler.post(() -> okPlayer.onVideoSizeChanged(width, height));
    }

    @Override
    public void setSurface(Surface surface) {
        mediaPlayer.setSurface(surface);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (SAVED_SURFACE == null) {
            SAVED_SURFACE = surface;
            prepare();
        } else {
            okPlayer.textureView.setSurfaceTexture(SAVED_SURFACE);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
