package com.example.CSCB07Project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceGroup;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class LoginPatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_patient);
    }

    public void createAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivityPatient.class);
        startActivity(intent);
    }

    public void signIn(View view){
        String userId = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        AppCompatActivity activity = this;
        FirebaseAPI.<String>getData("Patients/" + userId + "/password", new Callback<String>() {
            @Override
            public void onCallback(String data) {
                if(password.equals(data)) {
                    java.util.Date date = new Date(System.currentTimeMillis());
                    DatabaseReference P_ref = FirebaseDatabase.getInstance().getReference().child("Patients");
                    Date time = new Date(System.currentTimeMillis());
                    com.example.CSCB07Project.Date t = new com.example.CSCB07Project.Date(time);
                    P_ref.child(userId).child("login_time").setValue(new com.example.CSCB07Project.Date(time));
                    ValueEventListener listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot child:snapshot.getChildren()){
                                Patient patient = child.getValue(Patient.class);
                                if (patient.upcomingAppoint == null){
                                    return;
                                }
                                for (Appointment apt : patient.upcomingAppoint){
                                    if (apt.end.before(t)){
                                        patient.upcomingAppoint.remove(apt);
                                        patient.visited.add(apt.toString());
                                    }
                                }
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.w("warning", "on cancelled");
                        }
                    };
                    P_ref.addValueEventListener(listener);
                    Intent intent = new Intent(activity, PatientDashboard.class);
                    intent.putExtra("userId",userId);
                    startActivity(intent);
                }
            }
        });
    }
}