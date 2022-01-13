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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Vote_History extends AppCompatActivity {
    LinearLayout l1, l2, l3;
    DrawerLayout dl;
    String type = "";
    NavigationView n1;
    CircleImageView menu;
    ImageView vote;
    EditText can_name, party, date,time;
    DatabaseReference rf1, rf;
    int i;
    String user, st, local, assembly, parliament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote__history);

        l1 = findViewById(R.id.local);
        l2 = findViewById(R.id.assembly);
        l3 = findViewById(R.id.parliament);

        //type_et = findViewById(R.id.type);

        time=findViewById(R.id.time);
        can_name = findViewById(R.id.can_name);
        date = findViewById(R.id.date);
        party = findViewById(R.id.party);

        n1 = findViewById(R.id.nav2);
        dl = findViewById(R.id.dl_home);
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.openDrawer(Gravity.START);
            }
        });

        vote=findViewById(R.id.vote);

        rf = FirebaseDatabase.getInstance().getReference("Voters_History");

        rf1 = FirebaseDatabase.getInstance().getReference("Elections");

        SharedPreferences sp = getSharedPreferences("epic", Context.MODE_PRIVATE);
        user = sp.getString("epic", "");

        rf.child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Firebase_Voter_Registration f1 = snapshot.getValue(Firebase_Voter_Registration.class);
                st = f1.getState();
                assembly = f1.getAssembly();
                parliament = f1.getParliament();
                local = f1.getLocal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Vote_History.this);
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

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l1.setBackgroundResource(R.drawable.cards);
                l2.setBackgroundResource(0);
                l3.setBackgroundResource(0);
                type = "Local";
                date.setText("");
                can_name.setText("");
                  party.setText("");
                  time.setText("");
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

                date.setText("");
                can_name.setText("");
                party.setText("");
                time.setText("");
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

                date.setText("");
                can_name.setText("");
                party.setText("");
                time.setText("");

                fetch();
            }
        });

        if (!type.isEmpty()) {

        }
    }

    void fetch() {

        try {
            if (type.equals("Parliament")) {
                rf.child(user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            Firebase_Vote_History obj = snapshot.getValue(Firebase_Vote_History.class);
                            date.setText(obj.getParliament_date());
                            party.setText(obj.getParliament_party());
                            can_name.setText(obj.getParliament_candidate());
                            time.setText(obj.getParliament_time().substring(11,20));
                        }
                        catch (NullPointerException ne) {
                            Toast.makeText(getApplicationContext(), "No History Of Parliament Election Is Available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else if (type.equals("Assembly")) {
                rf.child(user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        try {
                            Firebase_Vote_History obj = snapshot.getValue(Firebase_Vote_History.class);
                            date.setText(obj.getAssembly_date());
                            party.setText(obj.getAssembly_party());
                            can_name.setText(obj.getAssembly_candidate());
                            time.setText(obj.getAssembly_time().substring(11,20));
                        } catch (NullPointerException ne) {
                            Toast.makeText(getApplicationContext(), "No History Of Assembly Election Is Available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else if (type.equals("Local")) {
                rf.child(user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        try {
                            Firebase_Vote_History obj = snapshot.getValue(Firebase_Vote_History.class);
                            date.setText(obj.getLocal_date());
                            party.setText(obj.getLocal_party());
                            can_name.setText(obj.getLocal_candidate());
                            time.setText(obj.getLocal_time().substring(11,20));
                        } catch (NullPointerException ne) {
                            Toast.makeText(getApplicationContext(), "No History Of  Local Election Is Available", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } catch (NullPointerException bbhv) {
            Toast.makeText(getApplicationContext(), "No History Of " + type + " Voting Is Available", Toast.LENGTH_SHORT).show();
        }

    }
}