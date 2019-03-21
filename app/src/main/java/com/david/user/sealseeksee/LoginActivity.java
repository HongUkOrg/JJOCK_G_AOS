package com.david.user.sealseeksee;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity
{

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2001;
    private Context mContext;
    private SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
//        ImageView main_image = (ImageView)findViewById(R.id.letter_image);
        HongController.getInstance().setMyContext(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if(checkAndRequestPermissions()){
//                Handler handler = new Handler();
//                Runnable r = new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity(new Intent(LoginActivity.this,MainViewActivity.class));
//                    }
//                };
//                handler.postDelayed(r,1000);
            }
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Log.d("HONG", "onSessionOpenFailed: "+exception.toString());
            }
        }
    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, MainViewActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean checkAndRequestPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        listPermissionsNeeded.clear();

        int internetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        if (internetPermission != PackageManager.PERMISSION_GRANTED) listPermissionsNeeded.add(Manifest.permission.INTERNET);
        int access_loc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (access_loc != PackageManager.PERMISSION_GRANTED) listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        int read_phone_state = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (read_phone_state != PackageManager.PERMISSION_GRANTED) listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);

        if (!listPermissionsNeeded.isEmpty()) {
            Log.d("HONG", "checkAndRequestPermissions: not empty");
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    Handler handler = new Handler();
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
//                            startActivity(new Intent(LoginActivity.this,MainViewActivity.class));
                        }
                    };
                    handler.postDelayed(r,1000);
                }
                else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Please Allow All Permission To Continue..", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            break;
        }
    }
}
