package com.david.user.sealseeksee.TabLayoutFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.david.user.sealseeksee.JGController;
import com.david.user.sealseeksee.R;

public class Fragment3 extends Fragment {
    public static final String ARG_OBJECT = "object";
    private Button skip;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment3, container, false);

        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        JGController.getInstance().finderChangeListener.changeFinder(3);
    }
}