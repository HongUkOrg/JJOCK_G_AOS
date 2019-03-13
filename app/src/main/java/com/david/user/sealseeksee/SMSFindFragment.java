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
import android.widget.LinearLayout;
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

public class SMSFindFragment extends Fragment implements View.OnClickListener{

    private EditText smsInput;
    private Button smsOkBtn;
    private Context mContext;
    private FrameLayout findSmsFragment;
    private LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("HONG", "frag onCreateView: ");
        View view = inflater.inflate(R.layout.sms_input_fragment, null);

        smsInput = (EditText)view.findViewById(R.id.sms_edit_text);
        smsOkBtn = (Button)view.findViewById(R.id.btn_OK_sms);
        findSmsFragment = (FrameLayout)view.findViewById(R.id.sms_find_fragment);
        linearLayout = (LinearLayout)view.findViewById(R.id.sms_linear_layout);

        mContext = getActivity();
        smsOkBtn.setOnClickListener(this);

        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(
                (int) (HongController.getInstance().getWidth() * 0.90), (int) (HongController.getInstance().getHeight() * 0.5));
        layout.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        view.setLayoutParams(layout);

        ViewGroup.LayoutParams param = linearLayout.getLayoutParams();
        param.width = (int) (HongController.getInstance().getWidth() * 0.90);
        param.height = (int) (HongController.getInstance().getHeight() * 0.45);
        layout.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        linearLayout.setLayoutParams(param);

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
        if (false) {
            Handler handler = new Handler();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    ((LetterMainActivity) getActivity()).viewSaveSuccessLetterFragmment();
                }
            };
            handler.postDelayed(r, 500);


        }
        return;
    }

    private void findBySms(String input) {
        if(input == null && input.length() == 0) return;
        JSONObject myObj = new JSONObject();
        String inputText[] = input.split("\n");
        Log.d(LetterConstants.TAG, "w3w, receiver_phone :  " + inputText[0] + "   " + inputText[1]);
        try {
            myObj.put("receiver_phone", inputText[0]);
            myObj.put("w3w_address", inputText[1]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpConnectionToPhwysl.getHttpConnectionInstance().HttpPostData(myObj, LetterConstants.FIND_LETTER_API);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_OK_sms:
                findBySms(smsInput.getText().toString());
                break;

        }
    }
}

