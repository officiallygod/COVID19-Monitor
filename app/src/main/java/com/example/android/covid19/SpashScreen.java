package com.example.android.covid19;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SpashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN=3000;

    Animation topAnim, bottomAnim;
    ImageView splash_logo;
    TextView splash_logo_name, splash_author_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spash_screen);

        topAnim = AnimationUtils.loadAnimation(SpashScreen.this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(SpashScreen.this, R.anim.bottom_animation);

        splash_logo = findViewById(R.id.splash_logo);
        splash_logo_name = findViewById(R.id.splash_logo_name);
        splash_author_name = findViewById(R.id.splash_author_name);


        splash_logo.setAnimation(topAnim);
        splash_logo_name.setAnimation(bottomAnim);
        splash_author_name.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SpashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}
