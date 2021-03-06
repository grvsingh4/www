package com.grehseva.www;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.EntityIterator;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.grehseva.www.contants.ParseContants;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.nio.channels.AlreadyConnectedException;

public class signUpActivity extends AppCompatActivity {

    public static final String TAG = signUpActivity.class.getSimpleName();

    protected ParseRelation<ParseUser> mSupportRelation; //create relation with support team
    protected ParseUser mCurrentUser;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    protected EditText mUsername;
    protected EditText mUserphone;
    protected Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        mUsername = (EditText)findViewById(R.id.usernameField);
        mUserphone = (EditText)findViewById(R.id.userPhone);
        mSignUpButton = (Button)findViewById(R.id.SignUpButton);
        mSignUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String userphone = mUserphone.getText().toString();

                username = username.trim();
                userphone = userphone.trim();

                if(username.isEmpty() || userphone.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signUpActivity.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    //create users
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(username);
                    newUser.setPassword("grehseva" + username);
                    newUser.put("phone", userphone);
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                mCurrentUser = ParseUser.getCurrentUser();
                                ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("username");
                                userQuery.whereEqualTo("grehseva", ParseUser.getCurrentUser());
                                mSupportRelation = mCurrentUser.getRelation(ParseContants.KEY_SUPPORT_RELATION);
                                mCurrentUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e != null){
                                            Log.e(TAG, e.getMessage());
                                        }
                                    }
                                });//relation with grehseva complete.//
                                Intent intent = new Intent(signUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(signUpActivity.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle(R.string.signup_error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }
            }
        });

    }
}
