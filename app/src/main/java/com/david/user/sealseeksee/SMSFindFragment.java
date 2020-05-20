package com.david.user.sealseeksee;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.igaworks.v2.core.AdBrixRm;

import org.json.JSONException;
import org.json.JSONObject;

public class SMSFindFragment extends Fragment implements View.OnClickListener,LetterUtils.OnBackPressedListener{

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
                (int) (JGController.getInstance().getWidth() * 0.90), (int) (JGController.getInstance().getHeight() * 0.5));
        layout.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        view.setLayoutParams(layout);

        ViewGroup.LayoutParams param = linearLayout.getLayoutParams();
        param.width = (int) (JGController.getInstance().getWidth() * 0.90);
        param.height = (int) (JGController.getInstance().getHeight() * 0.45);
        layout.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        linearLayout.setLayoutParams(param);

        ((LetterMainActivity)getActivity()).setOnBackPressedListener(this);
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
        if(((LetterMainActivity)getActivity()).onBackPressedListener!=null){
            ((LetterMainActivity)getActivity()).onBackPressedListener=null;
        }
        getActivity().
                getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.anim.close_letter_seek_to_left, R.anim.close_letter_seek_to_left2).
                remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.changeFragment)).commit();

        return;
    }

    private void findBySms(String input) {
        if(input == null || input.length() == 0) return;
        int tempIndex = 0;
        JSONObject myObj = new JSONObject();
        String inputText[] = input.split("\n");
        if(inputText.length<2) {
            Toast.makeText(getActivity(),"정해진 형식으로 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Log.d(LetterConstants.TAG, "w3w, receiver_phone :  " + inputText[0] + "   " + inputText[1]);
            if(inputText[0].startsWith("쪽지가 도착했습니다")) tempIndex = 1;
            else tempIndex = 0;
            myObj.put("receiver_phone", inputText[tempIndex]);
            myObj.put("w3w_address", inputText[tempIndex+1]);
            AdBrixRm.event("find_by_sms");
            httpConnectionToPhwysl.getHttpConnectionInstance().HttpPostData(myObj, LetterConstants.FIND_LETTER_API);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_OK_sms:
                findBySms(smsInput.getText().toString());
                break;

        }
    }

    @Override
    public void doBack() {
        removeFragmentToLeft();
    }
}

