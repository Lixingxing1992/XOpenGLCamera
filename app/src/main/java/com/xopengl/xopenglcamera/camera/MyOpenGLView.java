package com.xopengl.xopenglcamera.camera;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.xopengl.xopenglcamera.camera.config.FilterType;

/**
 * @author Lixingxing
 */
public class MyOpenGLView extends GLSurfaceView {
    // 自定义的 Renderer
    public MyCameraRenderer cameraRenderer;

    public void setFilterType(final FilterType filterType) {
        if(cameraRenderer!=null) {
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    // 必须要在 glThread中执行
                    cameraRenderer.setFilterType(filterType);
                }
            });
        }
    }

    public MyOpenGLView(Context context) {
        this(context,null);
    }

    public MyOpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        cameraRenderer = new MyCameraRenderer(this);
        setEGLContextClientVersion(2);
        setRenderer(cameraRenderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    // 屏幕锁屏或者按了home键或者页面关闭销毁的时候
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        cameraRenderer.surfaceDestroyed();
    }
}
