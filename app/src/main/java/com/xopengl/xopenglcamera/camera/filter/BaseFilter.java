package com.xopengl.xopenglcamera.camera.filter;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.xopengl.xopenglcamera.camera.util.OpenGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author Lixingxing
 */
public abstract class BaseFilter {
    protected int mProgram;
    protected int vPosition,vCoord,vMatrix,vTexture;
    protected int mWidth, mHeight;
    protected FloatBuffer vPostionBuffer;
    protected float[] POSITION = new float[]{-1f,-1f,
            1f,-1f,
            -1f,1f,
            1f,1f};

    protected FloatBuffer vCoordBuffer;
    protected float[] TEXTURE = new float[]{1f,0f,
            1f,1f,
            0f,0f,
            0f,1f};
    protected float[] mtx = new float[16];

    // 初始化
    public BaseFilter(Context context,int vertRawId,int fragRawId){
        initilize(context,vertRawId,fragRawId);
        initCoord();
    }

    protected void initilize(Context context, int mVershaderId, int mFragShaderId){
        mProgram = OpenGLUtils.createProgram(context,mVershaderId,mFragShaderId);
        // 获得着色器中的变量的索引,通过这个索引给这个变量赋值
        vPosition = GLES20.glGetAttribLocation(mProgram,"vPosition");
        vCoord = GLES20.glGetAttribLocation(mProgram,"vCoord");
        vMatrix = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        vTexture = GLES20.glGetUniformLocation(mProgram,"vTexture");
    }

    protected void initCoord(){
        // 顶点坐标
        vPostionBuffer = ByteBuffer.allocateDirect(4*2*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vPostionBuffer.clear();
        vPostionBuffer.put(POSITION);
        // 纹理坐标
        vCoordBuffer = ByteBuffer.allocateDirect(4*2*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vCoordBuffer.clear();
        vCoordBuffer.put(TEXTURE);
    }

    // 设置窗口大小
    public void onReady(int width,int height){
        this.mWidth = width;
        this.mHeight = height;
    }

    public void setMatrix(float[] mtx){
        this.mtx = mtx;
    }

    // 绘制方法
    public int onDrawFrame(int textureId){
        // 1.设置窗口大小
        GLES20.glViewport(0,0, mWidth,mHeight);
        // 2.使用着色器程序
        GLES20.glUseProgram(mProgram);
        // 3.给着色器程序中传值
        // 3.1 给顶点坐标数据传值
        vPostionBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition,2,GLES20.GL_FLOAT,false,0,vPostionBuffer);
        // 激活
        GLES20.glEnableVertexAttribArray(vPosition);
        // 3.2 给纹理坐标数据传值
        vCoordBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord,2,GLES20.GL_FLOAT,false,0,vCoordBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        // 3.3 变化矩阵传值
        GLES20.glUniformMatrix4fv(vMatrix,1,false,mtx,0);

        // 3.4 给片元着色器中的 采样器绑定
        // 激活图层
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // 图像数据
        GLES20.glBindTexture(GLES11Ext.GL_SAMPLER_EXTERNAL_OES,textureId);
        // 传递参数
        GLES20.glUniform1i(vTexture,0);

        //参数传递完毕,通知 opengl开始画画
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        // 解绑
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);

        return textureId;
    }
}
