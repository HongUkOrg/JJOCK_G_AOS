package com.example.user.sealseeksee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView main_image = (ImageView)findViewById(R.id.letter_image);
        HongController.getInstance().setMyContext(getApplicationContext());

        main_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                startActivity(new Intent(LoginActivity.this,MainActivity.class));

            }
        });

    }

}
