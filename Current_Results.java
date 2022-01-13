package com.example.sharekhancalc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.Firebase_Candidates;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Current_Results extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DrawerLayout dl;
    NavigationView n1;
    CircleImageView menu;
    int i;
    ArrayList<String> al;
    Spinner sp1, sp2;
    DatabaseReference rf, rf1;
    Button pl, asmbly, local;
    static int status, high;
    static String constt;
    TableLayout tab;
    static TableRow head;
    String state;
    static int[] a;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current__results);

        rf = FirebaseDatabase.getInstance().getReference("Elections");
        rf1 = FirebaseDatabase.getInstance().getReference("Poll_Details");

        al = new ArrayList<>();
        local = findViewById(R.id.local);
        pl = findViewById(R.id.pl);
        asmbly = findViewById(R.id.asmbly);

        tab = findViewById(R.id.table);
        head = findViewById(R.id.head);

        sp1 = findViewById(R.id.sp1);
        sp2 = findViewById(R.id.sp2);
        n1 = findViewById(R.id.nav2);
        dl = findViewById(R.id.dl_home);
        menu = findViewById(R.id.menu);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = sp1.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), state, Toast.LENGTH_SHORT).show();
                sp1.setSelection(position);
                if (status == 1) {
                    reload(state);
                } else if (status == 0) {
                    reload(state);
                } else if (status == 2) {
                    reload(state);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp2.setOnItemSelectedListener(this);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<String> PC_NAME = databaseAccess.getPC_NAME();
        databaseAccess.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, PC_NAME);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter);

        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                local.setBackgroundResource(R.drawable.button_click);
                pl.setBackgroundResource(R.color.blue);
                asmbly.setBackgroundResource(R.color.blue);
                status = 0;
                al.clear();
                reload(state);
            }
        });

        pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pl.setBackgroundResource(R.drawable.button_click);
                local.setBackgroundResource(R.color.blue);
                asmbly.setBackgroundResource(R.color.blue);
                status = 2;
                al.clear();
                reload(state);
            }
        });

        asmbly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asmbly.setBackgroundResource(R.drawable.button_click);
                pl.setBackgroundResource(R.color.blue);
                local.setBackgroundResource(R.color.blue);
                status = 1;
                al.clear();
                reload(state);
            }
        });

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
                }
                else if (i == R.id.live) {
                  dl.closeDrawer(Gravity.START);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Current_Results.this);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (sp2.getSelectedItem() != null) {
            constt = sp2.getSelectedItem().toString();
            fetch();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void reload(String st) {
        if (status == 0) {
            al.clear();
            tab.removeAllViews();
            rf.child("Constituencies").child(st).child("Local").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        Firebase_Constituencies c1 = dsp.getValue(Firebase_Constituencies.class);
                        if (c1.getDate() != null)
                            al.add(c1.getConstituency());

                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, al);

                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp2.setAdapter(adapter2);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

        } else if (status == 1) {
            al.clear();
            tab.removeAllViews();
            rf.child("Constituencies").child(st).child("Assembly").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        Firebase_Constituencies c1 = dsp.getValue(Firebase_Constituencies.class);
                        if (c1.getDate() != null)
                            al.add(c1.getConstituency());

                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, al);

                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp2.setAdapter(adapter2);

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        } else if (status == 2) {
            al.clear();
            tab.removeAllViews();
            rf.child("Constituencies").child(st).child("Parliament").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        Firebase_Constituencies c1 = dsp.getValue(Firebase_Constituencies.class);
                        if (c1.getDate() != null)
                            al.add(c1.getConstituency());

                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, al);

                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp2.setAdapter(adapter2);

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }

    }

    void fetch() {
        tab.removeAllViews();
        tab.addView(head);
        if (status == 0) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            String t = cal.getTime().toString();
            int i1 = Integer.parseInt(t.substring(11, 13));
            rf1.child(state).child("Local").child(constt).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    tab.removeAllViews();
                    tab.addView(head);

                    try {
                        a = new int[Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))];
                        int i = 0;
                        for (DataSnapshot dsp : snapshot.getChildren()) {
                            Firebase_Candidates obj = dsp.getValue(Firebase_Candidates.class);


                            a[i] = Integer.parseInt(obj.getVotes());
                            i++;
                        }
                    } catch (NullPointerException ne) {

                    } catch(ArrayIndexOutOfBoundsException ae)
                    {

                    }
                    try {
                        Arrays.sort(a);
                        high = a[a.length - 1];

                        for (DataSnapshot dsp : snapshot.getChildren()) {
                            final Firebase_Candidates obj = dsp.getValue(Firebase_Candidates.class);
                            {
                                for (int k = 1; k < (int) snapshot.getChildrenCount() - 1; k++) {
                                    if (high == Integer.parseInt(obj.getVotes())) {
                                        TableRow row = new TableRow(Current_Results.this);
                                        row.setBackgroundResource(R.drawable.card2);
                                        TextView party = new TextView(Current_Results.this);
                                        TextView name = new TextView(Current_Results.this);
                                        ImageView symbol = new ImageView(Current_Results.this);
                                        TextView sub = new TextView(Current_Results.this);

                                        sub.setText("Votes");
                                        sub.setText(obj.getVotes());
                                        sub.setWidth(100);
                                        sub.setHeight(100);
                                        sub.setTextColor(Color.BLACK);

                                        sub.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                        sub.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                        sub.setGravity(Gravity.CENTER);

                                        party.setWidth(150);
                                        name.setWidth(150);
                                        party.setTextColor(Color.BLACK);
                                        name.setTextColor(Color.BLACK);
                                        party.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                        name.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                        party.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                        name.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                        party.setGravity(Gravity.LEFT);

                                        name.setGravity(Gravity.RIGHT);

                                        party.setText(obj.getCandidate());

                                        name.setText(obj.getParty());
                                        Picasso.get().load(obj.getImg_URL()).into(symbol);


                                        row.addView(symbol);
                                        row.addView(party);
                                        row.addView(name);
                                        row.addView(sub);
                                        tab.addView(row);
                                    } else {
                                        TableRow row = new TableRow(Current_Results.this);
                                        row.setBackgroundResource(R.drawable.card1);
                                        TextView party = new TextView(Current_Results.this);
                                        TextView name = new TextView(Current_Results.this);
                                        ImageView symbol = new ImageView(Current_Results.this);
                                        TextView sub = new TextView(Current_Results.this);

                                        sub.setText("Votes");
                                        sub.setText(obj.getVotes());
                                        sub.setWidth(100);
                                        sub.setHeight(100);
                                        sub.setTextColor(Color.BLACK);

                                        sub.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                        sub.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                        sub.setGravity(Gravity.CENTER);

                                        party.setWidth(150);
                                        name.setWidth(150);
                                        party.setTextColor(Color.BLACK);
                                        name.setTextColor(Color.BLACK);
                                        party.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                        name.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                        party.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                        name.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                        party.setGravity(Gravity.LEFT);

                                        name.setGravity(Gravity.RIGHT);

                                        party.setText(obj.getCandidate());

                                        name.setText(obj.getParty());
                                        Picasso.get().load(obj.getImg_URL()).into(symbol);


                                        row.addView(symbol);
                                        row.addView(party);
                                        row.addView(name);
                                        row.addView(sub);
                                        tab.addView(row);
                                    }
                                }

                            }

                        }

                    } catch (NullPointerException ne) {
                        Toast.makeText(getApplicationContext(), "No Current Assembly Election Is Going On", Toast.LENGTH_SHORT).show();
                    } catch(ArrayIndexOutOfBoundsException ae)
                    {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (status == 1) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            String t = cal.getTime().toString();
            int i1 = Integer.parseInt(t.substring(11, 13));

                rf1.child(state).child("Assembly").child(constt).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tab.removeAllViews();
                        tab.addView(head);

                        try {
                            a = new int[Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))];
                            int i = 0;
                            for (DataSnapshot dsp : snapshot.getChildren()) {
                                Firebase_Candidates obj = dsp.getValue(Firebase_Candidates.class);


                                a[i] = Integer.parseInt(obj.getVotes());
                                i++;
                            }
                        } catch (NullPointerException ne) {

                        } catch(ArrayIndexOutOfBoundsException ae)
                        {

                        }
                        try {
                            Arrays.sort(a);
                            high = a[a.length - 1];

                            for (DataSnapshot dsp : snapshot.getChildren()) {
                                final Firebase_Candidates obj = dsp.getValue(Firebase_Candidates.class);
                                {
                                    for (int k = 1; k < (int) snapshot.getChildrenCount() - 1; k++) {
                                        if (Integer.parseInt(obj.getVotes()) == high) {
                                            TableRow row = new TableRow(Current_Results.this);
                                            row.setBackgroundResource(R.drawable.card2);
                                            TextView party = new TextView(Current_Results.this);
                                            TextView name = new TextView(Current_Results.this);
                                            ImageView symbol = new ImageView(Current_Results.this);
                                            TextView sub = new TextView(Current_Results.this);

                                            sub.setText("Votes");
                                            sub.setText(obj.getVotes());
                                            sub.setWidth(100);
                                            sub.setHeight(100);
                                            sub.setTextColor(Color.BLACK);

                                            sub.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                            sub.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                            sub.setGravity(Gravity.CENTER);

                                            party.setWidth(150);
                                            name.setWidth(150);
                                            party.setTextColor(Color.BLACK);
                                            name.setTextColor(Color.BLACK);
                                            party.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                            name.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                            party.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                            name.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                            party.setGravity(Gravity.LEFT);

                                            name.setGravity(Gravity.RIGHT);

                                            party.setText(obj.getCandidate());

                                            name.setText(obj.getParty());
                                            Picasso.get().load(obj.getImg_URL()).into(symbol);


                                            row.addView(symbol);
                                            row.addView(party);
                                            row.addView(name);
                                            row.addView(sub);
                                            tab.addView(row);

                                        } else {
                                            TableRow row = new TableRow(Current_Results.this);
                                            row.setBackgroundResource(R.drawable.card1);
                                            TextView party = new TextView(Current_Results.this);
                                            TextView name = new TextView(Current_Results.this);
                                            ImageView symbol = new ImageView(Current_Results.this);
                                            TextView sub = new TextView(Current_Results.this);

                                            sub.setText("Votes");
                                            sub.setText(obj.getVotes());
                                            sub.setWidth(100);
                                            sub.setHeight(100);
                                            sub.setTextColor(Color.BLACK);

                                            sub.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                            sub.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                            sub.setGravity(Gravity.CENTER);

                                            party.setWidth(150);
                                            name.setWidth(150);
                                            party.setTextColor(Color.BLACK);
                                            name.setTextColor(Color.BLACK);
                                            party.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                            name.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                            party.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                            name.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                            party.setGravity(Gravity.LEFT);

                                            name.setGravity(Gravity.RIGHT);

                                            party.setText(obj.getCandidate());

                                            name.setText(obj.getParty());
                                            Picasso.get().load(obj.getImg_URL()).into(symbol);


                                            row.addView(symbol);
                                            row.addView(party);
                                            row.addView(name);
                                            row.addView(sub);
                                            tab.addView(row);

                                        }
                                    }
                                }

                            }

                        } catch (
                                NullPointerException ne) {
                            Toast.makeText(getApplicationContext(), "No Current Assembly Election Is Going On", Toast.LENGTH_SHORT).show();
                        } catch(ArrayIndexOutOfBoundsException ae)
                        {

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        } else if (status == 2) {
            rf1.child(state).child("Parliament").child(constt).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    tab.removeAllViews();
                    tab.addView(head);

                    try {
                        a = new int[Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))];
                        int i = 0;
                        for (DataSnapshot dsp : snapshot.getChildren()) {
                            Firebase_Candidates obj = dsp.getValue(Firebase_Candidates.class);


                            a[i] = Integer.parseInt(obj.getVotes());
                            i++;
                        }
                    } catch (NullPointerException ne) {

                    } catch(ArrayIndexOutOfBoundsException ae)
                    {

                    }
                    try {
                        Arrays.sort(a);
                        high = a[a.length - 1];
                        for (DataSnapshot dsp : snapshot.getChildren()) {
                            final Firebase_Candidates obj = dsp.getValue(Firebase_Candidates.class);
                            {
                                for (int k = 1; k < (int) snapshot.getChildrenCount() - 1; k++) {
                                    if (high == Integer.parseInt(obj.getVotes())) {
                                        TableRow row = new TableRow(Current_Results.this);
                                        row.setBackgroundResource(R.drawable.card2);
                                        TextView party = new TextView(Current_Results.this);
                                        TextView name = new TextView(Current_Results.this);
                                        ImageView symbol = new ImageView(Current_Results.this);
                                        TextView sub = new TextView(Current_Results.this);

                                        sub.setText("Votes");
                                        sub.setText(obj.getVotes());
                                        sub.setWidth(100);
                                        sub.setHeight(100);
                                        sub.setTextColor(Color.BLACK);

                                        sub.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                        sub.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                        sub.setGravity(Gravity.CENTER);

                                        party.setWidth(150);
                                        name.setWidth(150);
                                        party.setTextColor(Color.BLACK);
                                        name.setTextColor(Color.BLACK);
                                        party.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                        name.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                        party.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                        name.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                        party.setGravity(Gravity.LEFT);

                                        name.setGravity(Gravity.RIGHT);

                                        party.setText(obj.getCandidate());

                                        name.setText(obj.getParty());
                                        Picasso.get().load(obj.getImg_URL()).into(symbol);


                                        row.addView(symbol);
                                        row.addView(party);
                                        row.addView(name);
                                        row.addView(sub);
                                        tab.addView(row);
                                    }
                                    else {
                                        TableRow row = new TableRow(Current_Results.this);
                                        row.setBackgroundResource(R.drawable.card1);
                                        TextView party = new TextView(Current_Results.this);
                                        TextView name = new TextView(Current_Results.this);
                                        ImageView symbol = new ImageView(Current_Results.this);
                                        TextView sub = new TextView(Current_Results.this);

                                        sub.setText("Votes");
                                        sub.setText(obj.getVotes());
                                        sub.setWidth(100);
                                        sub.setHeight(100);
                                        sub.setTextColor(Color.BLACK);

                                        sub.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                        sub.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                        sub.setGravity(Gravity.CENTER);

                                        party.setWidth(150);
                                        name.setWidth(150);
                                        party.setTextColor(Color.BLACK);
                                        name.setTextColor(Color.BLACK);
                                        party.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                        name.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                                        party.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                        name.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                        party.setGravity(Gravity.LEFT);

                                        name.setGravity(Gravity.RIGHT);

                                        party.setText(obj.getCandidate());

                                        name.setText(obj.getParty());
                                        Picasso.get().load(obj.getImg_URL()).into(symbol);


                                        row.addView(symbol);
                                        row.addView(party);
                                        row.addView(name);
                                        row.addView(sub);
                                        tab.addView(row);
                                    }
                                }

                            }

                        }

                    } catch (NullPointerException ne) {
                        Toast.makeText(getApplicationContext(), "No Current Assembly Election Is Going On", Toast.LENGTH_SHORT).show();
                    }
                    catch(ArrayIndexOutOfBoundsException ae)
                    {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

}