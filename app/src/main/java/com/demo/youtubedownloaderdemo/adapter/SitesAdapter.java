package com.demo.youtubedownloaderdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.demo.youtubedownloaderdemo.R;
import com.demo.youtubedownloaderdemo.constants.iConstants;


public class SitesAdapter extends BaseAdapter implements iConstants {

    private Context mContext;


    // Constructor
    public SitesAdapter(Context c){
        mContext = c;
    }

    @Override
    public int getCount() {
        return HomePageThumbs.length;
    }

    @Override
    public Object getItem(int position) {
        return HomePageThumbs[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(mContext).inflate(R.layout.model,parent,false);
        }
        ImageView img= convertView.findViewById(R.id.imageId);

        img.setImageResource(HomePageThumbs[position]);
       //img.setScaleType(ImageView.ScaleType.CENTER_CROP);
      //  imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
        return convertView;
    }
}
