package com.example.sharekhancalc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseReference rf, rf1;
    Button signup, login;
    EditText epic, mob, name, pw, cpw, dob, addr, email, state;
    RadioGroup grp;
    RadioButton r;
    CircleImageView pic;
    Intent i1, i2;
    Uri path;
    String download;
    ArrayList<String> pl, as, lc;
    String st;
    Spinner s1, s2, s3, s4;
    boolean status;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        rf = FirebaseDatabase.getInstance().getReference("Voters");
        rf1 = FirebaseDatabase.getInstance().getReference("Elections");

        pic = findViewById(R.id.pic);

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://e-voting-ab239.appspot.com");

        storageRef = storage.getReference().child("Images");

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFile();
            }
        });
        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.signin);

        status = false;

        i1 = new Intent(getApplicationContext(), MainActivity3.class);
        i2 = new Intent(getApplicationContext(), Login.class);

        s1 = findViewById(R.id.state);
        s2 = findViewById(R.id.parliament);
        s3 = findViewById(R.id.assembly);
        s4 = findViewById(R.id.local);
        epic = findViewById(R.id.epic);
        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        mob = findViewById(R.id.mob);
        pw = findViewById(R.id.pw);
        cpw = findViewById(R.id.cpw);
        addr = findViewById(R.id.addr);
        email = findViewById(R.id.email);
///            state=findViewById(R.id.state);

        grp = findViewById(R.id.rbg);
        s1.setOnItemSelectedListener(this);

        pl = new ArrayList<>();
        as = new ArrayList<>();
        lc = new ArrayList<>();
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<String> PC_NAME = databaseAccess.getPC_NAME();
        databaseAccess.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, PC_NAME);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    uploadFile();

                    storageRef.child(epic.getText().toString() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //   download=uri.getPath();
                        }
                    });

                    int i = grp.getCheckedRadioButtonId();
                    r = findViewById(i);

                    Firebase_Voter_Registration r1 = new Firebase_Voter_Registration();
                    boolean val=validate();
                    if(val && pw.getText().toString().equals(cpw.getText().toString())) {
                        if (status && download != null) {
                            r1.setName(name.getText().toString());
                            r1.setAddress(addr.getText().toString());
                            r1.setDob(dob.getText().toString());
                            r1.setEmail(email.getText().toString());
                            r1.setPassword(pw.getText().toString());
                            r1.setEpic(epic.getText().toString());
                            r1.setGender(r.getText().toString());
                            r1.setIsAllowed("false");
                            r1.setMobile(mob.getText().toString());
                            r1.setAssembly(s3.getSelectedItem().toString());
                            r1.setParliament(s2.getSelectedItem().toString());
                            r1.setLocal(s4.getSelectedItem().toString());
                            r1.setState(st);
                            r1.setImg(download);
                            rf.child(epic.getText().toString()).setValue(r1);

                            SharedPreferences sp = getSharedPreferences("epic", Context.MODE_PRIVATE);
                            SharedPreferences.Editor speditor = sp.edit();
                            speditor.putString("epic", epic.getText().toString());
                            speditor.apply();
                            speditor.commit();
                            startActivity(i1);
                        } else {
                            Toast.makeText(getApplicationContext(), "Image is still being uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i2);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        st = s1.getSelectedItem().toString();
        reload(st);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void reload(String st) {
        as.clear();
        pl.clear();
        lc.clear();
        rf1.child("Constituencies").child(st).child("Assembly").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    Firebase_Constituencies c1 = dsp.getValue(Firebase_Constituencies.class);
                    as.add(c1.getConstituency());

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, as);

                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s3.setAdapter(adapter2);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        rf1.child("Constituencies").child(st).child("Parliament").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    Firebase_Constituencies c1 = dsp.getValue(Firebase_Constituencies.class);
                    pl.add(c1.getConstituency());
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, pl);

                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s2.setAdapter(adapter1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rf1.child("Constituencies").child(st).child("Local").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    Firebase_Constituencies c1 = dsp.getValue(Firebase_Constituencies.class);
                    lc.add(c1.getConstituency());

                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, lc);

                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s4.setAdapter(adapter3);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            storageRef.child(epic.getText().toString() + ".jpg").putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
        boolean valid=true;

        if (epic.getText().toString().equals(""))
        {
            epic.setError("Enter some value");
            valid=false;
        }

        if (pw.getText().toString().equals(""))
        {
            pw.setError("Enter some value");
            valid=false;
        }

        if (dob.getText().toString().equals(""))
        {
            dob.setError("Enter some value");
            valid=false;
        }

        if (addr.getText().toString().equals(""))
        {
            addr.setError("Enter some value");
            valid=false;
        }

        if (mob.getText().toString().equals(""))
        {
            mob.setError("Enter some value");
            valid=false;
        }

        if (email.getText().toString().equals(""))
        {
            email.setError("Enter some value");
            valid=false;
        }

        if (name.getText().toString().equals(""))
        {
            name.setError("Enter some value");
            valid=false;
        }

        if (cpw.getText().toString().equals(""))
        {
            cpw.setError("Enter some value");
            valid=false;
        }

      if(path==null)
      {
          valid=false;
          Toast.makeText(getApplicationContext(),"Upload your photo",Toast.LENGTH_SHORT).show();

      }

        if (epic.getText().toString().equals(""))
        {
            epic.setError("Enter some value");
            valid=false;
        }

        int i = grp.getCheckedRadioButtonId();
        if(i<0)
        {
            Toast.makeText(getApplicationContext(),

                    "Select a gender",Toast.LENGTH_SHORT).show();
            valid=false;
        }

        return valid;
    }

}