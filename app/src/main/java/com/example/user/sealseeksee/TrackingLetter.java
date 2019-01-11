package com.example.user.sealseeksee;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class TrackingLetter extends AppCompatActivity implements OnMapReadyCallback {


    public static double double_lati = -1;
    public static double double_long = -1;
    public static double my_lati,my_long;
    public static double calculatedDistance;

    private LocationManager locationManager;

    private OnLocationUpdatedListener locationListener;
    private Location mCurrentLocation;
    private Handler handler;
    private Runnable locationRunnable;
    private boolean refreshMyLocation = true;
    
    private String phone1,phone2,phone3,word1,word2,word3;

    TextView leftDisanceText,responseMessage;
    Button read_btn,read_btn_update;

    public static final String TAG = LetterConstants.TAG;

    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_letter);

        leftDisanceText = (TextView)findViewById(R.id.distance);
        responseMessage =(TextView) findViewById(R.id.letter_msseage);
        read_btn = (Button) findViewById(R.id.letter_read_btn);
        read_btn_update = (Button)findViewById(R.id.letter_read_btn);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map2);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SmartLocation.with(this).location().start(locationListener);

        handler = new Handler();
        locationListener = new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                my_lati = location.getLatitude();
                my_long = location.getLongitude();
                Log.d(TAG, "onLocationUpdated: "+my_lati+" , "+my_long);
                handler.postDelayed(locationRunnable,500);
            }
        };
        SmartLocation.with(this).location().start(locationListener);
        locationRunnable = new Runnable() {
            @Override
            public void run() {
                if(refreshMyLocation) SmartLocation.with(HongController.getInstance().getMyContext()).location().start(locationListener);
                updateDistance();
                updateReadButton();
            }
        };
    }

    private void updateReadButton() {
        if(calculatedDistance < 50.0)
        {
            read_btn_update.setClickable(true);
            read_btn_update.setBackground(getResources().getDrawable(R.drawable.btn_on));
        }
        else {
            read_btn_update.setClickable(false);
            read_btn_update.setBackground(getResources().getDrawable(R.drawable.btn_off));

        }
    }

    private void updateDistance() {
        LatLng current_position = new LatLng(my_lati,my_long);
        calculatedDistance = cal_distance(double_lati,double_long,current_position);
        leftDisanceText.setText((int)calculatedDistance+"m Left ! Fighting");
    }
    
    @Override
    public void onMapReady(final GoogleMap map)
    {

        mMap=map;
        String lat_lang = "";
        String latitude = "";
        String longitude = "";
        String title = "";
        String message = "";
        
        Intent intent = getIntent();
        if (intent != null) {
            phone1 = intent.getStringExtra("phone1");
            phone2 = intent.getStringExtra("phone2");
            phone3 = intent.getStringExtra("phone3");
            word1 = intent.getStringExtra("word1");
            word2 = intent.getStringExtra("word2");
            word3 = intent.getStringExtra("word3");
            title = intent.getStringExtra("title");
            message = intent.getStringExtra("message");
            latitude = intent.getStringExtra("latitude");
            longitude = intent.getStringExtra("longitude");
        }

        responseMessage.setText(phone1 + phone2 + phone3 + "" + "\n" + word1 + " " + word2 + " " + word3 + " " +
                "\n latitude :" + latitude + " longitude : " + longitude);

        double_lati = Double.parseDouble(latitude);
        double_long = Double.parseDouble(longitude);

        LatLng SEOUL = new LatLng(double_lati, double_long);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("New Letter");
        markerOptions.snippet("Arrived!");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.new_letter));
        map.addMarker(markerOptions);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 16));

        final String finalTitle = title;
        final String finalMessage = message;
        read_btn.setClickable(false);
        read_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng myLocation = getCurrentLocation(map);

                double distance = -1;
                distance = cal_distance(double_lati, double_long, myLocation);

                if(distance<50)
                {
                    final Dialog dialog2 = new Dialog(TrackingLetter.this);
                    dialog2.setContentView(R.layout.message_read);
                    dialog2.setTitle("Title...");

                    Button back_home = (Button)dialog2.findViewById(R.id.back_home);
                    TextView read_title = (TextView)dialog2.findViewById(R.id.read_title);
                    TextView read_message = (TextView)dialog2.findViewById(R.id.read_message);

                    read_title.setText(finalTitle);
                    read_message.setText(finalMessage);

                    back_home.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            dialog2.dismiss();
                            startActivity(new Intent(TrackingLetter.this,MainActivity.class));
                        }
                    });
                    dialog2.show();
                }
            }
        });
    }

    private double cal_distance(double letter_lati, double letter_long, LatLng latLongB) {

        float[] results = new float[1];
        Location.distanceBetween(letter_lati, letter_long, latLongB.latitude, latLongB.longitude, results);
        return (double) results[0];
    }

    private LatLng getCurrentLocation(GoogleMap mMap) {

        Location myLocation = mMap.getMyLocation();
        if (myLocation != null) {
            double dLatitude = myLocation.getLatitude();
            double dLongitude = myLocation.getLongitude();
            LatLng result = new LatLng(dLatitude, dLongitude);
            return result;
        } else {
            Toast.makeText(this, "Unable to fetch the current location", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
