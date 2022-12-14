package com.example.projectwecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class attendnurse extends AppCompatActivity {

    RecyclerView recyclerView;
    attendnurseviewadapter adapter;
    ArrayList<Allnurseview> list;
    TextView viewtxt;
    ProgressBar progressBar;
    ArrayList<String> ids;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), adminMain.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendnurse);

        viewtxt = findViewById(R.id.viewText3);
        progressBar = findViewById(R.id.progressBar19);
        recyclerView = findViewById(R.id.attendListNur);

        ids = new ArrayList<String>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new attendnurseviewadapter(this, list);
        recyclerView.setAdapter(adapter);

        nextdate();
        currentdate();
        //check();
    }
    private void currentdate() {

        String currentDate = new SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(new Date());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Attendance").child(currentDate);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String status = "in";
                    String status1 = dataSnapshot.child("status").getValue(String.class);
                    if (status1.equals(status)) {
                        String id = dataSnapshot.child("id").getValue(String.class);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Registered Nurses");
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    String newid = dataSnapshot1.child("nurseID").getValue(String.class);
                                    if (id.equals(newid)) {
                                        Allnurseview a = dataSnapshot1.getValue(Allnurseview.class);
                                        list.add(a);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    adapter.notifyDataSetChanged();
                                    //check();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void nextdate() {
        Date date = new Date();
        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        // format.format(calendar.getTime());

        String currentDate = new SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(calendar.getTime());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Attendance").child(currentDate);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String status = "in";
                    String status1 = dataSnapshot.child("status").getValue(String.class);

                    if (status1.equals(status)) {
                        String id = dataSnapshot.child("id").getValue(String.class);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Registered Nurses");
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    String newid = dataSnapshot1.child("nurseID").getValue(String.class);
                                    if (id.equals(newid)) {
                                        Allnurseview a = dataSnapshot1.getValue(Allnurseview.class);
                                        list.add(a);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void check() {
        if (list.size() > 0) {
            progressBar.setVisibility(View.INVISIBLE);
            viewtxt.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            viewtxt.setVisibility(View.VISIBLE);
        }
    }
}