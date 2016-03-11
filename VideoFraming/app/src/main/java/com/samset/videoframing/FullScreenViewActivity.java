package com.samset.videoframing;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.samset.videoframing.R;
import com.samset.videoframing.adapter.FullFrameDisplayAdapter;
import com.samset.videoframing.utils.PassData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullScreenViewActivity extends AppCompatActivity {

ViewPager viewPager;
    FullFrameDisplayAdapter displayAdapter;
    ArrayList<Bitmap> datalist;
    Intent intent;
    private int clickposition=0;
    public FullScreenViewActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_full_frame_display);
        viewPager= (ViewPager)findViewById(R.id.pager);
        intent=getIntent();
        clickposition=intent.getIntExtra("position", 0);
        datalist= PassData.getInstance().getListData();
        displayAdapter=new FullFrameDisplayAdapter(this,datalist);
        viewPager.setAdapter(displayAdapter);
        viewPager.setCurrentItem(clickposition);

    }



}
