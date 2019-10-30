package com.xopengl.xopenglcamera.camera.filter;

import android.content.Context;

import com.xopengl.xopenglcamera.R;

/**
 * 翻转
 * @author Lixingxing
 */
public class FlipFilter extends BaseFilter{
    public FlipFilter(Context context) {
        super(context, R.raw.screen_vert, R.raw.screen_frag);
    }

    @Override
    protected void initCoord() {
        TEXTURE = new float[]{ 0f,0f,
                0f,1f,
                1f,0f,
                1f,1f,
               };
        super.initCoord();
    }
}
