package com.david.user.sealseeksee;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.david.user.sealseeksee.TabLayoutFragment.LetterCollectionPagerAdapter;
import com.igaworks.v2.core.AdBrixRm;

public class MainViewActivity extends FragmentActivity implements HongController.FinderChangeListener {

    protected HongController.FinderChangeListener finderChangeListener;
    private ImageView finder;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button skip;
        LinearLayout cartoonView = (LinearLayout) findViewById(R.id.cartoonView);
        ViewGroup.LayoutParams param = cartoonView.getLayoutParams();
        param.height = (int) (HongController.getInstance().getHeight() * 0.70);
        cartoonView.setGravity(Gravity.CENTER);
        cartoonView.setLayoutParams(param);

        skip = (Button)findViewById(R.id.btn_main_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainViewActivity.this, LetterMainActivity.class));
                AdBrixRm.event("skip_called");
            }
        });
        finder = findViewById(R.id.finder);

        HongController.getInstance().setFinderChangeListener(this);
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
    @Override
    public void changeFinder(int i) {
        switch (i){
            case 1:
                finder.setImageResource(R.drawable.finder01);
                break;
            case 2:
                finder.setImageResource(R.drawable.finder02);
                break;
            case 3:
                finder.setImageResource(R.drawable.finder03);
                break;
            case 4:
                finder.setImageResource(R.drawable.finder04);
                break;
            case 5:
                finder.setImageResource(R.drawable.finder05);
                break;
            case 6:
                finder.setImageResource(R.drawable.finder06);
                break;
            case 7:
                finder.setImageResource(R.drawable.finder07);
                break;
            case 8:
                finder.setImageResource(R.drawable.finder08);
                break;
        }
    }
}
