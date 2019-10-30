//片元着色器  因为SurfaceTexture比较特殊,这里需要多加一句说明
#extension GL_OES_EGL_image_external : require

// 精度
precision mediump float;
// 采样点的坐标 来自顶点着色器
varying vec2 aCoord;

// 采样器  -- 需要从代码中传入
uniform samplerExternalOES vTexture;

//void main() {
//    // 内置变量 接受像素值
//    //    gl_FragColor = vec4(1,1,1,1);
//    // texture2D是内置函数,通过采样器采集到aCoord的颜色
//    vec4 color =  texture2D(vTexture,aCoord);
//    gl_FragColor = vec4(color.r,color.g,color.b,color.a);
//}

void main()
{
    lowp vec4 textureColor;
    lowp vec4 textureColorOri;

    float xCoordinate = aCoord.x;
    float yCoordinate = aCoord.y;

    highp float redCurveValue;
    highp float greenCurveValue;
    highp float blueCurveValue;

    textureColor = texture2D( vTexture, vec2(xCoordinate, yCoordinate));
    textureColorOri = textureColor;
    // step1 vTexture 
    redCurveValue = texture2D(vTexture, vec2(textureColor.r, 0.0)).r;
    greenCurveValue = texture2D(vTexture, vec2(textureColor.g, 0.0)).g;
    blueCurveValue = texture2D(vTexture, vec2(textureColor.b, 0.0)).b;
    // step2 level
    redCurveValue = texture2D(vTexture, vec2(redCurveValue, 0.0)).a;
    greenCurveValue = texture2D(vTexture, vec2(greenCurveValue, 0.0)).a;
    blueCurveValue = texture2D(vTexture, vec2(blueCurveValue, 0.0)).a;
    // step3 brightness/constrast adjust 
    redCurveValue = redCurveValue * 1.25 - 0.12549;
    greenCurveValue = greenCurveValue * 1.25 - 0.12549;
    blueCurveValue = blueCurveValue * 1.25 - 0.12549;
    //redCurveValue = (((redCurveValue) > (1.0)) ? (1.0) : (((redCurveValue) < (0.0)) ? (0.0) : (redCurveValue)));
    //greenCurveValue = (((greenCurveValue) > (1.0)) ? (1.0) : (((greenCurveValue) < (0.0)) ? (0.0) : (greenCurveValue)));
    //blueCurveValue = (((blueCurveValue) > (1.0)) ? (1.0) : (((blueCurveValue) < (0.0)) ? (0.0) : (blueCurveValue)));
    // step4 normal blending with original
    textureColor = vec4(redCurveValue, greenCurveValue, blueCurveValue, 1.0);
    textureColor = (textureColorOri - textureColor) * 0.549 + textureColor;

    gl_FragColor = vec4(textureColor.r, textureColor.g, textureColor.b, 1.0);
} 
