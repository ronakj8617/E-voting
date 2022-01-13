package com.example.sharekhancalc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText et1,et2;
    LinearLayout ll1;
    Button signup,login;
    DatabaseReference rf;
    Intent i1,i2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        signup=(Button)findViewById(R.id.btn1);
        login=(Button)findViewById(R.id.btn2);

        i1=new Intent(getApplicationContext(),Registration.class);
        i2=new Intent(getApplicationContext(),MainActivity3.class);

        rf = FirebaseDatabase.getInstance().getReference("Voters");

        SharedPreferences sp = getSharedPreferences("epic", Context.MODE_PRIVATE);
        String user = sp.getString("epic", "");

        if (!user.isEmpty()) {
            Intent i1 = new Intent(getApplicationContext(), MainActivity3.class);
            startActivity(i1);
            finish();
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(i1);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    rf.child(et1.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Firebase_Voter_Registration obj=snapshot.getValue(Firebase_Voter_Registration.class);

                            if(et1.getText().toString().equals(obj.epic) && et2.getText().toString().equals(obj.password))
                            {
                                startActivity(i2);
                                SharedPreferences sp = getSharedPreferences("epic", Context.MODE_PRIVATE);
                                SharedPreferences.Editor speditor = sp.edit();
                                speditor.putString("epic", et1.getText().toString());
                                speditor.apply();
                                speditor.commit();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"EPIC No. or Password is incorrect",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                catch (NullPointerException nj)
                {

                }
            }
        });


    }


}