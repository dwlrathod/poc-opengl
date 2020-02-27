package io.github.dwlrathod.opengl_poc_plugin;

import android.annotation.TargetApi;
import android.opengl.GLES20;
import android.os.Build;

class SampleRenderWorker implements OpenGLRenderer.Worker {

    private double _tick = 0;

    @Override
    public void onCreate() {

    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    public boolean onDraw() {
        _tick = _tick + Math.PI / 60;

        float green = (float) ((Math.sin(_tick) + 1) / 2);

        GLES20.glClearColor(0f, green, 0f, 1f);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        return true;
    }

    @Override
    public void onDispose() {

    }
}
