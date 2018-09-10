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

public class TrackingLetter extends AppCompatActivity implements OnMapReadyCallback, LocationListener {


    public static double double_lati = -1;
    public static double double_long = -1;

    private LocationManager locationManager;

    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLcationProviderClient;

    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_letter);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        mFusedLcationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                1, locationListenerGPS);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        get_current_location();



    }

    @Override
    public void onLocationChanged(Location location)
    {


        LatLng current_position = new LatLng(location.getLatitude(),location.getLongitude());

        double distance = cal_distance(double_lati,double_long,current_position);
        TextView dis_text = (TextView)findViewById(R.id.distance);
        dis_text.setText((int)distance+"m 남았습니다.");

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onMapReady(final GoogleMap map)
    {

        mMap=map;
        String phone1 = "";
        String phone2 = "";
        String phone3 = "";
        String word1 = "";
        String word2 = "";
        String word3 = "";

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

        TextView res_mesg = (TextView) findViewById(R.id.letter_msseage);

        res_mesg.setText(phone1 + phone2 + phone3 + "" + "\n" + word1 + " " + word2 + " " + word3 + " " +
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);

//        Button find_my_letter=(Button)findViewById(R.id.letter_find_btn2);
//
//        find_my_letter.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//
//
//
//            }
//        });
//


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 16));


        final Button read_btn = (Button) findViewById(R.id.letter_read_btn);
        final Button refresh_btn = (Button) findViewById(R.id.refresh_location);

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
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view)
            {
                LatLng myLocation = getCurrentLocation(map);

                double distance = -1;

                distance = cal_distance(double_lati, double_long, myLocation);

                TextView dis = (TextView) findViewById(R.id.distance);

                dis.setText((int)distance + "m 남았습니다");

                Button read_btn_update = (Button)findViewById(R.id.letter_read_btn);

                if(distance<50.0)
                {

                    read_btn_update.setClickable(true);
                    read_btn_update.setBackground(getResources().getDrawable(R.drawable.btn_on));
                }
                else {
                    read_btn_update.setClickable(false);
                    read_btn_update.setBackground(getResources().getDrawable(R.drawable.btn_off));

                }



            }
        });



    }

    private double cal_distance(double letter_lati, double letter_long, LatLng latLongB) {

        float[] results = new float[1];
        Location.distanceBetween(letter_lati, letter_long,
                latLongB.latitude, latLongB.longitude,
                results);

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

    private void get_current_location() {

        OnCompleteListener<Location> mCompleteListener = new OnCompleteListener<Location>()
        {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    mCurrentLocation = task.getResult();

                    LatLng current_position = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());

                    double distance = cal_distance(double_lati,double_long,current_position);
                    TextView dis_text = (TextView)findViewById(R.id.distance);
                    dis_text.setText((int)distance+"m 남았습니다");
//                    Toast.makeText(TrackingLetter.this, mCurrentLocation.getLatitude() + " " + mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();



                } else {

                    Log.d("error", "onComplete: error");
                }

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLcationProviderClient.getLastLocation().addOnCompleteListener(this, mCompleteListener);


    }

    LocationListener locationListenerGPS=new LocationListener()
    {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onLocationChanged(android.location.Location location) {


            LatLng current_position = new LatLng(location.getLatitude(),location.getLongitude());
            double dis = cal_distance(double_lati,double_long,current_position);
            TextView update_dis =(TextView)findViewById(R.id.distance);
            update_dis.setText((int)dis+"m 남았습니다");
            Button read_btn_update = (Button)findViewById(R.id.letter_read_btn);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_position, 16));

            if(dis<50.0)
            {

                read_btn_update.setClickable(true);
                read_btn_update.setBackground(getResources().getDrawable(R.drawable.btn_on));
            }
            else {
                read_btn_update.setClickable(false);
                read_btn_update.setBackground(getResources().getDrawable(R.drawable.btn_off));

            }


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };




}
