package com.parse.starter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Trung Nguyen on 12/10/2016.
 */
public class FeedAdapter extends ArrayAdapter<Bitmap> {
    List<Bitmap> list;
    ViewHolder holder;
    Context mContext;

    public FeedAdapter(Context context, int resource, List<Bitmap> objects) {
        super(context, resource, objects);
        list = objects;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            ViewHolder holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.feed_item, parent, false);
            holder.imgUserFeed = (ImageView) convertView.findViewById(R.id.imgUserFeed);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.imgUserFeed.setImageBitmap(list.get(position));
        }
        return convertView;
    }

    private class ViewHolder {
        public ImageView imgUserFeed;
    }
}
