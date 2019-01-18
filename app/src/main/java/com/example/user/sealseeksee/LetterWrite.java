package com.example.user.sealseeksee;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONException;
import org.json.JSONObject;


public class LetterWrite extends AppCompatActivity implements View.OnClickListener // Fragment로 바꿔야됨
{
    public static final String TAG ="HONG";
    private static boolean timeLockBool = false;

    private EditText title,content,phone1,phone2,phone3;
    private Button ButtonOK,CancelButton;
    private ShineButton timeLockButton;
    private TextView letterWrite_w3w,txt_TimeLock;
    private ImageView image;
    private static String w3w;
    private double my_lati,my_long;

    private String receiver_phone_number,get_title,get_content;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_write);

        letterWrite_w3w = (TextView)findViewById(R.id.letterWrite_w3w);
        txt_TimeLock =(TextView)findViewById(R.id.txt_timeLock);
        title = (EditText) findViewById(R.id.dialog_title);
        content = (EditText)findViewById(R.id.content);
        phone1 = (EditText) findViewById(R.id.phone1);
        phone2 = (EditText) findViewById(R.id.phone2);
        phone3 = (EditText) findViewById(R.id.phone3);
        ButtonOK = (Button) findViewById(R.id.ButtonOK);
        CancelButton = (Button)findViewById(R.id.ButtonCancel);
        timeLockButton = (ShineButton)findViewById(R.id.po_image2);
        image= (ImageView)findViewById(R.id.image);

                Intent intentGetFromSendLetter = getIntent();
        image.setImageResource(R.drawable.letter_image);
        w3w = intentGetFromSendLetter.getStringExtra("w3w");
        my_lati = intentGetFromSendLetter.getDoubleExtra("my_lati",-1);
        my_long = intentGetFromSendLetter.getDoubleExtra("my_long",-1);

        Log.d(TAG, "w3w Test : "+w3w);



        if(w3w != null) letterWrite_w3w.setText("현재 위치의 W3W : "+w3w);
        ButtonOK.setOnClickListener(LetterWrite.this);
        CancelButton.setOnClickListener(LetterWrite.this);
        timeLockButton.init(LetterWrite.this);
        timeLockButton.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                Log.d(TAG, "onClick: po_image2");
                if(checked){
                    txt_TimeLock.setText("봉인");
                    timeLockBool = true;
                }
                else{
                    txt_TimeLock.setText("봉인하지 않음");
                    timeLockBool = false;
                }

            }
        });



    }

    public void sendSmsIntent(String number,String word){
        try{
            Uri smsUri = Uri.parse("sms:"+number);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
            sendIntent.putExtra("sms_body", number+"\n"+word);
            startActivity(sendIntent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void send_success_dialog(final String receiver_phone_number, final String w3w_words)
    {

        final Dialog dialog2 = new Dialog(this);
        dialog2.setContentView(R.layout.dialog2);
        dialog2.setTitle("Title...");
        Button dialogButton = (Button) dialog2.findViewById(R.id.success_dialogButtonOK);
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

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ButtonOK :
                JSONObject myObj = new JSONObject();
                myObj = getSenderInformation();

                httpConnectionToPhwysl.getHttpConnectionInstance().HttpPostData(myObj,LetterConstants.LETTER_WRITE_API);
                send_success_dialog(receiver_phone_number,w3w);
                break;
            case R.id.ButtonCancel:
                startActivity(new Intent(this,SendLetterActivity.class));
                break;
                default:
                    break;

        }

    }

    private JSONObject getSenderInformation()
    {
        get_title = title.getText().toString();
        get_content = content.getText().toString();
        receiver_phone_number = phone1.getText().toString()+"-"+phone2.getText().toString() +"-"+phone3.getText().toString();
        JSONObject myObj = new JSONObject();
        try {
            myObj.put("receiver_phone",receiver_phone_number)
                    .put("message", get_content)
                    .put("title", get_title)
                    .put("w3w_address",w3w)
                    .put("sender_phone","010-2043-8751")
                    .put("latitude",Double.toString(my_lati))
                    .put("longitude",Double.toString(my_long))
                    .put("time_lock","20190115");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return myObj;
    }

    private void datePicker()
    {
        SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
            @Override
            public void onCancelled() {
                rlDateTimeRecurrenceInfo.setVisibility(View.GONE);
            }

            @Override
            public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                                int hourOfDay, int minute,
                                                SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                                String recurrenceRule) {

                mSelectedDate = selectedDate;
                mHour = hourOfDay;
                mMinute = minute;
                mRecurrenceOption = recurrenceOption != null ?
                        recurrenceOption.name() : "n/a";
                mRecurrenceRule = recurrenceRule != null ?
                        recurrenceRule : "n/a";

                updateInfoView();

                svMainContainer.post(new Runnable() {
                    @Override
                    public void run() {
                        svMainContainer.scrollTo(svMainContainer.getScrollX(),
                                cbAllowDateRangeSelection.getBottom());
                    }
                });
            }
        };

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed Called");
        startActivity(new Intent(LetterWrite.this,MainActivity.class));
    }
}
