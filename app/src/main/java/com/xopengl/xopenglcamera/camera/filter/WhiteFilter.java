package com.xopengl.xopenglcamera.camera.filter;

import android.content.Context;

import com.xopengl.xopenglcamera.R;

/**
 * 白幕
 * @author Lixingxing
 */
public class WhiteFilter extends BaseFilter{
    public WhiteFilter(Context context) {
        super(context, R.raw.screen_vert, R.raw.white_frag);
    }
}
