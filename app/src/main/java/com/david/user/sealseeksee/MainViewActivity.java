package com.david.user.sealseeksee;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.david.user.sealseeksee.TabLayoutFragment.LetterCollectionPagerAdapter;

public class MainViewActivity extends FragmentActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        LetterCollectionPagerAdapter letterCollectionPagerAdapter = new LetterCollectionPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(letterCollectionPagerAdapter);
        if(getIntent().getData()!=null)
        {
            Bundle bundle = getIntent().getExtras();
            Log.d("HONG", "onCreate: "+bundle.toString());
            Log.d("HONG", "onCreate: "+getIntent().getData().toString());
        }
    }
}l

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.


// Instances of this class are fragments representing a single
// object in our collection.
