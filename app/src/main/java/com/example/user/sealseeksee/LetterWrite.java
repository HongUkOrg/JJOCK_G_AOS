package com.example.user.sealseeksee;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.example.user.sealseeksee.DateHelper.SublimePickerFragment;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;


public class LetterWrite extends AppCompatActivity implements View.OnClickListener // Fragment로 바꿔야됨
{
    public static final String TAG = "HONG";
    private static boolean timeLockBool = false;

    private EditText title, content, phone1, phone2, phone3;
    private Button ButtonOK, CancelButton;
    private ShineButton timeLockButton;
    private TextView letterWrite_w3w, txt_TimeLock, timeLockTime;
    private ImageView image;
    private static String w3w;
    private double my_lati, my_long;
    private long myTimeLockTime = -1;

    private String receiver_phone_number, get_title, get_content;

    SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {
//            rlDateTimeRecurrenceInfo.setVisibility(View.GONE);
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {

            setTimeLockTime(selectedDate, hourOfDay, minute);

        }
    };

    private void setTimeLockTime(SelectedDate selectedDate, int hourOfDay, int minute) {
        selectedDate.getStartDate().set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedDate.getStartDate().set(Calendar.MINUTE, minute);
        Calendar myCalender = selectedDate.getStartDate();
        Date myTime = myCalender.getTime();
        myTimeLockTime = myTime.getTime();
        timeLockTime.setText(myCalender.get(Calendar.YEAR) + "/" + myCalender.get(Calendar.MONTH) + 1 + "/" + myCalender.get(Calendar.DAY_OF_MONTH) + "\n"
                + myCalender.get(Calendar.HOUR_OF_DAY) + "시" + myCalender.get(Calendar.MINUTE) + "분");
        Log.d(TAG, "setTimeLockTime: " + myTime.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_write);

        letterWrite_w3w = (TextView) findViewById(R.id.letterWrite_w3w);
        txt_TimeLock = (TextView) findViewById(R.id.txt_timeLock);
        timeLockTime = (TextView) findViewById(R.id.timeLock_time);
        title = (EditText) findViewById(R.id.dialog_title);
        content = (EditText) findViewById(R.id.content);
        phone1 = (EditText) findViewById(R.id.phone1);
        phone2 = (EditText) findViewById(R.id.phone2);
        phone3 = (EditText) findViewById(R.id.phone3);
        ButtonOK = (Button) findViewById(R.id.ButtonOK);
        CancelButton = (Button) findViewById(R.id.ButtonCancel);
        timeLockButton = (ShineButton) findViewById(R.id.po_image2);
        image = (ImageView) findViewById(R.id.image);

        Intent intentGetFromSendLetter = getIntent();
        image.setImageResource(R.drawable.letter_image);
        w3w = intentGetFromSendLetter.getStringExtra("w3w");
        my_lati = intentGetFromSendLetter.getDoubleExtra("my_lati", -1);
        my_long = intentGetFromSendLetter.getDoubleExtra("my_long", -1);

        Log.d(TAG, "w3w Test : " + w3w);


        if (w3w != null) letterWrite_w3w.setText("현재 위치의 W3W : " + w3w);
        ButtonOK.setOnClickListener(LetterWrite.this);
        CancelButton.setOnClickListener(LetterWrite.this);
        timeLockTime.setOnClickListener(LetterWrite.this);
        timeLockButton.init(LetterWrite.this);
        timeLockButton.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                Log.d(TAG, "onClick: po_image2");
                if (checked) {
                    txt_TimeLock.setText("봉인");
                    timeLockBool = true;
                    timeLockTime.setVisibility(View.VISIBLE);
                } else {
                    txt_TimeLock.setText("봉인하지 않음");
                    timeLockBool = false;
                    timeLockTime.setVisibility(View.INVISIBLE);
                }

            }
        });


    }

    public void sendSmsIntent(String number, String word) {
        try {
            Uri smsUri = Uri.parse("sms:" + number);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
            sendIntent.putExtra("sms_body", number + "\n" + word);
            startActivity(sendIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void send_success_dialog(final String receiver_phone_number, final String w3w_words) {

        final Dialog dialog2 = new Dialog(this);
        dialog2.setContentView(R.layout.dialog2);
        dialog2.setTitle("Title...");
        Button dialogButton = (Button) dialog2.findViewById(R.id.success_dialogButtonOK);
        Button dialogCanelButton = (Button) dialog2.findViewById(R.id.success_dialogButtonCancel);

        TextView w3w = (TextView) dialog2.findViewById(R.id.success_w3w);
        w3w.setText(w3w_words);

        TextView success_receiver_phone_number = (TextView) dialog2.findViewById(R.id.success_phone);
        success_receiver_phone_number.setText(receiver_phone_number);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSmsIntent(receiver_phone_number, w3w_words);
                dialog2.dismiss();
            }
        });
        dialogCanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ButtonOK:
                JSONObject myObj = new JSONObject();
                myObj = getSenderInformation();
                if(myObj==null) return;
                httpConnectionToPhwysl.getHttpConnectionInstance().HttpPostData(myObj, LetterConstants.LETTER_WRITE_API);
                send_success_dialog(receiver_phone_number, w3w);
                break;
            case R.id.ButtonCancel:
                startActivity(new Intent(this, SendLetterActivity.class));
                break;
            case R.id.timeLock_time:
                datePicker();
            default:
                break;

        }

    }

    private JSONObject getSenderInformation() {
        get_title = title.getText().toString();
        get_content = content.getText().toString();
        if(!LetterUtils.isEmptyString(get_title))
        {
            Toast.makeText(this,"제목을 입력하세요.",Toast.LENGTH_SHORT).show();
            return null;
        }
        else if(!LetterUtils.isEmptyString(get_content))
        {
            Toast.makeText(this,"내용을 입력하세요.",Toast.LENGTH_SHORT).show();
            return null;
        }
        else if(!LetterUtils.isValidPhoneNumber(phone2.getText().toString(),phone3.getText().toString()))
        {
            Log.d(TAG, "invaild phone number!");
            Toast.makeText(this,"Invalid Phone Number",Toast.LENGTH_SHORT).show();
            return null;
        }

        receiver_phone_number = phone1.getText().toString() + "-" + phone2.getText().toString() + "-" + phone3.getText().toString();
        JSONObject myObj = new JSONObject();
        try {
            myObj.put("receiver_phone", receiver_phone_number)
                    .put("message", get_content)
                    .put("title", get_title)
                    .put("w3w_address", w3w)
                    .put("sender_phone", getSenderPhone())
                    .put("latitude", Double.toString(my_lati))
                    .put("longitude", Double.toString(my_long));
            if (timeLockBool) myObj.put("time_lock", myTimeLockTime);
            else myObj.put("time_lock", -1); // -1 means that user does not set time_lock setting.
            Log.d("HONG", "myTimeLock Save : " + myTimeLockTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return myObj;
    }

    private void datePicker() {
        SublimePickerFragment pickerFrag = new SublimePickerFragment();
        pickerFrag.setCallback(mFragmentCallback);

        // Options
        Pair<Boolean, SublimeOptions> optionsPair = getOptions();

        if (!optionsPair.first) { // If options are not valid
            Toast.makeText(this, "No pickers activated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Valid options
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
        pickerFrag.setArguments(bundle);

        pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");

    }

    Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;
        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
        displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER;
        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);

        options.setDisplayOptions(displayOptions);

        // Enable/disable the date range selection feature
        // Example for setting date range:
        // Note that you can pass a date range as the initial date params
        // even if you have date-range selection disabled. In this case,
        // the user WILL be able to change date-range using the header
        // TextViews, but not using long-press.

        /*Calendar startCal = Calendar.getInstance();
        startCal.set(2016, 2, 4);
        Calendar endCal = Calendar.getInstance();
        endCal.set(2016, 2, 17);
        options.setDateParams(startCal, endCal);*/

        // If 'displayOptions' is zero, the chosen options are not valid
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed Called");
        startActivity(new Intent(LetterWrite.this, MainActivity.class));
    }

    public String getSenderPhone() {
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String mPhoneNumber = tMgr.getLine1Number();
        Log.d(TAG, "getSendrPhone: "+mPhoneNumber);
        return mPhoneNumber;
    }
}
