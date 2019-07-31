package com.david.user.sealseeksee.TabLayoutFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.david.user.sealseeksee.HongController;
import com.david.user.sealseeksee.LetterMainActivity;
import com.david.user.sealseeksee.MainViewActivity;
import com.david.user.sealseeksee.R;
import com.igaworks.v2.core.AdBrixRm;

public class Fragment1 extends Fragment {
    private Button skip;
    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.fragment_fragment1, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        HongController.getInstance().finderChangeListener.changeFinder(1);
    }


}