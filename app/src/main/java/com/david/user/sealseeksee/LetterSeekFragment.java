package com.david.user.sealseeksee;

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

import com.david.user.sealseeksee.LetterAdapter.LetterAdapter;
import com.david.user.sealseeksee.LetterAdapter.LetterOption;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class LetterSeekFragment extends Fragment implements View.OnClickListener {

    private HttpURLConnection http;
    private EditText phone1, phone2, phone3, word1, word2, word3;
    private EditText fullPhoneNumber;
    private Button find_btn, sms_find_btn;
    private int letterNumber;
    private static Context mContext;
    private String title, message, latitude, longitude;
    private JSONObject myJson = null, myLetter = null;

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
                    word1.requestFocus();
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

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    removeFragment();
                    return true;
                }
                return false;
            }
        });

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

    private void removeFragmentToLeft() {
        getActivity().
                getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.anim.close_letter_seek_to_left, R.anim.close_letter_seek_to_left2).
                remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.changeFragment)).commit();
//                ((LetterMainActivity) getActivity()).translateW3Wbar(0);
//                ((LetterMainActivity) getActivity()).infoView.setVisibility(View.VISIBLE);
        ((LetterMainActivity) getActivity()).viewSMSFindFragmment();


        return;
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
                removeFragmentToLeft();
            default:
                break;
        }
    }


}

