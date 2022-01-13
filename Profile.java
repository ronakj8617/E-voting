package com.example.sharekhancalc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    CircleImageView back, pic;
    Button save;
    EditText sfn, ssn, sln, spw, sdob, scno, email, state;
    Uri path;
    boolean status;
    StorageReference storageRef;
    RadioGroup grp;
    RadioButton r, rb1, rb2;
    DatabaseReference rf, rf1;
    String user, download;
    int img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        rf = FirebaseDatabase.getInstance().getReference("Voters");
        rf1 = FirebaseDatabase.getInstance().getReference("Elections");

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://e-voting-ab239.appspot.com");

        storageRef = storage.getReference().child("Images");

        back = findViewById(R.id.back);
        pic = findViewById(R.id.pic);
        save = findViewById(R.id.save);

        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        sfn = findViewById(R.id.sfn);
        ssn = findViewById(R.id.ssn);
        sln = findViewById(R.id.sln);
        spw = findViewById(R.id.spw);
        scno = findViewById(R.id.scno);
        email = findViewById(R.id.email);
        sdob = findViewById(R.id.sdob);
        state = findViewById(R.id.sstate);
        grp = findViewById(R.id.sgrp);
        SharedPreferences sp = getSharedPreferences("epic", Context.MODE_PRIVATE);
        user = sp.getString("epic", "");

        try {
            rf.child(user).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Firebase_Voter_Registration f1 = snapshot.getValue(Firebase_Voter_Registration.class);
                    sfn.setText(f1.getName());
                    ssn.setText(f1.getAddress());
                    sln.setText(f1.getEpic());
                    sdob.setText(f1.getDob());
                    email.setText(f1.getEmail());
                    state.setText(f1.getState());
                    scno.setText(f1.getMobile());
                    spw.setText(f1.getPassword());
                    Picasso.get().load(f1.getImg()).into(pic);
                    img = 1;
                    if (f1.getGender().equals("Male"))
                        rb1.setChecked(true);
                    else
                        rb2.setChecked(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (NullPointerException jgj) {

        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();

                storageRef.child(user + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //   download=uri.getPath();
                    }
                });
                int i = grp.getCheckedRadioButtonId();
                r = findViewById(i);
                try {
                    rf.child(user).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Firebase_Voter_Registration rf1 = snapshot.getValue(Firebase_Voter_Registration.class);
                            Firebase_Voter_Registration r1 = new Firebase_Voter_Registration();
                            boolean val = validate();
                            if (img == 1) {
                                if (val) {
                                    r1.setName(sfn.getText().toString());
                                    r1.setAddress(ssn.getText().toString());
                                    r1.setDob(sdob.getText().toString());
                                    r1.setEmail(email.getText().toString());
                                    r1.setPassword(spw.getText().toString());
                                    r1.setEpic(sln.getText().toString());
                                    r1.setGender(r.getText().toString());
                                    r1.setIsAllowed("false");
                                    r1.setMobile(scno.getText().toString());
                                    r1.setAssembly(rf1.getAssembly());
                                    r1.setParliament(rf1.getParliament());
                                    r1.setLocal(rf1.getLocal());
                                    r1.setState(state.getText().toString());
                                    r1.setImg(rf1.getImg());

                                    rf.child(user).setValue(r1);
                                }
                            } else {
                                if (status && download != null) {
                                    if (val) {
                                        r1.setName(sfn.getText().toString());
                                        r1.setAddress(ssn.getText().toString());
                                        r1.setDob(sdob.getText().toString());
                                        r1.setEmail(email.getText().toString());
                                        r1.setPassword(spw.getText().toString());
                                        r1.setEpic(sln.getText().toString());
                                        r1.setGender(r.getText().toString());
                                        r1.setIsAllowed("false");
                                        r1.setMobile(scno.getText().toString());
                                        r1.setAssembly(rf1.getAssembly());
                                        r1.setParliament(rf1.getParliament());
                                        r1.setLocal(rf1.getLocal());
                                        r1.setState(state.getText().toString());
                                        r1.setImg(download);

                                        rf.child(user).setValue(r1);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } catch (NullPointerException nbgj) {

                }
                Toast.makeText(getApplicationContext(), "Data Successfully Updated", Toast.LENGTH_SHORT).show();
            }
        });
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFile();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity3.class);
                startActivity(i);
            }
        });
    }

    void showFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select"), 234);

    }

    void uploadFile() {

        if (path != null) {
            img = 2;
            storageRef.child(user + ".jpg").putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            download = uri.toString();
                        }
                    });
                    //download=taskSnapshot.getUploadSessionUri().getPath().toString();
                    Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    status = true;
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    ///     pg.setMessage((int) progress + "% Done");
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    status = true;

                    //download=task.getResult().getUploadSessionUri().toString(); perfect
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 234 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            path = data.getData();
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                pic.setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean validate() {
        boolean valid = true;

        if (sfn.getText().toString().equals("")) {
            sfn.setError("Enter some value");
            valid = false;
        }

        if (spw.getText().toString().equals("")) {
            spw.setError("Enter some value");
            valid = false;
        }

        if (sdob.getText().toString().equals("")) {
            sdob.setError("Enter some value");
            valid = false;
        }

        if (ssn.getText().toString().equals("")) {
            ssn.setError("Enter some value");
            valid = false;
        }

        if (sln.getText().toString().equals("")) {
            sln.setError("Enter some value");
            valid = false;
        }

        if (email.getText().toString().equals("")) {
            email.setError("Enter some value");
            valid = false;
        }

        if (scno.getText().toString().equals("")) {
            scno.setError("Enter some value");
            valid = false;
        }

        if (sdob.getText().toString().equals("")) {
            sdob.setError("Enter some value");
            valid = false;
        }

        int i = grp.getCheckedRadioButtonId();
        if (i < 0) {
            Toast.makeText(getApplicationContext(),

                    "Select a gender", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
}