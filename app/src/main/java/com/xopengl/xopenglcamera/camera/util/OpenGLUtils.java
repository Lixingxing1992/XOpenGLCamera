package com.xopengl.xopenglcamera.camera.util;

import android.content.Context;
import android.opengl.GLES20;

import com.xopengl.xopenglcamera.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Lixingxing
 */
public class OpenGLUtils {

    public static String readRawFileContent(Context context,int rawId){
        InputStream inputStream = context.getResources().openRawResource(rawId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    public static int createProgram(Context context, int vertRawId,int vertFragId){
        //1.生成顶点着色器并编译顶点着色器代码
        String vertexSource = OpenGLUtils.readRawFileContent(context,vertRawId);
        // 1.1 生成顶点着色器id
        int vShaderVextId = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        // 1.2 绑定代码到着色器中
        GLES20.glShaderSource(vShaderVextId, vertexSource);
        // 1.3 编译着色器代码
        GLES20.glCompileShader(vShaderVextId);
        // 1.4 主动获取成功 失败状态
        int[] status = new int[1];
        GLES20.glGetShaderiv(vShaderVextId, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            // 如果没有成功,抛出异常 如果不做处理,log会输出一条GLERROR的日志
            throw new IllegalStateException(" 顶点着色器配置失败");
        }

        // 2.创建片元着色器并编译顶点着色器代码
        // 2.0 获取片元着色器代码
        String fragSource = OpenGLUtils.readRawFileContent(context, vertFragId);
        // 2.1 生成片元着色器id
        int vShaderFragId = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        // 2.2 绑定代码到着色器中
        GLES20.glShaderSource(vShaderFragId, fragSource);
        // 2.3 编译着色器代码
        GLES20.glCompileShader(vShaderFragId);
        // 2.4 主动获取成功 失败状态
        GLES20.glGetShaderiv(vShaderFragId, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            // 如果没有成功,抛出异常 如果不做处理,log会输出一条GLERROR的日志
            throw new IllegalStateException(" 片元着色器配置失败");
        }

        // 3.创建着色器程序并链接着色器
        int mProgram = GLES20.glCreateProgram();
        // 把着色器塞入 程序当中
        GLES20.glAttachShader(mProgram, vShaderVextId);
        GLES20.glAttachShader(mProgram, vShaderFragId);
        // 链接着色器
        GLES20.glLinkProgram(mProgram);
        // 主动获取成功 失败状态
        GLES20.glGetProgramiv(mProgram, GLES20.GL_LINK_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            // 如果没有成功,抛出异常 如果不做处理,log会输出一条GLERROR的日志
            throw new IllegalStateException(" 创建着色器程序失败");
        }

        // 4. 释放资源
        GLES20.glDeleteShader(vShaderVextId);
        GLES20.glDeleteShader(vShaderFragId);

        return mProgram;
    }
}
