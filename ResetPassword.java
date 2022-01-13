package com.example.sharekhancalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        auth= FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword("xyz@gmail.com", "123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(ResetPassword.this, "Successful", Toast.LENGTH_SHORT).show();
            }
        });

        auth.getInstance().setLanguageCode("en");

    }
    public void done() {
     auth.signInWithEmailAndPassword("evoteindia6@gmail.com","1237899").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
          @Override
          public void onSuccess(AuthResult authResult) {
              Toast.makeText(ResetPassword.this, "Login", Toast.LENGTH_SHORT).show();

          }
      });
       auth.sendPasswordResetEmail("evoteindia6@gmail.com").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ResetPassword.this, "Reset1", Toast.LENGTH_SHORT).show();
            }
        });


    }
}