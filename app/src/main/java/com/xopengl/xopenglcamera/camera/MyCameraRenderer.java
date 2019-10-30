package com.xopengl.xopenglcamera.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.xopengl.xopenglcamera.camera.config.FilterType;
import com.xopengl.xopenglcamera.camera.filter.BaseFilter;
import com.xopengl.xopenglcamera.camera.filter.FlipFilter;
import com.xopengl.xopenglcamera.camera.filter.ScreenFilter;
import com.xopengl.xopenglcamera.camera.filter.WhiteFilter;
import com.xopengl.xopenglcamera.camera.util.CameraHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author Lixingxing
 */
public class MyCameraRenderer implements GLSurfaceView.Renderer {
    private GLSurfaceView glSurfaceView;
    private CameraHelper cameraHelper;
    private SurfaceTexture surfaceTexture;
    private int[] mTextures;
    // 这里用 BaseFilter
    private BaseFilter baseFilter;
    private FilterType filterType = FilterType.DEFAULT;

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
        // 生成相机数据处理类
        if(filterType == FilterType.DEFAULT){
            baseFilter = new ScreenFilter(glSurfaceView.getContext());
        }else if(filterType == FilterType.FLIP){
            baseFilter = new FlipFilter(glSurfaceView.getContext());
        }else if(filterType == FilterType.WHITE){
            baseFilter = new WhiteFilter(glSurfaceView.getContext());
        }
        baseFilter.onReady(cameraHelper.WIDTH,cameraHelper.HEIGHT);
        glSurfaceView.requestRender();
    }

    private float[] mtx = new float[16];

    public MyCameraRenderer(GLSurfaceView glSurfaceView){
        this.glSurfaceView = glSurfaceView;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 打开前置摄像头
        cameraHelper = new CameraHelper(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mTextures = new int[1];
        GLES20.glGenTextures(mTextures.length,mTextures,0);
        surfaceTexture = new SurfaceTexture(mTextures[0]);
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                glSurfaceView.requestRender();
            }
        });
       if(baseFilter == null){
           baseFilter = new ScreenFilter(glSurfaceView.getContext());
       }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 开启预览
        cameraHelper.WIDTH = width;
        cameraHelper.HEIGHT = height;
        cameraHelper.startPreview(surfaceTexture);
        if(baseFilter!=null){
            baseFilter.onReady(width,height);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 1. 清理屏幕 设置屏幕颜色为 glClearColor中设置的颜色
        GLES20.glClearColor(0,0,0,0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // 2. 把摄像头数据从 SurfaceTexture 中取出来
        //   2.1 更新纹理,然后才能使用openGl从SurfaceTexture中获取数据
        surfaceTexture.updateTexImage();
        //   2.2 取得变换矩阵
        //     SurfaceTexture 在opengl中使用的是特殊的采样器“samplerExternalOES”,必须要通过变换矩阵才能获得 Simple2D的采样器
        //     mtx 代表一个4*4的矩阵数据，所以要用  float[] mtx = new float[16]来声明
        surfaceTexture.getTransformMatrix(mtx);

        // 3.把数据绘制到屏幕上显示
        if(baseFilter!=null){
            baseFilter.setMatrix(mtx);
            baseFilter.onDrawFrame(mTextures[0]);
        }
    }

    public void surfaceDestroyed(){
        cameraHelper.stopPreview();
    }
}
