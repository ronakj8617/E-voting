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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.Cast_Vote;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Current_elections extends AppCompatActivity {
    LinearLayout l1, l2, l3;
    DrawerLayout dl;
     String type = "";
    NavigationView n1;
    CircleImageView menu;
    ImageView vote;
    EditText c_name, c_no, state, type_et;
    DatabaseReference rf1,rf;
    int i;
    String user,st,local,assembly,parliament;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_elections);

        l1 = findViewById(R.id.local);
        l2 = findViewById(R.id.assembly);
        l3 = findViewById(R.id.parliament);

        vote=findViewById(R.id.vote);

        n1 = findViewById(R.id.nav2);
        dl = findViewById(R.id.dl_home);

        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.openDrawer(Gravity.START);
            }
        });

        type_et = findViewById(R.id.type);
        c_no = findViewById(R.id.c_no);
        c_name = findViewById(R.id.c_name);
        state = findViewById(R.id.state);

        rf=FirebaseDatabase.getInstance().getReference("Voters");

        rf1 = FirebaseDatabase.getInstance().getReference("Elections");

       SharedPreferences sp = getSharedPreferences("epic", Context.MODE_PRIVATE);
       user= sp.getString("epic", "");

        rf.child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Firebase_Voter_Registration f1=snapshot.getValue(Firebase_Voter_Registration.class);
                st=f1.getState();
                assembly=f1.getAssembly();
                parliament=f1.getParliament();
                local=f1.getLocal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("") )
                {
                    Toast.makeText(getApplicationContext(),"Select An Election Type",Toast.LENGTH_SHORT).show();
                }
                else if(!type.isEmpty() && !c_name.getText().toString().isEmpty())
                {
                    Intent i= new Intent(Current_elections.this, Device.class);
                    SharedPreferences sp = getSharedPreferences("type", Context.MODE_PRIVATE);
                    SharedPreferences.Editor speditor = sp.edit();
                    speditor.putString("type", type);
                    speditor.apply();
                    speditor.commit();
                    startActivity(i);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Current_elections.this);
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
                 dl.closeDrawer(Gravity.START);

                }
                return false;
            }
        });

        n1.setItemIconTintList(null);

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l1.setBackgroundResource(R.drawable.cards);
                l2.setBackgroundResource(0);
                l3.setBackgroundResource(0);
                type = "Local";
                type_et.setText("");
                c_name.setText("");
                c_no.setText("");
                state.setText("");
                fetch();
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l2.setBackgroundResource(R.drawable.cards);
                l1.setBackgroundResource(0);
                l3.setBackgroundResource(0);
                type = "Assembly";
                type_et.setText("");
                c_name.setText("");
                c_no.setText("");
                state.setText("");
                fetch();
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l3.setBackgroundResource(R.drawable.cards);
                l2.setBackgroundResource(0);
                l1.setBackgroundResource(0);
                type = "Parliament";
                type_et.setText("");
                c_name.setText("");
                c_no.setText("");
                state.setText("");
                fetch();
            }
        });

        if (!type.isEmpty()) {

        }


    }

    void fetch() {

        try {
            if (type.equals("Parliament")) {
                rf1.child("Constituencies").child(st).child(type).child(parliament).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            Firebase_Constituencies obj = snapshot.getValue(Firebase_Constituencies.class);

                            if (obj.getIsAdjourned().equals("false")) {
                                state.setText(obj.getState());
                                type_et.setText(obj.getType());
                                c_name.setText(obj.getConstituency());
                                c_no.setText(obj.getConstituency_Number());
                            }
                        } catch (NullPointerException ne) {
                            Toast.makeText(getApplicationContext(), "No Current Parliament Election Is Going On", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else if (type.equals("Assembly")) {
                rf1.child("Constituencies").child(st).child(type).child(assembly).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        try {
                            Firebase_Constituencies obj = snapshot.getValue(Firebase_Constituencies.class);

                            if (obj.getIsAdjourned().equals("false")) {
                                state.setText(obj.getState());
                                type_et.setText(obj.getType());
                                c_name.setText(obj.getConstituency());
                                c_no.setText(obj.getConstituency_Number());
                            }
                        } catch (NullPointerException ne) {
                            Toast.makeText(getApplicationContext(), "No Current Assembly Election Is Going On", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else if (type.equals("Local")) {
                rf1.child("Constituencies").child(st).child(type).child(local).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        try {
                            Firebase_Constituencies obj = snapshot.getValue(Firebase_Constituencies.class);

                            if (obj.getIsAdjourned().equals("false")) {
                                state.setText(obj.getState());
                                type_et.setText(obj.getType());
                                c_name.setText(obj.getConstituency());
                                c_no.setText(obj.getConstituency_Number());
                            }
                        } catch (NullPointerException ne) {
                            Toast.makeText(getApplicationContext(), "No Current Local Election Is Going On", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        catch (NullPointerException bbhv)
        {
            Toast.makeText(getApplicationContext(), "No Current " +type+" Election Is Going On", Toast.LENGTH_SHORT).show();
        }

    }
}