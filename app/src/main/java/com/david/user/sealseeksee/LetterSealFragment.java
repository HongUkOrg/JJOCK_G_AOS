package com.david.user.sealseeksee;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.david.user.sealseeksee.DateHelper.SublimePickerFragment;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.network.ErrorResult;
import com.kakao.util.helper.log.Logger;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class LetterSealFragment extends Fragment implements View.OnClickListener, LetterUtils.OnBackPressedListener{
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
    private ImageView phoneBook;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1001;


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
        View view = inflater.inflate(R.layout.seal_letter_fragment, null);

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
        phoneBook = (ImageView) view.findViewById(R.id.phone_book);

        JGController.writingNow = true;

        my_W3W = JGController.getInstance().getMy_w3w();
        my_lati = JGController.getInstance().getMy_lati();
        my_long = JGController.getInstance().getMy_long();
        Log.d(TAG, "my_W3W Test : " + my_W3W);

        ButtonOK.setOnClickListener(this);
        CancelButton.setOnClickListener(this);
        timeLockTime.setOnClickListener(this);
        phoneBook.setOnClickListener(this);
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
                if (phone1.getText().toString().length() == 3)     //size as per your requirement
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
                if (phone2.getText().toString().length() == 4)     //size as per your requirement
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
                if (phone3.getText().toString().length() == 4)     //size as per your requirement
                {
                    content.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int) (JGController.getInstance().getWidth() * 0.92),
                (int) (JGController.getInstance().getHeight() * LetterConstants.LETTER_FRAGMENT_HEIGHT_LATIO));
        view.setLayoutParams(params);

        ((LetterMainActivity)getActivity()).setOnBackPressedListener(this);

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

        if (LetterUtils.isEmptyString(get_content)) {
            Toast.makeText(getActivity(), "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            return null;
        } else if (!LetterUtils.isValidPhoneNumber(phone2.getText().toString(), phone3.getText().toString())) {
            Log.d(TAG, "invaild phone number!");
            Toast.makeText(getActivity(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            return null;
        }

        receiver_phone_number = phone1.getText().toString() + "-" + phone2.getText().toString() + "-" + phone3.getText().toString();
        JGController.getInstance().setSavedPhoneNumber(receiver_phone_number);
        if (my_W3W == null || my_W3W.isEmpty()) return null;
        JGController.getInstance().setMy_w3w(my_W3W);
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
        Log.d(TAG, "getSendrPhone: " + mPhoneNumber);
        return mPhoneNumber;
    }

    private void removeFragment(boolean sendOrNot) {
        if(((LetterMainActivity)getActivity()).onBackPressedListener!=null){
            ((LetterMainActivity)getActivity()).onBackPressedListener=null;
        }
        getActivity().
                getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.anim.close_letter_write_anim, R.anim.close_letter_write_anim2).
                remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.changeFragment)).commit();
        ((LetterMainActivity) getActivity()).translateW3Wbar(0);
        ((LetterMainActivity) getActivity()).infoView.setVisibility(View.VISIBLE);
        if (sendOrNot) {
            Handler handler = new Handler();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    ((LetterMainActivity) getActivity()).viewSaveSuccessLetterFragmment();
                }
            };
            handler.postDelayed(r, 500);


        }
        JGController.writingNow = false;
        return;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ButtonOK:
                JSONObject myObj;
                myObj = getSenderInformation();
                if (myObj == null) {
                    ((LetterMainActivity) getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "위치 탐색 중입니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                httpConnectionToPhwysl.getHttpConnectionInstance().HttpPostData(myObj, LetterConstants.LETTER_WRITE_API);
                removeFragment(true);
                break;
            case R.id.ButtonCancel:
                removeFragment(false);
                break;
            case R.id.timeLock_time:
                datePicker();
            case R.id.phone_book:
                Log.d(TAG, "onClick: phoneBook");
                startContactIntent();
//                requestFriends();

                break;
            default:
                break;

        }
    }

    private void startContactIntent() {
        if(checkHasContactPermission()){
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, 1000);
        }
        else{
            Log.d(TAG, "ERROR :: Do not Allowed to access contact!");
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {

        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (1000):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor phone = getActivity().getContentResolver().query(contactData, null, null, null, null);
                    if (phone.moveToFirst()) {
                        String hasPhoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String id = phone.getString(phone.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String contactNumberName = phone.getString(phone.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        if (Integer.parseInt(hasPhoneNumber) > 0) {
                            Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                            phones.moveToFirst();
                            String contactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            if(!LetterUtils.isEmptyString(contactNumber)){
                                String[] phoneNumbers = contactNumber.split("-");
                                if(phoneNumbers.length == 3) {
                                    phone1.setText(phoneNumbers[0]);
                                    phone2.setText(phoneNumbers[1]);
                                    phone3.setText(phoneNumbers[2]);
                                }
                                else if(phoneNumbers.length == 1 && contactNumber.length() == 11){
                                    phone1.setText(contactNumber.substring(0,3));
                                    phone2.setText(contactNumber.substring(3,7));
                                    phone2.setText(contactNumber.substring(7,11));
                                }
                                else{
                                    Log.d(TAG, "ERROR : invalid phone number..!");
                                }
                            }
                            Log.i("HONG", "The phone number is " + contactNumber);


                        } else {
                            Log.d(TAG, "There is no phone number!");
                        }
                    }
                }
                break;
        }
    }

    public boolean checkHasContactPermission(){

        int readContacts = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);
        if(readContacts != PackageManager.PERMISSION_GRANTED) {
            String[] contactPermission = { Manifest.permission.READ_CONTACTS };
            requestPermissions(contactPermission,REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        else{
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Log.d(TAG, "onRequestPermissionsResult: ");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    Log.d(TAG, "onRequestPermissionsResult: Success");
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 1000);
                }
                else {
                    Log.d(TAG, "onRequestPermissionsResult: Fail");
                    Toast.makeText(getActivity(), "Please Allow contact Permission To Continue..", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    @Override
    public void doBack() {
        removeFragment(false);
    }

    private abstract class KakaoTalkResponseCallback<T> extends TalkResponseCallback<T> {
        @Override
        public void onNotKakaoTalkUser() {
            Log.d("HONG", "onNotKakaoTalkUser: not kakao talk user");
        }

        @Override
        public void onFailure(ErrorResult errorResult) {
            Log.d(TAG, "onFailure: " + errorResult.toString());
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
//            redirectLoginActivity();
        }

        @Override
        public void onNotSignedUp() {
//            redirectSignupActivity();
        }
    }

    public void requestProfile() {
        Log.d(TAG, "requestProfile is called!");
        KakaoTalkService.getInstance().requestProfile(new KakaoTalkResponseCallback<KakaoTalkProfile>() {
            @Override
            public void onNotKakaoTalkUser() {
                Log.d(TAG, "onNotKakaoTalkUser: ");
                super.onNotKakaoTalkUser();
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.d(TAG, "onFailure: " + errorResult.toString());
                super.onFailure(errorResult);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d(TAG, "onSessionClosed: " + errorResult.toString());
                super.onSessionClosed(errorResult);
            }

            @Override
            public void onNotSignedUp() {
                Log.d(TAG, "onNotSignedUp: ");
                super.onNotSignedUp();
            }

            @Override
            public void onSuccess(KakaoTalkProfile talkProfile) {

                final String nickName = talkProfile.getNickName();
                final String profileImageURL = talkProfile.getProfileImageUrl();
                final String thumbnailURL = talkProfile.getThumbnailUrl();
                final String countryISO = talkProfile.getCountryISO();
                Log.d(TAG, "onSuccess: " + nickName);
                Log.d(TAG, "onSuccess: " + profileImageURL);
                Log.d(TAG, "onSuccess: " + thumbnailURL);
                Log.d(TAG, "onSuccess: " + countryISO);
            }
        });
    }

    private void requestAccessTokenInfo() {
        AuthService.getInstance().requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d(TAG, "onSessionClosed: " + errorResult.toString());
//                redirectLoginActivity(self);
            }

            @Override
            public void onNotSignedUp() {
                // not happened
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e("failed to get access token info. msg=" + errorResult);
            }

            @Override
            public void onSuccess(AccessTokenInfoResponse accessTokenInfoResponse) {
                long userId = accessTokenInfoResponse.getUserId();
                Logger.d("this access token is for userId=" + userId);

                long expiresInMilis = accessTokenInfoResponse.getExpiresInMillis();
                Logger.d("this access token expires after " + expiresInMilis + " milliseconds.");
            }
        });
    }

    public void requestFriends() {

        // offset = 0, limit = 100
        AppFriendContext friendContext = new AppFriendContext(true, 0, 100, "asc");

        KakaoTalkService.getInstance().requestAppFriends(friendContext,
                new TalkResponseCallback<AppFriendsResponse>() {
                    @Override
                    public void onNotKakaoTalkUser() {
                        Log.d(TAG, "onNotKakaoTalkUser: ");
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.d(TAG, "onSessionClosed: "+errorResult.toString());
//                        redirectLoginActivity();
                    }

                    @Override
                    public void onNotSignedUp() {
                        Log.d(TAG, "onNotSignedUp: ");
//                        redirectSignupActivity();
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.d(TAG, "onFailure: "+ errorResult.toString());
                    }

                    @Override
                    public void onSuccess(AppFriendsResponse result) {
                        // 친구 목록
                        Log.d(TAG, " friends list : "+result.getFriends().toString());
                        Log.d(TAG, " getResultId : "+result.getResultId());
                        Log.d(TAG, " getAfterUrl : "+result.getAfterUrl());
                        Log.d(TAG, " getBeforeUrl : "+result.getBeforeUrl());
                        Log.d(TAG, " getTotalCount : "+result.getTotalCount());
                        content.setText(result.getFriends().toString());
                        // context의 beforeUrl과 afterUrl이 업데이트 된 상태.
                    }
                });
    }

}
