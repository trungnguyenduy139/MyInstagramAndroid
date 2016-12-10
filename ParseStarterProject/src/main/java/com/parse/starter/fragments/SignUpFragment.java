package com.parse.starter.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.starter.R;

import java.util.List;

/**
 * Created by Trung Nguyen on 12/8/2016.
 */
public class SignUpFragment extends Fragment {
    private static final String TAG = SignUpFragment.class.getSimpleName();
    EditText etUser, etPass;
    Button btSignUp;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.sign_up_fragment, container, false);
        addControls(mView);
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userTemp = etUser.getText().toString();
                String pass = etPass.getText().toString();
                ParseUser user = new ParseUser();
                if (!userTemp.isEmpty() || !pass.isEmpty()) {
                    if (checkExistUserName()) {
                        Toast.makeText(getActivity(), "User Name is Existed", Toast.LENGTH_SHORT).show();
                    } else {
                        user.setUsername(userTemp);
                        user.setPassword(pass);
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.d(TAG, "Sign Up Successful");
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "Sign Up Failed " + e.toString());
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "Wrong User name or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ParseAnalytics.trackAppOpenedInBackground(getActivity().getIntent());
        return mView;
    }

    private boolean checkExistUserName() {
        return false;
    }

    private void addControls(View view) {
        etUser = (EditText) view.findViewById(R.id.etSignUpUser);
        etPass = (EditText) view.findViewById(R.id.etSignUpPass);
        btSignUp = (Button) view.findViewById(R.id.btSignUp);
    }

}
