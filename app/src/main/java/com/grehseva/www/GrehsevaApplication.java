package com.grehseva.www;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;

/**
 * Created by Gaurav Singh on 9/5/2015.
 */

public class GrehsevaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "qS1djL7cAkl4hAWyu73MdDlMlTaelWQdbIL8xCUO", "aPUcPStBX8hRikw4KQyEmOQxI4AN2NrLKfIuEIvI");
        ParseFacebookUtils.initialize(this);
        ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
    }

}
