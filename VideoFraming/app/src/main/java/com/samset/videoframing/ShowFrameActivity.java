package com.samset.videoframing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowFrameActivity extends AppCompatActivity {
ListView listView;
    ArrayList<Bitmap> bitmaps;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_frame);
        /*intent=getIntent();
        Bundle bundle=intent.getExtras();
        bitmaps= (ArrayList<Bitmap>) bundle.getSerializable("data");*/
        bitmaps=SecondActivity.rev;
        Log.e("Data"," sizee "+bitmaps.size());
        listView=(ListView)findViewById(R.id.list);
        listView.setAdapter(new MyAdapter());
    }



    public class MyAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return bitmaps.size();
        }

        @Override
        public Object getItem(int position) {
            return bitmaps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return bitmaps.indexOf(getItem(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item, null);
                holder = new ViewHolder();
                holder.txtTitle = (TextView) convertView.findViewById(R.id.count);
                holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtTitle.setText("Bitmap "+position);
            holder.imageView.setImageBitmap(bitmaps.get(position));

            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            TextView txtTitle;

        }
    }
}
