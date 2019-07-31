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

import com.david.user.sealseeksee.HongController;
import com.david.user.sealseeksee.LetterMainActivity;
import com.david.user.sealseeksee.R;
import com.igaworks.v2.core.AdBrixRm;

import org.w3c.dom.Text;

public class Fragment7 extends Fragment {
    private TextView startBtn;
    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment7, container, false);

        return rootView;
    }
}