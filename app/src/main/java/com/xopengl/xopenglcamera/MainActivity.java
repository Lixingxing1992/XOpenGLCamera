package com.xopengl.xopenglcamera;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xopengl.xopenglcamera.camera.MyOpenGLView;
import com.xopengl.xopenglcamera.camera.config.FilterType;
import com.xopengl.xopenglcamera.camera.filter.FlipFilter;
import com.xopengl.xopenglcamera.camera.filter.ScreenFilter;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    MyOpenGLView myOpenGlView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissionRequest();
    }

    public void checkPermissionRequest() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.setLogging(true);
        permissions.request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            setContentView(R.layout.activity_main);
                            myOpenGlView = findViewById(R.id.myOpenGlView);
                        }
                    }
                });
    }

    public void clickButton(View view) {
        switch (view.getId()) {
            case R.id.defaultButton:
                myOpenGlView.setFilterType(FilterType.DEFAULT);
                break;
            case R.id.filpButton:
                myOpenGlView.setFilterType(FilterType.FLIP);
                break;
            case R.id.whiteButton:
                myOpenGlView.setFilterType(FilterType.WHITE);
                break;
            default:
                break;
        }
    }
}
