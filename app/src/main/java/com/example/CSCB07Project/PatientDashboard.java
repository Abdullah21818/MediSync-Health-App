package com.example.CSCB07Project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PatientDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_patient);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        FirebaseAPI.getPatient(userId, new Callback<HashMap<String, Object>>() {
            @Override
            public void onCallback(HashMap<String, Object> data) {
                Log.i("Patient info",data.toString());
                Patient patient = new Patient(data);
                SpannableStringBuilder patientInfo = makeBold("Username: ", patient.getUserId());
                patientInfo.append(makeBold("\nName: ", patient.getName()));
                patientInfo.append(makeBold("\nGender: ", patient.getGender()));

                TextView userText = findViewById(R.id.textView9);
                userText.setText(patientInfo);
            }
        });

        //Log.i("note", "breakpoint 1");
        //Patient patient = FirebaseHelper.getPatient("Patient", userId);
        //Log.i("note", "breakpoint 2");

        // Remember to get upcomingAppoint info and put it in the TextView in appointScroll
        // Right now reading appointment info from Firebase doesn't work?
    }

    private SpannableStringBuilder makeBold(String boldText, String text) {
        SpannableStringBuilder info = new SpannableStringBuilder(boldText + text);
        StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);
        info.setSpan(bold, 0, boldText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return info;
    }
}