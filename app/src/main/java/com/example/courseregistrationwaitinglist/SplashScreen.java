package com.example.courseregistrationwaitinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_splash_screen);
        //To initiate the splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent to_student_course_waiting_list = new Intent(SplashScreen.this, CourseWaitingList.class);
                startActivity(to_student_course_waiting_list);
                finish();
            }
        }, 3000);
    }
}