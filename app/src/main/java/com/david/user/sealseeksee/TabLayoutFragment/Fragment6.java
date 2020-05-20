package com.david.user.sealseeksee.TabLayoutFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.david.user.sealseeksee.R;

public class Fragment6 extends Fragment {
    private TextView startBtn;
    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment6, container, false);

        return rootView;
    }
}