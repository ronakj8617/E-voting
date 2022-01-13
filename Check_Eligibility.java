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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Check_Eligibility extends AppCompatActivity {
    EditText et1, et2;
    DatabaseReference rf1, rf;
    String user, st, local, assembly, parliament, allowed, name;
    static Firebase_Voter_Registration f1;
    NavigationView n1;
    int i;
    DrawerLayout dl;
    CircleImageView menubtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check__eligibility);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);

        n1=findViewById(R.id.nav2);
        dl=findViewById(R.id.home_menu);
        menubtn=findViewById(R.id.menu);

        menubtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                dl.openDrawer(Gravity.START);
            }
        });

        SharedPreferences sp = getSharedPreferences("epic", Context.MODE_PRIVATE);
        //   user= sp.getString("epic", "");

        rf = FirebaseDatabase.getInstance().getReference("Voters");

        final SwipeButton enableButton = (SwipeButton) findViewById(R.id.swipe_btn);
        enableButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                user = et1.getText().toString();
                getData();
                if (f1 == null)
                {
                    getData();

                }
                else {
                    if (f1.getEpic().equals(user) && f1.getPassword().equals(et2.getText().toString())) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, new Eligibility())
                                .commit();
                    } else
                    {

                        Toast.makeText(getApplicationContext(),"EPIC NO. or Password is incorrect",Toast.LENGTH_SHORT).show();
                    }
                }

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
                }
                else if (i == R.id.live) {
                    Intent i2;
                    i2 = new Intent(getApplicationContext(), Current_Results.class);
                    startActivity(i2);
                }
                else if (i == R.id.epic_card) {
                    Intent i2;
                    i2 = new Intent(getApplicationContext(), EPIC_CARD.class);
                    startActivity(i2);

                }
                else if (i == R.id.profile) {
                    Intent i2;
                    i2 = new Intent(getApplicationContext(), Profile.class);
                    startActivity(i2);

                } else if (i == R.id.cLogout) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Check_Eligibility.this);
                    builder.setTitle("Decide");
                    builder.setTitle("Decide").setMessage("Do you want to Logout?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Toast.makeText(getApplicationContext(), "Your are successfully logged out", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sp =getSharedPreferences("epic", Context.MODE_PRIVATE);
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

    public Firebase_Voter_Registration getData() {
        rf.child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                f1 = snapshot.getValue(Firebase_Voter_Registration.class);
                st = f1.getState();
                assembly = f1.getAssembly();
                parliament = f1.getParliament();
                local = f1.getLocal();
                name = f1.getName();
                allowed = f1.getIsAllowed();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return f1;
    }
}