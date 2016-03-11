package com.samset.videoframing.fragments;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.samset.videoframing.R;
import com.samset.videoframing.SecondActivity;
import com.samset.videoframing.adapter.FrameDisplayAdapter;
import com.samset.videoframing.utils.Constants;
import com.samset.videoframing.utils.Utils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrameDisplayFragment extends Fragment {

    private Utils utils;
    //private ArrayList<String> imagePaths = new ArrayList<String>();
    private FrameDisplayAdapter adapter;
    private GridView gridView;
    private int columnWidth;
    View view;
    private static ArrayList<Bitmap> bitmapdata;
    public static FrameDisplayFragment fragment=new FrameDisplayFragment();
    public FrameDisplayFragment() {
        // Required empty public constructor
    }

    public static FrameDisplayFragment getinstance() {
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frame_display, container, false);
        gridView = (GridView) view.findViewById(R.id.grid_view);
        utils = new Utils(getActivity());
        bitmapdata=SecondActivity.rev;
        Log.e("Frag"," Fragment "+bitmapdata.size());

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initilizing Grid View
        InitilizeGridLayout();

        // loading all image paths from SD card
        //imagePaths = utils.getFilePaths();
        // Gridview adapter
        adapter = new FrameDisplayAdapter(getActivity(), bitmapdata, columnWidth);
        // setting grid view adapter
        gridView.setAdapter(adapter);
    }

    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Constants.GRID_PADDING, r.getDisplayMetrics());
        columnWidth = (int) ((utils.getScreenWidth() - ((Constants.NUM_OF_COLUMNS + 1) * padding)) / Constants.NUM_OF_COLUMNS);

        gridView.setNumColumns(Constants.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

}
