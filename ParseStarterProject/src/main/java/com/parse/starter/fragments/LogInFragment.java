package com.parse.starter.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.starter.MainActivity;
import com.parse.starter.R;
import com.parse.starter.UserListActivity;

/**
 * Created by Trung Nguyen on 12/8/2016.
 */
public class LogInFragment extends Fragment {
    private static final String TAG = LogInFragment.class.getSimpleName();
    EditText etLogInUser, etLogInPass;
    Button btLogIn;
    ImageView imgCreate;
    CheckBox cbSaveLogin;
    public static final String LOG_IN = "login";
    private boolean Save;
    private boolean isLogOut;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.log_in_fragment, container, false);
        addControls(mView);
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
    }

}
