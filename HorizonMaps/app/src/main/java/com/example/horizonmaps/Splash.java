package com.example.horizonmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    private static int Splash_Screen= 5000;
    //Variables for image/animation
    Animation ALogo;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //Animation to pass
        ALogo= AnimationUtils.loadAnimation(this,R.anim.logo_animation);

        //Hooks
        image=findViewById(R.id.Logoh);

        //assign image to animation
        image.setAnimation(ALogo);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent= new Intent(Splash.this,Login.class);
                startActivity(intent);
                finish();
            }
        },Splash_Screen);
    }
}