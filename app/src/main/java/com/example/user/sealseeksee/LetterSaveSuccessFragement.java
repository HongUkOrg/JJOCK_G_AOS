package com.example.user.sealseeksee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LetterSaveSuccessFragement extends Fragment implements View.OnClickListener{

    private Button btn_sms_send,btn_sms_cancel;
    private TextView success_w3w,success_phoneNumber;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("HONG", "frag2 onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("HONG", "frag onCreateView: ");
        View view = inflater.inflate(R.layout.save_letter_success_fragment,null);

        btn_sms_send = (Button)view.findViewById(R.id.btn_success_sms);
        btn_sms_cancel = (Button)view.findViewById(R.id.btn_success_cancel);
        success_w3w = (TextView)view.findViewById(R.id.success_w3w);
        success_phoneNumber = (TextView)view.findViewById(R.id.success_phone);
        btn_sms_send.setOnClickListener(this);
        btn_sms_cancel.setOnClickListener(this);

        success_w3w.setText(HongController.getInstance().getMy_w3w());
        success_phoneNumber.setText(HongController.getInstance().getSavedPhoneNumber());


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_success_sms:
                removeFragment();
                sendSmsIntent(HongController.getInstance().getSavedPhoneNumber(),HongController.getInstance().getMy_w3w());
                break;
            case R.id.btn_success_cancel:
                removeFragment();
                break;
        }
    }

    public void sendSmsIntent(String number, String word) {
        try {
            Uri smsUri = Uri.parse("sms:" + number);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
            sendIntent.putExtra("sms_body", number + "\n" + word);
            getActivity().startActivity(sendIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void removeFragment()
    {
        getActivity().
                getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.anim.close_letter_write_anim,R.anim.close_letter_write_anim2).
                remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.changeFragment)).commit();
        ((LetterMainActivity)getActivity()).translateW3Wbar(0);
        ((LetterMainActivity)getActivity()).infoView.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(),"Move to SMS input",Toast.LENGTH_SHORT).show();

        return;
    }
}
