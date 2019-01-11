package com.example.user.sealseeksee;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2001;
    public static final String TAG ="HONG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button letter_find_btn = (Button) findViewById(R.id.letter_find_btn);
        Button letter_send_btn = (Button) findViewById(R.id.letter_send_btn);
        Button serviceIntroduce_btn = (Button) findViewById(R.id.service_introduce);

        letter_find_btn.setOnClickListener(this);
        letter_send_btn.setOnClickListener(this);
        serviceIntroduce_btn.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkAndRequestPermissions();

    }
    private boolean checkAndRequestPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        listPermissionsNeeded.clear();

        int internetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        if (internetPermission != PackageManager.PERMISSION_GRANTED) listPermissionsNeeded.add(Manifest.permission.INTERNET);
        int access_loc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (access_loc != PackageManager.PERMISSION_GRANTED) listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);

        if (!listPermissionsNeeded.isEmpty()) {
            Log.d(TAG, "checkAndRequestPermissions: not empty");
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


    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.letter_find_btn :
                startActivity(new Intent(MainActivity.this,FindLetterActivity.class));
                break;
            case R.id.letter_send_btn :
                Log.d(TAG, "??? ");
                startActivity(new Intent(MainActivity.this,SendLetterActivity.class));
                break;
            case R.id.service_introduce :
                break;
            default :
                break;

        }

    }
}
