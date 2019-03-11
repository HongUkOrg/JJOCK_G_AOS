package com.example.user.sealseeksee;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.user.sealseeksee.LetterAdapter.LetterAdapter;
import com.example.user.sealseeksee.LetterAdapter.LetterOption;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class LetterSeekFragment extends Fragment implements View.OnClickListener, HongController.LetterListener {

    private HttpURLConnection http;
    private EditText phone1, phone2, phone3, word1, word2, word3;
    private EditText fullPhoneNumber;
    private Button find_btn, sms_find_btn;
    private int letterNumber;
    private static Context mContext;
    private String title, message, latitude, longitude;
    private JSONObject myJson = null, myLetter = null;
    private JSONArray myJsonArr = null;
    private String serarch_letter, myTimeLocktime;
    private long myTimeLockTimeLongType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("HONG", "frag onCreateView: ");
        View view = inflater.inflate(R.layout.seek_letter_fragment, null);

        mContext = getActivity();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        find_btn = (Button) view.findViewById(R.id.find_Letter_search_btn);
        sms_find_btn = (Button) view.findViewById(R.id.find_by_sms);
        phone1 = (EditText) view.findViewById(R.id.find_phone1);
        phone2 = (EditText) view.findViewById(R.id.find_phone2);
        phone3 = (EditText) view.findViewById(R.id.find_phone3);
        word1 = (EditText) view.findViewById(R.id.first_word);
        word2 = (EditText) view.findViewById(R.id.second_word);
        word3 = (EditText) view.findViewById(R.id.third_word);


        find_btn.setOnClickListener(this);
        sms_find_btn.setOnClickListener(this);
        HongController.getInstance().setLetterListener(this);


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

        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(
                (int) (HongController.getInstance().getWidth() * 0.92), (int) (HongController.getInstance().getHeight() * 0.5));
        layout.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        view.setLayoutParams(layout);

        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    removeFragment();
                    return true;
                }
                return false;
            }
        } );

        view.setOnTouchListener(new LetterTouchListener(getActivity()) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
            }

            public void onSwipeLeft() {
            }

            public void onSwipeBottom() {
                removeFragment();

            }

        });

        return view;
    }

    private void removeFragment() {
        getActivity().
                getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.anim.close_letter_seek_anim, R.anim.close_letter_seek_anim2).
                remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.changeFragment)).commit();
