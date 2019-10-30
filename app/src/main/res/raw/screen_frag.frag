//片元着色器  因为SurfaceTexture比较特殊,这里需要多加一句说明
#extension GL_OES_EGL_image_external : require

// 精度
precision mediump float;
// 采样点的坐标 来自顶点着色器
varying vec2 aCoord;

// 采样器  -- 需要从代码中传入
uniform samplerExternalOES vTexture;

void main() {
    // 内置变量 接受像素值
    //    gl_FragColor = vec4(1,1,1,1);
    // texture2D是内置函数,通过采样器采集到aCoord的颜色
    gl_FragColor = texture2D(vTexture,aCoord);
}
