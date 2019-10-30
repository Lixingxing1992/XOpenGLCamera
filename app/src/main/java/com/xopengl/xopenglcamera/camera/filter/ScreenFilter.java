package com.xopengl.xopenglcamera.camera.filter;

import android.content.Context;
import com.xopengl.xopenglcamera.R;

/**
 * @author Lixingxing
 */
public class ScreenFilter extends BaseFilter{
    public ScreenFilter(Context context) {
        super(context, R.raw.screen_vert,R.raw.screen_frag);
    }
}
