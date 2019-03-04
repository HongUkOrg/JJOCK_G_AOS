package com.example.user.sealseeksee;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

import static com.example.user.sealseeksee.LetterUtils.location_processing;

public class LetterMainActivity extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener
{

    public static final String TAG ="HONG";
    private static double my_lati;
    private static double my_long;
    private static String myW3W;
    protected static double my_selected_lati;
    protected static double my_selected_long;
    protected static String my_selected_W3W;
    private static boolean firstTimeLocSet = true;
    private static boolean viewSubButtons = true;
    private static boolean infoViewState = false;

    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLcationProviderClient;

    private httpConnectionToPhwysl httpConnectionToPhwysl;
    private static GoogleMap myMap;

    private Button main_save_letter_btn,main_btn,main_find_letter_btn,info_question_btn;
    protected RelativeLayout infoView;
    private TextView w3w_textView, currentState_textView,info_text;
    private LinearLayout myCurrentLocationTXT;
    private FrameLayout fragment;


    private OnLocationUpdatedListener locationListener;
    private Runnable locationRunnable,initRunnable;
    protected static Context ctx;
    private Handler handler;
    private boolean refreshMyLocation = true;
    private int postionUpdateCount = 0;

    private static int moveHorizontalOffset = 20;
    private static int moveVerticalOffset = 20;
    private static int moveInfoVerticalOffset = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_letter);

        main_save_letter_btn =(Button)findViewById(R.id.main_save_letter_btn);
        main_find_letter_btn =(Button)findViewById(R.id.main_find_letter_btn);
        info_question_btn = (Button)findViewById(R.id.info_question_btn);
        main_btn = (Button) findViewById(R.id.main_btn);
        myCurrentLocationTXT = (LinearLayout) findViewById(R.id.topTitleMyCurrentLocation);
        w3w_textView = (TextView) findViewById(R.id.trans_words);
        currentState_textView = (TextView) findViewById(R.id.text1);
        infoView = (RelativeLayout)findViewById(R.id.Top);
        info_text = (TextView)findViewById(R.id.info_text);
        fragment = (FrameLayout)findViewById(R.id.changeFragment);


        ctx = this;
        resizeFragment();

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


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

    private void resizeFragment() {
        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(
                (int)(HongController.getInstance().getWidth()*0.92),(int)(HongController.getInstance().getHeight()*0.77));
        layout.gravity = Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
        fragment.setLayoutParams(layout);

        infoView.getLayoutParams().height = (int)(HongController.getInstance().getHeight()*0.1);
        infoView.requestLayout();

        ContentFrameLayout.LayoutParams params = new ContentFrameLayout.LayoutParams(
                (int)(HongController.getInstance().getWidth()*0.8),(int)(HongController.getInstance().getHeight()*0.05));
        params.setMargins(0,(int)(HongController.getInstance().getHeight()*0.08),0,0);
        params.gravity = Gravity.TOP|Gravity.CENTER_HORIZONTAL;
        w3w_textView.setLayoutParams(params);

//        w3w_textView.getLayoutParams().height = (int)(HongController.getInstance().getHeight()*0.05);
//        w3w_textView.getLayoutParams().width =  (int)(HongController.getInstance().getWidth()*0.8);
//        w3w_textView.requestLayout();


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

            w3w_textView.setText(getMyProcessedW3W(my_w3w));
            myMap.clear();
            myMap.addMarker(new MarkerOptions()
                    .position(new LatLng(my_lati, my_long))
                    .title("CurrentMyLocation")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_letter)));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(my_lati, my_long), 16));
        }
    }


    @Override
    public void onMapReady(final GoogleMap map) {

        myMap = map;
        checkHasInfoOfLocation();
        main_btn.setOnClickListener(this);
        main_save_letter_btn.setOnClickListener(this);
        main_find_letter_btn.setOnClickListener(this);
        info_question_btn.setOnClickListener(this);

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
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_letter)));
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
        RequestHandle requestHandle = client.get(link, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject responseBody) {

                setW3W(responseBody);
            }
        });
    }

    public void setW3W(JSONObject response)
    {
        if(HongController.writingNow) return;
        currentState_textView.setText("나의 현재 주소");
        try {
            w3w_textView.setText(getMyProcessedW3W(response.getString("words")));
            myW3W = response.getString("words");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void movePageToLetterWrite(String w3w_words)
    {
        refreshMyLocation = false;

        Intent i = new Intent(LetterMainActivity.this,LetterWrite.class);
        i.putExtra("w3w",w3w_words);
        i.putExtra("my_lati",my_lati);
        i.putExtra("my_long",my_long);
        startActivity(i);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.main_btn :
                viewSubButtons(v);
                break;
            case R.id.main_save_letter_btn:
                viewSaveLetterFragmment();
                translateW3Wbar(1);
//                sendMyLetter(myMap);
                break;
            case R.id.main_find_letter_btn :
                startActivity(new Intent(LetterMainActivity.this,FindLetterActivity.class));
                    break;
            case R.id.info_question_btn:
                if(!infoViewState) changeInfomationView(600);
                else changeInfomationView(330);
                break;
            default:
                break;
        }
    }



    private void changeInfomationView(int length) {

        ValueAnimator anim = ValueAnimator.ofInt(infoView.getMeasuredHeight(), length);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = infoView.getLayoutParams();
                layoutParams.height = val;
                infoView.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(500);
        anim.start();
        if(infoViewState) {
            info_text.setVisibility(View.GONE);
        }
        else{
            info_text.setVisibility(View.VISIBLE);
        }
        infoViewState = !infoViewState;
    }

    private void viewSubButtons(View v) {
        if(viewSubButtons) {
            translateSubMenu(v,0);
            viewSubButtons = !viewSubButtons;
        }
        else {
            translateSubMenu(v,1);
            viewSubButtons = !viewSubButtons;
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed Called");
        startActivity(new Intent(this, MainActivity.class));
    }
    private void translateSubMenu(View v,int alpha)
    {
        Log.d(TAG, "translateSubMenu: start");
        main_find_letter_btn.animate().setDuration(1000)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .alpha(alpha)
                .translationX(moveHorizontalOffset*-1)
                .translationY(moveVerticalOffset).start();
        main_save_letter_btn.animate().setDuration(1000)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .alpha(alpha)
                .translationX(moveHorizontalOffset)
                .translationY(moveVerticalOffset).start();
        moveHorizontalOffset*=-1;
        moveVerticalOffset*=-1;
        return;

    }
    protected void translateW3Wbar(int i)
    {
        w3w_textView.animate().setDuration(500)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .translationX(0)
                .translationY(moveInfoVerticalOffset*i).start();
        if(i==1) {
            w3w_textView.setBackgroundResource(R.drawable.btn_round_white);
            currentState_textView.setText("쪽지 남기기");
        }
        else {
            w3w_textView.setBackgroundResource(R.drawable.btn_round_ganari);
            currentState_textView.setText("나의 현재 주소");
        }
        return;

    }

    public String getMyProcessedW3W(String myW3W) {
        Log.d(TAG, "origin w3w: "+myW3W);
        String w3ws[] = myW3W.split("\\.");
        String processedW3W = "";
        for(int i=0; i<w3ws.length;i++)
        {
            processedW3W+=w3ws[i];
            if(i!=w3ws.length-1) processedW3W+="\t\t\t\t/\t\t\t\t";
        }
        return processedW3W;
    }

    private void viewSaveLetterFragmment(){
        Log.d(TAG, "viewSaveLetterFragmment ");
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        HongController.getInstance().setMy_w3w(myW3W);
        HongController.getInstance().setMy_lati(my_lati);
        HongController.getInstance().setMy_long(my_long);
        /*
        lati,long 모두 컨트롤러로 이관필요
        위에 번들로 넘길필요 없음
         */
        transaction.setCustomAnimations(R.anim.open_letter_write_anim,R.anim.open_letter_write_anim2);
        Fragment frag = new LetterWriteFragment();
        transaction.replace(R.id.changeFragment, frag);
        transaction.commit();


    }

    public void viewSaveSuccessLetterFragmment() {
        Log.d(TAG, "viewSaveSuccessLetterFragmment ");
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.open_letter_write_anim,R.anim.open_letter_write_anim2);
        Fragment frag = new LetterSaveSuccessFragement();
        transaction.replace(R.id.changeFragment, frag);
        transaction.commit();


    }
}
