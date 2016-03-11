package com.samset.videoframing.utils;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by weesync on 04/03/16.
 */
public class PassData {
    public static PassData data=new PassData();
    ArrayList<Bitmap> listData;

    public static PassData getInstance()
    {
        return data;
    }

    public void setData(ArrayList<Bitmap> setData)
    {
      this.listData=setData;
    }
    public ArrayList<Bitmap> getListData()
    {
        return listData;
    }
}
