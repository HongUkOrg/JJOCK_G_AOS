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


public class MainActivity extends AppCompatActivity {

    Button response_btn;
    Button mylocation_btn;
    private LocationManager mLocationManager;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();
        }

        Button letter_find_btn = (Button) findViewById(R.id.letter_find_btn);

        letter_find_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,FindLetterActivity.class));

            }
        });

        mylocation_btn = (Button) findViewById(R.id.letter_send_btn);
        mylocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(MainActivity.this,SendLetterActivity.class));

            }
        });





    }

    private boolean checkAndRequestPermissions() {

        List<String> listPermissionsNeeded = new ArrayList<>();
        listPermissionsNeeded.clear();


        int internet = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);//

        //int read_concise_call_state= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PRECISE_PHONE_STATE);//


        if (internet != PackageManager.PERMISSION_GRANTED) {

            listPermissionsNeeded.add(Manifest.permission.INTERNET);

        }


        int access_loc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);//
        if (access_loc != PackageManager.PERMISSION_GRANTED) {

            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);

        }

        if (!listPermissionsNeeded.isEmpty()) {

            Log.d("permission", "checkAndRequestPermissions: not empty");

            Toast.makeText(this, "not empty", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
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




    public void transaction(String position) {

        String link = "https://api.what3words.com/v2/reverse?coords=";

        if(position==null) {
            link += "51.521251,-0.203586";
        }
        else
        {
            link+=position;
        }
        link+= "&display=full&format=json&key=KYM3G8LX";
        AsyncHttpClient client = new AsyncHttpClient();


        RequestHandle requestHandle = client.get(link, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject responseBody) {




            }


        });
    }



    private String get_my_long_lat() {

        String result = "Fail to Access My fine Location";



        mLocationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();

        }



        double longitude = -1;
        double latitude = -1;
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }


        BigDecimal bd = new BigDecimal(latitude);
        bd = bd.round(new MathContext(6));
        double rounded_lati = bd.doubleValue();

        BigDecimal bd2 = new BigDecimal(longitude);
        bd = bd.round(new MathContext(6));
        double rounded_long = bd2.doubleValue();

        result = "";

        result += Double.toString(rounded_lati) + "," + Double.toString(rounded_long);

        Log.d("my_loc", "get_my_long_lat: "+result);

        return result;

    }


}
