package com.example.user.sealseeksee;

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

import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity
{

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2001;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        ImageView main_image = (ImageView)findViewById(R.id.letter_image);
        HongController.getInstance().setMyContext(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkAndRequestPermissions();

        Handler handler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        };
        handler.postDelayed(r,2000);





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
                } else {
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
