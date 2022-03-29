package com.theworld.vibratestrings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theworld.vibratestrings.ui.CourseActivity;
import com.theworld.vibratestrings.ui.DashboardActivity;
import com.theworld.vibratestrings.ui.auth.LoginActivity;
import com.theworld.vibratestrings.utils.ExtensionKt;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Context context = this;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        ImageView imageView = findViewById(R.id.splash_img);

        AlphaAnimation anim = new AlphaAnimation(0, 1);
        anim.setDuration(3000);
        imageView.startAnimation(anim);

        new Handler().postDelayed(this::fetchUserProfile, 1000);
    }

    private void fetchUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            String id = currentUser.getUid();

            db.collection("users")
                    .document(id)
                    .get()
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            Intent intent;
                            DocumentSnapshot document = task.getResult();

                            String interestedIn = document.getString("interested_in");

                            if (interestedIn == null || interestedIn.equals("")) {
                                intent = new Intent(SplashActivity.this, CourseActivity.class);

                            } else {
                                intent = new Intent(SplashActivity.this, DashboardActivity.class);
                            }

                            startActivity(intent);
                            finish();

                        }
                    });
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}