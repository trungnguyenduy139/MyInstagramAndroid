/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.starter.fragments.LogInFragment;
import com.parse.starter.fragments.SignUpFragment;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String LOG_IN_FRAGMENT = "log_in_fragment";
    public static final String SIGN_UP_FRAGMENT = "sign_up_fragment";
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: Put new value in Parse server
//        ParseObject score = new ParseObject("Score");
//        score.put("username", "the");
//        score.put("score", 99);
//        score.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Log.i("SaveInBackground", "Successful");
//                } else {
//                    Log.i("SaveInBackground", "Failed");
//                    e.printStackTrace();
//                }
//            }
//        });
        // TODO: Update user
//        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Score"); // Score is my object we've already created
//        parseQuery.getInBackground("SqrE2QqWz0", new GetCallback<ParseObject>() { // GetCallback will call my query is complete
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                if (e == null) {
//                    // TODO: update "trung"'s score to 200
//                    object.put("score", 200);
//                    Update username
//                    object.put("username", "lan");
//                    object.saveInBackground();
//                }
//            }
//        });
        // TODO: 12/8/2016 query
//        final ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Score");
//        parseQuery.whereEqualTo("username", "trang");
//        parseQuery.setLimit(5) // set limit of user for query
//        parseQuery.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                if (e == null) {
//                    Log.i(TAG, "Retrieved " + objects.size() + " results");
//                    for (ParseObject object : objects) {
//                        object.put("score", 50);
//                        object.saveInBackground();
//                    }
//                }
//            }
//        });
//        ParseUser user = new ParseUser();
//        user.setUsername("trung");
//        user.setPassword("trung");
//        user.signUpInBackground(new SignUpCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e== null){
//                    Log.d(TAG, "Thành công");
//                }else {Log.d(TAG, "Thất Bại "+e.toString());
//                    ParseUser.logOut();
//                }
//            }
//        });
//        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        LogInFragment save = (LogInFragment) getSupportFragmentManager().findFragmentByTag(LOG_IN_FRAGMENT);
        if (save == null) {
            LogInFragment logInFragment = new LogInFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.mainActivityHolder, logInFragment, LOG_IN_FRAGMENT);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
