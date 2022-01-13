package com.example.sharekhancalc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EPIC_CARD extends AppCompatActivity {
    DrawerLayout dl;
    NavigationView n1;
    CircleImageView menu, pic;
    int i;
    DatabaseReference rf, rf1;
    String user, download;
    TextView epic, name, gender, pl, asmbly, local;
    StorageReference storageRef;

    int img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_p_i_c__c_a_r_d);


        epic = findViewById(R.id.epic);
        gender = findViewById(R.id.gender);
        pl = findViewById(R.id.pl);
        asmbly = findViewById(R.id.asmbly);
        local = findViewById(R.id.local);
        name = findViewById(R.id.name);

        pic = findViewById(R.id.pic);

        rf = FirebaseDatabase.getInstance().getReference("Voters");

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://e-voting-ab239.appspot.com");

        storageRef = storage.getReference().child("Images");

        SharedPreferences sp = getSharedPreferences("epic", Context.MODE_PRIVATE);
        user = sp.getString("epic", "");
        try {
            rf.child(user).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Firebase_Voter_Registration f1 = snapshot.getValue(Firebase_Voter_Registration.class);
                    name.setText("Name: "+f1.getName());
                    epic.setText(f1.getEpic());
                    asmbly.setText("Assembly Constituency: "+ f1.getAssembly());
                    gender.setText("Gender: "+f1.getGender());
                    pl.setText("Parliament Constituency: "+f1.getParliament());
                    local.setText("Local Constituency: "+f1.getLocal());
                    Picasso.get().load(f1.getImg()).into(pic);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (NullPointerException j) {

        }
        n1 = findViewById(R.id.nav2);
        dl = findViewById(R.id.dl_home);
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.openDrawer(Gravity.START);
            }
        });
        n1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                i = item.getItemId();

                if (i == R.id.home) {

                    Intent i2;
                    i2 = new Intent(getApplicationContext(), MainActivity3.class);
                    startActivity(i2);
                } else if (i == R.id.live) {
                    Intent i2;
                    i2 = new Intent(getApplicationContext(), Current_Results.class);
                    startActivity(i2);
                } else if (i == R.id.epic_card) {

                    dl.closeDrawer(Gravity.START);

                } else if (i == R.id.profile) {
                    Intent i2;
                    i2 = new Intent(getApplicationContext(), Profile.class);
                    startActivity(i2);

                } else if (i == R.id.cLogout) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EPIC_CARD.this);
                    builder.setTitle("Decide");
                    builder.setTitle("Decide").setMessage("Do you want to Logout?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Toast.makeText(getApplicationContext(), "Your are successfully logged out", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sp = getSharedPreferences("epic", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor speditor = sp.edit();
                                    speditor.clear();
                                    speditor.commit();

                                    Intent i2;

                                    i2 = new Intent(getApplicationContext(), Registration.class);
                                    startActivity(i2);
                                    finish();

                                }

                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Decide!");
                    alert.show();
                 /*   SharedPreferences sp = getSharedPreferences("id3", Context.MODE_PRIVATE);
                    SharedPreferences.Editor speditor = sp.edit();
                    speditor.clear();
                    speditor.commit();
*/
                } else if (i == R.id.delete) {
                    Intent i2;
                    i2 = new Intent(getApplicationContext(), Current_elections.class);
                    startActivity(i2);

                }
                return false;
            }
        });

        n1.setItemIconTintList(null);

    }
}