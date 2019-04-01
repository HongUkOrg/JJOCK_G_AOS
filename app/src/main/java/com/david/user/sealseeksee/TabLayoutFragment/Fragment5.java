package com.david.user.sealseeksee.TabLayoutFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.user.sealseeksee.LetterMainActivity;
import com.david.user.sealseeksee.R;
import com.igaworks.v2.core.AdBrixRm;

import org.w3c.dom.Text;

public class Fragment5 extends Fragment {
    private TextView startBtn;
    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.fragment_fragment5, container, false);
        Bundle args = getArguments();
//        ((TextView) rootView.findViewById(android.R.id.text1)).setText(Integer.toString(args.getInt(ARG_OBJECT)));

        startBtn = (TextView) rootView.findViewById(R.id.btn_main_start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LetterMainActivity.class));
                AdBrixRm.event("skip_called");
            }
        });
        return rootView;
    }
}