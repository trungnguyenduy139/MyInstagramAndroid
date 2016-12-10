package com.parse.starter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    EditText etLogInUser, etLogInPass;
    Button btLogIn;
    ImageView imgCreate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.log_in_fragment, container, false);
        addControls(mView);
        ParseUser.logOut();
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
    }

    @Override
    public void onStart() {
        super.onStart();
        etLogInUser.setText("");
        etLogInPass.setText("");
        etLogInUser.requestFocus();
    }
}
