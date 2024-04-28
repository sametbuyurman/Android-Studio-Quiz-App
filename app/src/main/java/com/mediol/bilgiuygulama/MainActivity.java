package com.mediol.bilgiuygulama;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onSignInClick(View v){
        Intent intent = new Intent(MainActivity.this,GirisActivity.class);
        startActivity(intent);
    }
    public void onCreateAccountClick(View v){
        Intent intent = new Intent(MainActivity.this,KayitActivity.class);
        startActivity(intent);
    }


}