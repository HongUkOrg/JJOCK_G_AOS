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

public class FindLetterActivity extends AppCompatActivity
{

    private HttpURLConnection http;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_letter);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        final Button find_btn = (Button)findViewById(R.id.letter_find_btn2);

        find_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                JSONObject myObj = new JSONObject();

                String w3w_adrress = "";
                String receiver_phone = "";





                EditText phone1 = (EditText)findViewById(R.id.phone1);
                EditText phone2 = (EditText)findViewById(R.id.phone2);
                EditText phone3 = (EditText)findViewById(R.id.phone3);
                EditText word1 = (EditText)findViewById(R.id.first_word);
                EditText word2 = (EditText)findViewById(R.id.second_word);
                EditText word3 = (EditText)findViewById(R.id.third_word);

                w3w_adrress+=word1.getText().toString()+","+word2.getText().toString()+","+word3.getText().toString();

                receiver_phone+=phone1.getText().toString()+"-"+phone2.getText().toString()+"-"+phone3.getText().toString();



                try {
                    myObj.put("w3w_address",w3w_adrress);
                    myObj.put("receiver_phone",receiver_phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject myJson = null;

                String serarch_letter = HttpPostData(myObj);
                if(serarch_letter==null)
                {
                    Toast.makeText(FindLetterActivity.this, "편지를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();

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


                String title = "";
                String message = "";
                String latitude = "";
                String longitude = "";

                try {
                    if(myJson!=null)
                    {
                        title = myJson.getString("title");
                        message = myJson.getString("message");
                        latitude = myJson.getString("latitude");
                        longitude = myJson.getString("longitude");



                        Intent intent = new Intent(FindLetterActivity.this,TrackingLetter.class);

                        intent.putExtra("phone1", phone1.getText().toString());
                        intent.putExtra("phone2", phone2.getText().toString());
                        intent.putExtra("phone3", phone3.getText().toString());
                        intent.putExtra("word1", word1.getText().toString());
                        intent.putExtra("word2", word2.getText().toString());
                        intent.putExtra("word3", word3.getText().toString());

                        intent.putExtra("title",title);
                        intent.putExtra("message",message);
                        intent.putExtra("latitude",latitude);
                        intent.putExtra("longitude",longitude);

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
        });
    }




    public String HttpPostData(JSONObject myObj)
    {
        String result = "";

        try {


            URL url = new URL("http://phwysl.dothome.co.kr/find_letter.php");
            // URL 설정
            http = (HttpURLConnection) url.openConnection();

            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");


            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");


            ContentValues contentValues = new ContentValues();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");



            String current = dateFormat.format(new Date());
            myObj.put("opened_date", current);





            String myJson = myObj.toString();





            StringBuffer buffer = new StringBuffer();
            buffer.append("json=");
            buffer.append(myJson);
//
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "EUC-KR");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();



            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "EUC-KR");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;



            if(http.getResponseCode()==200)
            {

                Toast.makeText(this,"편지를 검색했습니다.",Toast.LENGTH_SHORT).show();

            }



            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
            }
            result = builder.toString();

            Log.d("received by server ", "HttpPostData: "+result);

            JSONObject json_result = new JSONObject(result);


            if(json_result.getString("title").equals("find_fail"))
            {
                http.disconnect();
                Log.d("return", "HttpPostData: i will return null");
                return null;
            }


            // 전송결과를 전역 변수에 저장

//            ((TextView)(findViewById(R.id.requestTextView))).setText(myResult);


//            Toast.makeText(FindLetterActivity.this, "전송 후 결과 받음", Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // try
        catch (JSONException e) {
            e.printStackTrace();
        }


        http.disconnect();

        return result;

    }





}
