package com.theworld.vibratestrings.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.theworld.vibratestrings.R;

public class SessionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
    }

    public void click(View view) {
        String url = "https://us05web.zoom.us/j/7613372199?pwd=YTZLQk95dS9KeWVHYzFiZ2RGcTE2UT09";
        Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(link);
    }

}