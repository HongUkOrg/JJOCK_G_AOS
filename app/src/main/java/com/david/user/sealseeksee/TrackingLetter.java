package com.david.user.sealseeksee;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
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
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.util.Date;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class TrackingLetter extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener {


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

    private Date myTimeLockDate;
    private long myTimeLockLong;

    private TextView leftDisanceText,responseMessage,timeLockText,read_btn,read_btn_update;
    private TextView letterContent;
    private ImageView trackingCangcelBtn;

    private String receivedTitle,receivedContent;

    private LinearLayout trackingInfo,bottomButtons;

    public static final String TAG = LetterConstants.TAG;

    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_letter);

        leftDisanceText = (TextView)findViewById(R.id.distance);
        timeLockText = (TextView)findViewById(R.id.distance);
        read_btn = (TextView) findViewById(R.id.letter_read_btn);
        read_btn_update = (TextView)findViewById(R.id.letter_read_btn);
        trackingInfo = (LinearLayout) findViewById(R.id.tracking_info);
        letterContent = (TextView)findViewById(R.id.letter_content);
        bottomButtons = (LinearLayout)findViewById(R.id.bottomButtons);
        trackingCangcelBtn = (ImageView) findViewById(R.id.btn_tracking_cancel);

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
                Log.d(TAG, "onLocationUpdated-TrackingLetter: "+my_lati+" , "+my_long);
                handler.postDelayed(locationRunnable,2000);
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

        read_btn_update.setOnClickListener(this);
        trackingCangcelBtn.setOnClickListener(this);

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) letterContent.getLayoutParams();
        mlp.width = (int)(HongController.getInstance().getWidth()*0.88);
        mlp.height =(int)(HongController.getInstance().getHeight()*0.03);
        mlp.setMargins(0,0,0,(int)(HongController.getInstance().getHeight()*0.22));
        letterContent.setLayoutParams(mlp);


        ViewGroup.LayoutParams params1 = bottomButtons.getLayoutParams();
        params1.height = (int)(HongController.getInstance().getHeight()*0.22);
        bottomButtons.setLayoutParams(params1);


    }

    private void changeHeightOfLetterContent(int length) {
        ValueAnimator anim = ValueAnimator.ofInt(letterContent.getMeasuredHeight(), length);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = letterContent.getLayoutParams();
                layoutParams.width = (int)(HongController.getInstance().getWidth()*0.90);
                layoutParams.height = val;
                int offset = (int)(HongController.getInstance().getWidth()*0.05);
                int topOffset = (int)(HongController.getInstance().getWidth()*0.08);

                letterContent.setLayoutParams(layoutParams);
                letterContent.setPadding(offset,topOffset,offset,0);
            }
        });
        anim.setDuration(500);
        anim.start();
    }

    private void updateReadButton() {

        if(myTimeLockDate!=null && myTimeLockDate.after(new Date()))
        {
            read_btn_update.setClickable(false);
            read_btn_update.setBackground(getResources().getDrawable(R.drawable.btn_off));
            long diff = myTimeLockDate.getTime() - new Date().getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            long remainingHour = hours-days*24;
            timeLockText.setVisibility(View.VISIBLE);
            timeLockText.setText("편지가 봉인되어 있습니다.. "+days+"일 "+remainingHour+"시간 뒤에 열립니다.\n"+"오픈 시각 : "+myTimeLockDate.toString());

            return;

        }
        if(calculatedDistance < 50.0)
        {
            read_btn_update.setClickable(true);
            read_btn_update.setBackground(getResources().getDrawable(R.drawable.tracking_letter_read_btn_white));
            trackingInfo.setBackground(getResources().getDrawable(R.drawable.bottom_round_trans_ganari));

        }
        else {
            read_btn_update.setClickable(false);
            read_btn_update.setBackground(getResources().getDrawable(R.drawable.tracking_letter_read_btn_gray));
            trackingInfo.setBackground(getResources().getDrawable(R.drawable.botton_round_grary));

        }
    }

    private void updateDistance() {
        LatLng current_position = new LatLng(my_lati,my_long);
        calculatedDistance = cal_distance(double_lati,double_long,current_position);
        leftDisanceText.setText("쪽지까지\n앞으로 "+(int)calculatedDistance+" 미터!");
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
            message = intent.getStringExtra("message");
            latitude = intent.getStringExtra("latitude");
            longitude = intent.getStringExtra("longitude");
            myTimeLockLong = intent.getLongExtra("time_lock",-1);
            if(myTimeLockLong != -1)
            {
                myTimeLockDate = new Date();
                myTimeLockDate.setTime(myTimeLockLong);
            }


        }

        double_lati = Double.parseDouble(latitude);
        double_long = Double.parseDouble(longitude);

        LatLng SEOUL = new LatLng(double_lati, double_long);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("New Letter");
        markerOptions.snippet("Arrived!");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_letter));
        map.addMarker(markerOptions);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 16));

        receivedTitle = title;
        receivedContent = message;
        read_btn.setClickable(false);

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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.letter_read_btn :
                Log.d(TAG, "onClick::letter_read_btn");
                changeHeightOfLetterContent((int)(HongController.getInstance().getHeight()*0.48));
                if(receivedContent!=null) letterContent.setText((receivedContent));
            break;
            case R.id.btn_tracking_cancel:
                startActivity(new Intent(this,LetterMainActivity.class));
                break;
        }

    }


}
