package com.example.user.sealseeksee;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.MathContext;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

import static com.example.user.sealseeksee.LetterUtils.location_processing;

public class SendLetterActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener
{

    public static final String TAG ="HONG";
    private static double my_lati;
    private static double my_long;
    private static String myW3W;
    private static boolean firstTimeLocSet = true;

    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLcationProviderClient;

    private httpConnectionToPhwysl httpConnectionToPhwysl;
    private static GoogleMap myMap;

    Button get_my_position;
    TextView response_msg,text1;

    private OnLocationUpdatedListener locationListener;
    private Runnable locationRunnable,initRunnable;
    private Context ctx;
    private Handler handler;
    private boolean refreshMyLocation = true;
    private int postionUpdateCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_letter);

        get_my_position=(Button)findViewById(R.id.send_letter_msg);
        response_msg = (TextView) findViewById(R.id.trans_words);
        text1 = (TextView) findViewById(R.id.text1);

        ctx = this;

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toast.makeText(this,"Searching for Current Location",Toast.LENGTH_LONG).show();

        handler = new Handler();
        locationListener = new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                my_lati = location.getLatitude();
                my_long = location.getLongitude();
                HongController.getInstance().setMy_lati(my_lati);
                HongController.getInstance().setMy_long(my_long);
                if(firstTimeLocSet)
                {
                    Log.d(TAG, "first Time Setting : ");
                    markCurrentPosition();
                    transaction(location_processing(my_lati,my_long),0);
                    firstTimeLocSet = false;
                }
                Log.d(TAG, "onLocationUpdated: "+my_lati+" , "+my_long);
                handler.postDelayed(locationRunnable,1000);
            }
        };
        SmartLocation.with(this).location().start(locationListener);

        locationRunnable = new Runnable() {
            @Override
            public void run() {
                markCurrentPosition();
                transaction(location_processing(my_lati,my_long),0);
                if(refreshMyLocation) SmartLocation.with(ctx).location().start(locationListener);
            }
        };

    }

    private void checkHasInfoOfLocation() {
        String my_w3w;
        double my_lati,my_long;
        my_w3w = HongController.getInstance().getMy_w3w();
        my_lati = HongController.getInstance().getMy_lati();
        my_long = HongController.getInstance().getMy_long();

        Log.d(TAG, "checkHasInfoOfLocation: "+my_w3w+" "+my_lati+" "+my_long);
        if(my_lati != -1 && my_long != -1 && my_w3w!=null)
        {
            Log.d(TAG, "my location is already set!");
            response_msg.setText(my_w3w);
            myMap.clear();
            myMap.addMarker(new MarkerOptions()
                    .position(new LatLng(my_lati, my_long))
                    .title("CurrentMyLocation")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.person_walking)));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(my_lati, my_long), 16));
        }
    }


    @Override
    public void onMapReady(final GoogleMap map) {

        myMap = map;
        checkHasInfoOfLocation();
        get_my_position.setOnClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        map.setMyLocationEnabled(true);

        LatLng SEOUL = new LatLng(37.56, 126.97);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL,16));
        clearMap();


    }

    private void clearMap()
    {
        myMap.clear();
    }

    private void markCurrentPosition()
    {
        if(postionUpdateCount%3==0) {
            Log.d(TAG, "updated current position. period : 3-times retrying.");
            myMap.clear();
            myMap.addMarker(new MarkerOptions()
                    .position(new LatLng(my_lati, my_long))
                    .title("CurrentMyLocation")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.person_walking)));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(my_lati, my_long), 16));
        }
        postionUpdateCount++;
    }

    private void initilizeMyLocation()
    {
        Location myLocation = myMap.getMyLocation();
        if(myLocation != null)
        {
            my_lati = myLocation.getLatitude();
            my_long = myLocation.getLongitude();
            Log.d(TAG, "dLat : "+my_lati);
            Log.d(TAG,"dLong : "+my_long);
        }
    }

    void sendMyLetter(GoogleMap mMap) {
        Location myLocation  = mMap.getMyLocation();
        if(myW3W!=null)
        {
            movePageToLetterWrite(myW3W);
        }
        else
        {
            Toast.makeText(this, "Unable to fetch the current location", Toast.LENGTH_SHORT).show();
        }

    }


    public void transaction(String position, final int SendOrNot) {

        String link = "https://api.what3words.com/v2/reverse?coords=";
        if(position==null) {
            Log.d(TAG, "transaction: position is null");
            link += "51.521251,-0.203586";
        }
        else
        {
            link+=position;
        }
        link+= "&display=full&format=json&key=KYM3G8LX";
        link += "&lang=ko";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestHandle requestHandle = client.get(link, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject responseBody)
            {
//                Log.d(TAG, "sendActivity w3w response body : "+responseBody.toString());
                setW3W(responseBody); //set
            }
        });
    }

    public void setW3W(JSONObject response)
    {

        text1.setText("Address is transformed to WHAT3WORDS ");
        try {
            response_msg.setText(response.getString("words"));
            myW3W = response.getString("words");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void movePageToLetterWrite(String w3w_words)
    {
        refreshMyLocation = false;
        Intent i = new Intent(SendLetterActivity.this,LetterWrite.class);
        i.putExtra("w3w",w3w_words);
        i.putExtra("my_lati",my_lati);
        i.putExtra("my_long",my_long);
        startActivity(i);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.send_letter_msg:
                sendMyLetter(myMap);
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed Called");
        startActivity(new Intent(this, MainActivity.class));
    }
}
