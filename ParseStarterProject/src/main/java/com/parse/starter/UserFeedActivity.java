package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserFeedActivity extends AppCompatActivity {
    List<Bitmap> bitmapList;
    ListView lvUserFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        lvUserFeed = (ListView) findViewById(R.id.lvUserFeed);
        bitmapList = new ArrayList<Bitmap>();
        final FeedAdapter adapter = new FeedAdapter(this, R.layout.feed_item, bitmapList);
        Intent intent = getIntent();
        String selectedUserName = intent.getStringExtra("USER_NAME");
        setTitle(selectedUserName + "'s Feed");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("images");
        query.whereEqualTo("username", selectedUserName);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            ParseFile file = (ParseFile) object.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        bitmapList.add(bitmap);
                                    }
                                }
                            });
                        }
                        lvUserFeed.setAdapter(adapter);
                    }
                }
            }
        });
    }

    private class FeedAdapter extends ArrayAdapter<Bitmap> {
        List<Bitmap> list;
        ViewHolder holder;

        public FeedAdapter(Context context, int resource, List<Bitmap> objects) {
            super(context, resource, objects);
            list = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                ViewHolder holder = new ViewHolder();
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.feed_item, parent, false);
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
}
