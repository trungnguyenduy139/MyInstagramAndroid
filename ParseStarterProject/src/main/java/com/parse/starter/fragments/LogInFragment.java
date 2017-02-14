package com.parse.starter.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.starter.MainActivity;
import com.parse.starter.R;
import com.parse.starter.UserListActivity;

import java.io.IOException;

/**
 * Created by Trung Nguyen on 12/8/2016.
 */
public class LogInFragment extends Fragment implements SurfaceHolder.Callback {
    private static final String TAG = LogInFragment.class.getSimpleName();
    EditText etLogInUser, etLogInPass;
    Button btLogIn;
    ImageView imgCreate;
    CheckBox cbSaveLogin;
    SurfaceView vvLogin;
    MediaPlayer mp;
    public static final String LOG_IN = "login";
    private boolean Save;
    private boolean isLogOut;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.log_in_fragment, container, false);
        addControls(mView);
//        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.nature);
////        vvLogin.setData(uri);
////        //start in onResume
////        vvLogin.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
////            @Override
////            public void onPrepared(MediaPlayer mediaPlayer) {
////                Log.d(TAG, "onPrepared");
////                mediaPlayer.setLooping(true);
////            }
////        });
        mp = new MediaPlayer();
        Uri video = Uri.parse("android.resource://" + getActivity().getPackageName() + "/"
                + R.raw.nature);

        try {
            mp.setDataSource(getActivity(),video);
            mp.prepare();
            mp.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        vvLogin.getHolder().addCallback(this);
        SharedPreferences preferences = getActivity().getSharedPreferences(this.LOG_IN, getActivity().MODE_PRIVATE);
        isLogOut = getActivity().getIntent().getBooleanExtra("IS_LOGOUT", false);
        if (!isLogOut) {
            Save = preferences.getBoolean("save", false);
        } else {
            cbSaveLogin.setChecked(false);
            Save = false;
        }
        if (Save && !isLogOut) {
            String user = getActivity().getSharedPreferences(LogInFragment.LOG_IN, getActivity().MODE_PRIVATE).getString("username", "");
            String pass = getActivity().getSharedPreferences(LogInFragment.LOG_IN, getActivity().MODE_PRIVATE).getString("password", "");
            ParseUser.logInInBackground(user, pass, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), UserListActivity.class);
                                intent.putExtra("SAVE", cbSaveLogin.isChecked());
                                getActivity().startActivity(intent);
                            }
                        });
                    }
                }
            });
        } else {
            btLogIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user = etLogInUser.getText().toString();
                    String pass = etLogInPass.getText().toString();

                    ParseUser.logInInBackground(user, pass, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, final ParseException e) {
                            if (e == null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getActivity(), UserListActivity.class);
                                        intent.putExtra("SAVE", cbSaveLogin.isChecked());
                                        getActivity().startActivity(intent);
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
        imgCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpFragment signUpFragment = new SignUpFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainActivityHolder, signUpFragment, MainActivity.SIGN_UP_FRAGMENT);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        ParseAnalytics.trackAppOpenedInBackground(getActivity().getIntent());
        return mView;
    }

    private void addControls(View view) {
        etLogInUser = (EditText) view.findViewById(R.id.etLogInUser);
        etLogInPass = (EditText) view.findViewById(R.id.etLogInPass);
        btLogIn = (Button) view.findViewById(R.id.btLogIn);
        imgCreate = (ImageView) view.findViewById(R.id.imgCreate);
        cbSaveLogin = (CheckBox) view.findViewById(R.id.cbSaveLogin);
        vvLogin = (SurfaceView) view.findViewById(R.id.vvLogin);
    }

    @Override
    public void onStart() {
        super.onStart();
        etLogInUser.setText("");
        etLogInPass.setText("");
        etLogInUser.requestFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences preferences = getActivity().getSharedPreferences(this.LOG_IN, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", etLogInUser.getText().toString());
        editor.putString("password", etLogInPass.getText().toString());
        if (cbSaveLogin.isChecked())
            Save = true;
        editor.putBoolean("save", Save);
        editor.commit();
        mp.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
//        vvLogin.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");


        //Get the dimensions of the video
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();

        //Get the width of the screen
        int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        //Get the SurfaceView layout parameters
        android.view.ViewGroup.LayoutParams lp = vvLogin.getLayoutParams();

        //Set the width of the SurfaceView to the width of the screen
        lp.width = screenWidth;

        //Set the height of the SurfaceView to match the aspect ratio of the video
        //be sure to cast these as floats otherwise the calculation will likely be 0
        lp.height = (int) (((float)videoHeight / (float)videoWidth) * (float)screenWidth);

        //Commit the layout parameters
        vvLogin.setLayoutParams(lp);

        //Start video
        mp.setDisplay(holder);
        mp.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
