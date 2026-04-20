package com.wallpaper4k.ultrahd.live.backgrounds.utils;

import android.net.Uri;
import android.os.Environment;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import java.io.File;

public class NyanNyanService extends WallpaperService {
    static final String TAG = "NYAN";

    @Override
    public Engine onCreateEngine() {
        return new NyanEngine();
    }

    public File getPath() {
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            filesDir = getExternalFilesDir(null);
        } else {
            filesDir = getFilesDir();
        }
        return filesDir;
    }

    class NyanEngine extends Engine {
        private ExoPlayer player;

        @Override
        public void onDestroy() {
            super.onDestroy();
            releasePlayer();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (player != null) {
                if (visible) {
                    player.play();
                } else {
                    player.pause();
                }
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            initializePlayer(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            if (player != null) {
                player.setVideoSurface(null);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            if (player != null) {
                player.setVideoSurface(holder.getSurface());
            }
        }

        private void initializePlayer(SurfaceHolder holder) {
            if (player == null) {
                player = new ExoPlayer.Builder(getApplicationContext()).build();
                player.setRepeatMode(Player.REPEAT_MODE_ALL);
                player.setVolume(0);
                
                File videoFile = new File(getPath(), "video.mp4");
                if (videoFile.exists()) {
                    MediaItem mediaItem = MediaItem.fromUri(Uri.fromFile(videoFile));
                    player.setMediaItem(mediaItem);
                    player.prepare();
                } else {
                    Log.e(TAG, "Video file not found: " + videoFile.getAbsolutePath());
                }
            }
            player.setVideoSurface(holder.getSurface());
            player.setPlayWhenReady(isVisible());
        }

        private void releasePlayer() {
            if (player != null) {
                player.release();
                player = null;
            }
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep,
                                     float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
        }
    }
}