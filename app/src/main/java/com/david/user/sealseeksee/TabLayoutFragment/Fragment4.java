package com.david.user.sealseeksee.TabLayoutFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.david.user.sealseeksee.HongController;
import com.david.user.sealseeksee.LetterMainActivity;
import com.david.user.sealseeksee.R;
import com.igaworks.v2.core.AdBrixRm;

public class Fragment4 extends Fragment {
    public static final String ARG_OBJECT = "object";
    private Button skip;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment4, container, false);

        return rootView;
    }
}