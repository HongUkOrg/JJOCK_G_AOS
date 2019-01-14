package com.example.user.sealseeksee;

import android.Manifest;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FindLetterActivity extends AppCompatActivity implements HongController.LetterListener,View.OnClickListener
{

    private HttpURLConnection http;
    private EditText phone1,phone2,phone3,word1,word2,word3;
    private Button find_btn,sms_find_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_letter);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        find_btn = (Button)findViewById(R.id.find_Letter_search_btn);
        sms_find_btn = (Button)findViewById(R.id.find_by_sms);
        phone1 = (EditText)findViewById(R.id.phone1);
        phone2 = (EditText)findViewById(R.id.phone2);
        phone3 = (EditText)findViewById(R.id.phone3);
        word1 = (EditText)findViewById(R.id.first_word);
        word2 = (EditText)findViewById(R.id.second_word);
        word3 = (EditText)findViewById(R.id.third_word);

        find_btn.setOnClickListener(this);
        sms_find_btn.setOnClickListener(this);
        HongController.getInstance().setLetterListener(this);

    }

    @Override
    public void onReceiveLetter(String resultJson)
    {
        Log.d(LetterConstants.TAG, "onReceiveLetter Callback Method is Called : "+resultJson);

        String title,message,latitude,longitude;
        JSONObject myJson = null;
        String serarch_letter = resultJson;

        if(serarch_letter==null)
        {
            Log.d("HONG", ":cannot connect backend server.. ");
            return;
        }
        else
        {
            try {
                myJson = new JSONObject(serarch_letter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            if(myJson!=null)
            {
                title = myJson.getString("title");
                if(title.equals("find_fail"))
                {
                    FindLetterActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "편지를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                message = myJson.getString("message");
                latitude = myJson.getString("latitude");
                longitude = myJson.getString("longitude");

                Intent intent = new Intent(FindLetterActivity.this,TrackingLetter.class);

                intent.putExtra("phone1", phone1.getText().toString())
                        .putExtra("phone2", phone2.getText().toString())
                        .putExtra("phone3", phone3.getText().toString())
                        .putExtra("word1", word1.getText().toString())
                        .putExtra("word2", word2.getText().toString())
                        .putExtra("word3", word3.getText().toString())
                        .putExtra("title",title)
                        .putExtra("message",message)
                        .putExtra("latitude",latitude)
                        .putExtra("longitude",longitude);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(FindLetterActivity.this, "편지를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.find_Letter_search_btn:
                JSONObject myObj = new JSONObject();

                String w3w_adrress = "";
                String receiver_phone = "";

                w3w_adrress+=word1.getText().toString()+","+word2.getText().toString()+","+word3.getText().toString();
                receiver_phone+=phone1.getText().toString()+"-"+phone2.getText().toString()+"-"+phone3.getText().toString();

                Log.d(LetterConstants.TAG, "w3w, receiver_phone :  "+w3w_adrress+"   "+receiver_phone);
                try {
                    myObj.put("w3w_address",w3w_adrress);
                    myObj.put("receiver_phone",receiver_phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                httpConnectionToPhwysl.getHttpConnectionInstance().HttpPostData(myObj,LetterConstants.FIND_LETTER_API);
                break;
            case R.id.find_by_sms:
                new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                        .setTopColorRes(R.color.rosequartz)
                        .setTitle(R.string.text_input_title)
                        .setMessage(R.string.text_input_message)
                        .setIcon(R.drawable.ic_star_border_white_36dp)
                        .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                            @Override
                            public boolean check(String text) {
                                Log.d("HONG", "result"+LetterUtils.isVaildSmsFormat(text));
                                return LetterUtils.isVaildSmsFormat(text);
                            }
                        })
                        .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                            @Override
                            public void onTextInputConfirmed(String text) {
                                Toast.makeText(FindLetterActivity.this, text, Toast.LENGTH_SHORT).show();
                                JSONObject myObj = new JSONObject();
                                String inputText[] = text.split("\n");
                                Log.d(LetterConstants.TAG, "w3w, receiver_phone :  "+inputText[0]+"   "+inputText[1]);
                                try {
                                    myObj.put("receiver_phone",inputText[0]);
                                    myObj.put("w3w_address",inputText[1]);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                httpConnectionToPhwysl.getHttpConnectionInstance().HttpPostData(myObj,LetterConstants.FIND_LETTER_API);
                            }
                        })
                        .show();
            default:
                break;
        }
    }
}
