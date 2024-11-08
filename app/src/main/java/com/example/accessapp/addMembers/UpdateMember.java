package com.example.accessapp.addMembers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accessapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateMember extends AppCompatActivity {

    FloatingActionButton fab;

    private RecyclerView acceessmemberList;
    private LinearLayout nodatafound;
    private List<MemberData> list;
    private DatabaseReference reference,dbref;

    AddMemberAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_member);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fab=findViewById(R.id.fab);
        acceessmemberList=findViewById(R.id.acceessmemberList);
        nodatafound=findViewById(R.id.nodatafound);
        reference= FirebaseDatabase.getInstance().getReference().child("members");

        memebers();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateMember.this,AddMember.class);
                startActivity(intent);

            }
        });
    }

    private void memebers() {
        dbref = reference.child("MEMBERS");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                if(!snapshot.exists())
                {
                    nodatafound.setVisibility(View.VISIBLE);
                    acceessmemberList.setVisibility(View.GONE);
                }
                else {
                    nodatafound.setVisibility(View.GONE);
                    acceessmemberList.setVisibility(View.VISIBLE);
                    for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        MemberData data = dataSnapshot.getValue(MemberData.class);
                        list.add(data);
                    }
                    acceessmemberList.setHasFixedSize(true);
                    acceessmemberList.setLayoutManager(new LinearLayoutManager(UpdateMember.this));
                    adapter = new AddMemberAdapter(list,UpdateMember.this);
                    acceessmemberList.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateMember.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}