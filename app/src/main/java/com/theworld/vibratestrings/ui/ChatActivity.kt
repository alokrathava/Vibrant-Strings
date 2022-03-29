package com.theworld.vibratestrings.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.theworld.vibratestrings.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }


    public void click(View view) {
        String url = "https://chat.whatsapp.com/GsLd9XUEdHFEa5E577LevA";
        Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(link);
    }
}