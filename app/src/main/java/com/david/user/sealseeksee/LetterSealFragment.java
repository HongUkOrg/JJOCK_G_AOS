package com.david.user.sealseeksee;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.david.user.sealseeksee.DateHelper.SublimePickerFragment;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class LetterSealFragment extends Fragment implements View.OnClickListener{
    public static final String TAG = "HONG";
    private static boolean timeLockBool = false;

    private EditText title, content, phone1, phone2, phone3;
    private Button ButtonOK, CancelButton;
    private ShineButton timeLockButton;
    private TextView letterWrite_w3w, txt_TimeLock, timeLockTime;
    private static String my_W3W;
    private double my_lati, my_long;
    private long myTimeLockTime = -1;
    private Context mContext;

    private String receiver_phone_number, get_title, get_content;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("HONG", "frag onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("HONG", "frag onCreateView: ");
        View view = inflater.inflate(R.layout.seal_letter_fragment,null);

        mContext = getActivity();

        txt_TimeLock = (TextView) view.findViewById(R.id.txt_timeLock);
        timeLockTime = (TextView) view.findViewById(R.id.timeLock_time);
        content = (LinedEditText) view.findViewById(R.id.content);
        phone1 = (EditText) view.findViewById(R.id.phone1);
        phone2 = (EditText) view.findViewById(R.id.phone2);
        phone3 = (EditText) view.findViewById(R.id.phone3);
        ButtonOK = (Button) view.findViewById(R.id.ButtonOK);
        CancelButton = (Button) view.findViewById(R.id.ButtonCancel);
        timeLockButton = (ShineButton) view.findViewById(R.id.po_image2);


        HongController.writingNow = true;

        my_W3W = HongController.getInstance().getMy_w3w();
        my_lati = HongController.getInstance().getMy_lati();
        my_long = HongController.getInstance().getMy_long();
        Log.d(TAG, "my_W3W Test : " + my_W3W);

        ButtonOK.setOnClickListener(this);
        CancelButton.setOnClickListener(this);
        timeLockTime.setOnClickListener(this);
        timeLockButton.init(getActivity());
        timeLockButton.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                Log.d(TAG, "onClick: po_image2");
                if (checked) {
                    txt_TimeLock.setText("LOCK");
                    timeLockBool = true;
                    timeLockButton.setShapeResource(R.drawable.ic_lock);
                    timeLockTime.setVisibility(View.VISIBLE);
                } else {
                    txt_TimeLock.setText("UNLOCK");
                    timeLockButton.setShapeResource(R.drawable.ic_unlock);
                    timeLockBool = false;
                    timeLockTime.setVisibility(View.INVISIBLE);

                }

            }
        });

        phone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(phone1.getText().toString().length()==3)     //size as per your requirement
                {
                    phone2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        phone2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(phone2.getText().toString().length()==4)     //size as per your requirement
                {
                    phone3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        phone3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(phone3.getText().toString().length()==4)     //size as per your requirement
                {
                    content.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int)(HongController.getInstance().getWidth()*0.92),
                (int)(HongController.getInstance().getHeight()*LetterConstants.LETTER_FRAGMENT_HEIGHT_LATIO));
        view.setLayoutParams(params);

        return view;
    }

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


    private JSONObject getSenderInformation() {
        get_content = content.getText().toString();

        if(!LetterUtils.isEmptyString(get_content))
        {
            Toast.makeText(getActivity(),"내용을 입력하세요.",Toast.LENGTH_SHORT).show();
            return null;
        }
        else if(!LetterUtils.isValidPhoneNumber(phone2.getText().toString(),phone3.getText().toString()))
        {
            Log.d(TAG, "invaild phone number!");
            Toast.makeText(getActivity(),"Invalid Phone Number",Toast.LENGTH_SHORT).show();
            return null;
        }

        receiver_phone_number = phone1.getText().toString() + "-" + phone2.getText().toString() + "-" + phone3.getText().toString();
        HongController.getInstance().setSavedPhoneNumber(receiver_phone_number);
        if(my_W3W == null || my_W3W.isEmpty()) return null;
        HongController.getInstance().setMy_w3w(my_W3W);
        JSONObject myObj = new JSONObject();
        try {

            myObj.put("receiver_phone", receiver_phone_number)
                    .put("message", get_content)
                    .put("title", "My Secret Letter")
                    .put("w3w_address", my_W3W)
                    .put("sender_phone", getSenderPhone())
                    .put("latitude", Double.toString(my_lati))
                    .put("longitude", Double.toString(my_long));
            if (timeLockBool) myObj.put("time_lock", myTimeLockTime);
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
            Toast.makeText(getActivity(), "No pickers activated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Valid options
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
        pickerFrag.setArguments(bundle);

        pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        pickerFrag.show(getActivity().getSupportFragmentManager(), "SUBLIME_PICKER");

//        DialogFragment newFragment = new myDatePicker();
//        newFragment.show(getSupportFragmentManager(), "date picker");

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


    public String getSenderPhone() {
        TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String mPhoneNumber = tMgr.getLine1Number();
        Log.d(TAG, "getSendrPhone: "+mPhoneNumber);
        return mPhoneNumber;
    }
    private void removeFragment(boolean sendOrNot)
    {
        getActivity().
                getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.anim.close_letter_write_anim,R.anim.close_letter_write_anim2).
                remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.changeFragment)).commit();
        ((LetterMainActivity)getActivity()).translateW3Wbar(0);
        ((LetterMainActivity)getActivity()).infoView.setVisibility(View.VISIBLE);
        if(sendOrNot){
            Handler handler = new Handler();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    ((LetterMainActivity)getActivity()).viewSaveSuccessLetterFragmment();
                }
            };
            handler.postDelayed(r,500);



        }
        HongController.writingNow = false;
        return;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ButtonOK:
                JSONObject myObj;
                myObj = getSenderInformation();
                if(myObj==null) {
                    ((LetterMainActivity)getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"위치 탐색 중입니다.",Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                httpConnectionToPhwysl.getHttpConnectionInstance().HttpPostData(myObj, LetterConstants.LETTER_WRITE_API);
                removeFragment(true);
                break;
            case R.id.ButtonCancel:
//                startActivity(new Intent(getActivity(), LetterMainActivity.class));
                removeFragment(false);
                break;
            case R.id.timeLock_time:
                datePicker();
            default:
                break;

        }

    }



}
