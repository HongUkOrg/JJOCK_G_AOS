package com.david.user.sealseeksee;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONException;
import org.json.JSONObject;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String TAG ="HONG";
    private OnLocationUpdatedListener locationListener;
    private Context mContext;

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

        mContext = this;

        locationListener = new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                Log.d("HONG", "MyApplication : refresh my location");
                HongController.getInstance().setMy_long(location.getLongitude());
                HongController.getInstance().setMy_lati(location.getLatitude());

                String link = "https://api.what3words.com/v2/reverse?coords=";
//                String link = "https://api.what3words.com/v2/languages?format=json&key=KYM3G8LX";
                String position = LetterUtils.location_processing(location.getLatitude(),location.getLongitude());
                if(position ==null) {
                    Log.d(TAG, "location is null");
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
                        Log.d(TAG, "Response Words : "+responseBody.toString());
                        if(responseBody.has("words")) {
                            try {
                                HongController.setMy_w3w((String) responseBody.get("words"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Log.d(TAG, "MainApplication : jsonObject has not key 'words'");
                        }
                    }
                });



            }
        };

        SmartLocation.with(this).location().start(locationListener);


        //info dialog does not support veiw.onClickListener ....
    }



    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.letter_find_btn :
//                startActivity(new Intent(MainActivity.this,FindLetterActivity.class));
                break;
            case R.id.letter_send_btn :
                Log.d(TAG, "??? ");
                startActivity(new Intent(MainActivity.this,LetterMainActivity.class));
                break;
            case R.id.service_introduce :
                break;
            default :
                break;

        }

    }

}