//                ((LetterMainActivity) getActivity()).translateW3Wbar(0);
//                ((LetterMainActivity) getActivity()).infoView.setVisibility(View.VISIBLE);
//                if (false) {
//                    Handler handler = new Handler();
//                    Runnable r = new Runnable() {
//                        @Override
//                        public void run() {
//                            ((LetterMainActivity) getActivity()).viewSaveSuccessLetterFragmment();
//                        }
//                    };
//                    handler.postDelayed(r, 500);
//
//
//                }
        return;
    }

    @Override
    public void onReceiveLetter(String resultJson) {
        Log.d(LetterConstants.TAG, "onReceiveLetter Callback Method is Called : " + resultJson);
        serarch_letter = resultJson;

        if (serarch_letter == null) {
            Log.d("HONG", ":cannot connect backend server.. ");
            return;
        } else {
            try {
                myJson = new JSONObject(serarch_letter);
                if (myJson != null && myJson.has("letter")) {
                    myJsonArr = (JSONArray) myJson.get("letter");
                    letterNumber = myJsonArr.length();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            if (letterNumber == 1) {
                myLetter = (JSONObject) myJsonArr.get(0);
                showMyLetter(myLetter);
            } else {
                showLetterSelectionDialog(myJsonArr);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showMyLetter(JSONObject myLetter) throws JSONException {
        if (myLetter != null) {

            title = myLetter.getString("title");
            if (title.equals("find_fail")) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "편지를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            message = myLetter.getString("message");
            latitude = myLetter.getString("latitude");
            longitude = myLetter.getString("longitude");
            myTimeLocktime = myLetter.getString("time_lock");

            if (myTimeLocktime.getClass().equals(Long.class)) {
                myTimeLockTimeLongType = Long.parseLong(myTimeLocktime);
            } else {
                myTimeLockTimeLongType = -1;
            }

            Intent intent = new Intent(getActivity(), TrackingLetter.class);

            intent.putExtra("phone1", phone1.getText().toString())
                    .putExtra("phone2", phone2.getText().toString())
                    .putExtra("phone3", phone3.getText().toString())
                    .putExtra("word1", word1.getText().toString())
                    .putExtra("word2", word2.getText().toString())
                    .putExtra("word3", word3.getText().toString())
                    .putExtra("title", title)
                    .putExtra("message", message)
                    .putExtra("latitude", latitude)
                    .putExtra("longitude", longitude);
            if (myTimeLockTimeLongType != -1) {
                Log.d("HONG", "TimeLock is Set! send to TrackingLetter Activity including date of timeLock");
                intent.putExtra("time_lock", myTimeLockTimeLongType);
            }
            startActivity(intent);
        } else {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity(), "편지를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_Letter_search_btn:
                JSONObject myObj = new JSONObject();

                String w3w_adrress = "";
                String receiver_phone = "";

                w3w_adrress += word1.getText().toString() + "." + word2.getText().toString() + "." + word3.getText().toString();
                receiver_phone += phone1.getText().toString() + "-" + phone2.getText().toString() + "-" + phone3.getText().toString();

                Log.d(LetterConstants.TAG, "w3w, receiver_phone :  " + w3w_adrress + "   " + receiver_phone);
                try {
                    myObj.put("w3w_address", w3w_adrress);
                    myObj.put("receiver_phone", receiver_phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                httpConnectionToPhwysl.getHttpConnectionInstance().HttpPostData(myObj, LetterConstants.FIND_LETTER_API);
                break;
            case R.id.find_by_sms:
                new LovelyTextInputDialog(mContext, R.style.EditTextTintTheme)
                        .setTopColorRes(R.color.rosequartz)
                        .setTitle("\nSMS 붙여넣기")
                        .setTitleGravity(1)
                        .setMessageGravity(1)
                        .setConfirmButtonColor(R.color.darkDeepOrange)
                        .setCancelable(true)
                        .setNegativeButtonColor(R.color.darkDeepOrange)
                        .setTopTitleColor(0x000000)
                        .setHint("010-2043-8751\n하이,헬로우,안녕")
                        .setMessage("받은 메시지를 입력해주세요\n\n")
                        .setIcon(R.drawable.ic_star_border_white_36dp)
                        .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                            @Override
                            public boolean check(String text) {
                                Log.d("HONG", "result" + LetterUtils.isVaildSmsFormat(text));
                                return LetterUtils.isVaildSmsFormat(text);
                            }
                        })
                        .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                            @Override
                            public void onTextInputConfirmed(String text) {
                                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                                JSONObject myObj = new JSONObject();
                                String inputText[] = text.split("\n");
                                Log.d(LetterConstants.TAG, "w3w, receiver_phone :  " + inputText[0] + "   " + inputText[1]);
                                try {
                                    myObj.put("receiver_phone", inputText[0]);
                                    myObj.put("w3w_address", inputText[1]);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                httpConnectionToPhwysl.getHttpConnectionInstance().HttpPostData(myObj, LetterConstants.FIND_LETTER_API);
                            }
                        })
                        .show();
            default:
                break;
        }
    }

    public void showLetterSelectionDialog(final JSONArray myJsonArr) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<LetterOption> adapter = new LetterAdapter(mContext, getLetterOption(myJsonArr));
                new LovelyChoiceDialog(mContext)
                        .setTopColorRes(R.color.darkGreen)
                        .setTitle(R.string.donate_title)
                        .setIcon(R.drawable.ic_local_atm_white_36dp)
                        .setMessage(R.string.donate_message)
                        .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<LetterOption>() {
                            @Override
                            public void onItemSelected(int position, LetterOption item) {
                                Log.d("HONG", "onItemSelected: " + position + " selected!");
                                try {
                                    showMyLetter((JSONObject) myJsonArr.get(position));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .show();
            }
        });


    }

    private List<LetterOption> getLetterOption(JSONArray myJsonArr) {
        List<LetterOption> result = new ArrayList<>();
//        String[] raw = getResources().getStringArray(R.array.donations);
        ArrayList<String> myArr = new ArrayList<String>();
        for (int i = 0; i < myJsonArr.length(); i++) {
            try {
                JSONObject myObj = (JSONObject) myJsonArr.get(i);
                myArr.add(i + "%" + (String) myObj.get("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;

        }
        for (int j = 0; j < myArr.size(); j++) {
            String[] info = myArr.get(j).split("%");
            result.add(new LetterOption(info[1], info[0]));
        }
        return result;
    }




}

