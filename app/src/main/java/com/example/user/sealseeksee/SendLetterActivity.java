package com.example.user.sealseeksee;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SendLetterActivity extends AppCompatActivity implements OnMapReadyCallback
{

    private  HttpURLConnection http;
    private static double my_lati;
    private static double my_long;

    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLcationProviderClient;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_letter);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }



    @Override
    public void onMapReady(final GoogleMap map) {




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

        Button get_my_position=(Button)findViewById(R.id.send_letter_msg);
        Button refresh=(Button)findViewById(R.id.refresh);

        get_my_position.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                getCurrentLocation(map);

            }
        });

        refresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                map.clear();

            }
        });

//        Location my_first_loc = map.getMyLocation();
//        LatLng ME  = new LatLng(my_first_loc.getLatitude(),my_first_loc.getLongitude());
        LatLng SEOUL = new LatLng(37.56, 126.97);


        map.addMarker(new MarkerOptions()
                .position(SEOUL)
                .title("Seoul")

                .icon(BitmapDescriptorFactory.fromResource(R.drawable.city)));


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL,16));


        getCurrentLocation(map);

    }

    void getCurrentLocation(GoogleMap mMap) {


        Location myLocation  = mMap.getMyLocation();
        if(myLocation!=null)
        {
            double dLatitude = myLocation.getLatitude();
            double dLongitude = myLocation.getLongitude();
            Log.i("APPLICATION"," : "+dLatitude);
            Log.i("APPLICATION"," : "+dLongitude);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(dLatitude, dLongitude))
                    .title("Seoul")

            .icon(BitmapDescriptorFactory.fromResource(R.drawable.person_walking)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 16));

            location_processing(dLatitude,dLongitude);





        }
        else
        {
            Toast.makeText(this, "Unable to fetch the current location", Toast.LENGTH_SHORT).show();
        }

    }

    public void location_processing(double latitude,double longitude)
    {
        String result ="nothing";

        BigDecimal bd = new BigDecimal(latitude);
        bd = bd.round(new MathContext(6));
        double rounded_lati = bd.doubleValue();

        BigDecimal bd2 = new BigDecimal(longitude);
        bd = bd.round(new MathContext(6));
        double rounded_long = bd2.doubleValue();

        my_lati=rounded_lati;
        my_long=rounded_long;

        result = "";

        result += Double.toString(rounded_lati) + "," + Double.toString(rounded_long);

        Log.d("my_loc", "get_my_long_lat: "+result);

        transaction(result);


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


        RequestHandle requestHandle = client.get(link, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject responseBody) {


                try {
                    response_parse(responseBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        });
    }

    private void response_parse(JSONObject response) throws JSONException {

        TextView response_msg = (TextView) findViewById(R.id.trans_words);
        TextView text1 = (TextView) findViewById(R.id.text1);


        String words = response.getString("words").toString();
        String first = "";
        String second = "";
        String third = "";


        String[] word = words.split("\\.");


        first = word[0];
        second = word[1];
        third = word[2];


        response_msg.setText("first : " + first + "\n" + "second : " + second + "\n" + "third : " + third);

        text1.setText("변환된 WHAT3WORDS 주소는 : ");



        String w3w_words = word[0]+","+word[1]+","+word[2];



        make_json_object(w3w_words);

//        sendSmsIntent("010-2043-9851",word);




    }

    public void make_json_object(final String w3w_words) throws JSONException {

        final JSONObject myObj = new JSONObject();

        final String[] receiver_phone = {""};

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        final EditText text = (EditText) dialog.findViewById(R.id.text);
        final EditText content = (EditText) dialog.findViewById(R.id.content);
        final EditText phone1 = (EditText) dialog.findViewById(R.id.phone1);
        final EditText phone2 = (EditText) dialog.findViewById(R.id.phone2);
        final EditText phone3 = (EditText) dialog.findViewById(R.id.phone3);

        text.setText("Title");
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.letter_image);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        Button dialogCanelButton = (Button)dialog.findViewById(R.id.dialogButtonCancel);


        dialogButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                String get_title = "";
                String get_content = "";

                get_content=content.getText().toString();
                get_title=text.getText().toString();

                String receiver_phone_number="";
                receiver_phone_number+=phone1.getText().toString()+"-"+phone2.getText().toString()
                        +"-"+phone3.getText().toString();

                try {
                    myObj.put("receiver_phone",receiver_phone_number);
                    myObj.put("message", get_content);
                    myObj.put("title", get_title);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HttpPostData(myObj);

                dialog.dismiss();

                send_succes_dialog(receiver_phone_number,w3w_words);
            }
        });

        dialogCanelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                dialog.dismiss();



            }
        });

        dialog.show();

        myObj.put("w3w_address",w3w_words);
        myObj.put("sender_phone","010-2043-8751");
        myObj.put("latitude",Double.toString(my_lati));
        myObj.put("longitude",Double.toString(my_long));

        Log.d("lati", "make_json_object: my lati is" +Double.toString(my_lati));







    }

    public void sendSmsIntent(String number,String word){
        try{
            Uri smsUri = Uri.parse("sms:"+number);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
            sendIntent.putExtra("sms_body", number+"\n"+word);
            startActivity(sendIntent);

//        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//        sendIntent.putExtra("address", number);
//        sendIntent.putExtra("sms_body", editBody.getText().toString());
//        sendIntent.setType("vnd.android-dir/mms-sms");
//        startActivity(sendIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void HttpPostData(JSONObject myObj) {
        try {


            URL url = new URL("http://phwysl.dothome.co.kr/login.php");
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
            myObj.put("created_date", current);





            String myJson = myObj.toString();





            StringBuffer buffer = new StringBuffer();
            buffer.append("json=");
            buffer.append(myJson);
//
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();



            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;



            if(http.getResponseCode()==200)
            {

                Toast.makeText(this,"정상적으로 전송 됐습니다",Toast.LENGTH_SHORT).show();

            }



            while ((str = reader.readLine()) != null)
            {
                // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
            }
            String myResult = builder.toString();
            // 전송결과를 전역 변수에 저장

//            ((TextView)(findViewById(R.id.requestTextView))).setText(myResult);



        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // try
        catch (JSONException e) {
            e.printStackTrace();
        }


        http.disconnect();

    }



    void send_succes_dialog(final String receiver_phone_number, final String w3w_words)
    {


        final Dialog dialog2 = new Dialog(this);
        dialog2.setContentView(R.layout.dialog2);
        dialog2.setTitle("Title...");





        Button dialogButton = (Button) dialog2.findViewById(R.id.success_dialogButtonOK);
        // if button is clicked, close the custom dialog
        Button dialogCanelButton = (Button)dialog2.findViewById(R.id.success_dialogButtonCancel);

        TextView w3w = (TextView)dialog2.findViewById(R.id.success_w3w);
        w3w.setText(w3w_words);

        TextView success_receiver_phone_number = (TextView)dialog2.findViewById(R.id.success_phone);

        success_receiver_phone_number.setText(receiver_phone_number);

        dialogButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                sendSmsIntent(receiver_phone_number,w3w_words);

                dialog2.dismiss();


            }
        });

        dialogCanelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                dialog2.dismiss();



            }
        });

        dialog2.show();





    }






}
