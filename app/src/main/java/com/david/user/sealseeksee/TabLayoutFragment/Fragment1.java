package com.david.user.sealseeksee.TabLayoutFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.david.user.sealseeksee.JGController;
import com.david.user.sealseeksee.R;

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
        JGController.getInstance().finderChangeListener.changeFinder(1);
    }


}