package com.knight.kotlin.library_video.play;

import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;

import android.view.Surface;
import android.view.TextureView;

/**
 * Author:Knight
 * Time:2024/5/11 15:36
 * Description:VideoMediaInterface
 */
public abstract class VideoMediaInterface implements TextureView.SurfaceTextureListener {

    public static SurfaceTexture SAVED_SURFACE;
    public HandlerThread mMediaHandlerThread;
    public Handler mMediaHandler;
    public Handler handler;
    public OkPlayer okPlayer;


    public VideoMediaInterface(OkPlayer okPlayer) {
        this.okPlayer = okPlayer;
    }

    public abstract void start();

    public abstract void prepare();

    public abstract void pause();

    public abstract boolean isPlaying();

    public abstract void seekTo(long time);

    public abstract void release();

    public abstract long getCurrentPosition();

    public abstract long getDuration();

    public abstract void setVolume(float leftVolume, float rightVolume);

    public abstract void setSpeed(float speed);

    public abstract void setSurface(Surface surface);
}
