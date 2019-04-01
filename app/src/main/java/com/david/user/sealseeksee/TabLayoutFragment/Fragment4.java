package com.david.user.sealseeksee.TabLayoutFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.david.user.sealseeksee.LetterMainActivity;
import com.david.user.sealseeksee.R;
import com.igaworks.v2.core.AdBrixRm;

public class Fragment4 extends Fragment {
    public static final String ARG_OBJECT = "object";
    private Button skip;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.fragment_fragment4, container, false);
        Bundle args = getArguments();
//        ((TextView) rootView.findViewById(android.R.id.text1)).setText(Integer.toString(args.getInt(ARG_OBJECT)));
        skip = (Button)rootView.findViewById(R.id.btn_main_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LetterMainActivity.class));
                AdBrixRm.event("skip_called");
            }
        });
        return rootView;
    }
}