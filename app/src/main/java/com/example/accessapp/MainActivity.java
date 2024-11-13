package com.example.accessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.accessapp.addMembers.UpdateMember;
import com.example.accessapp.deleteNotice.DeleteNoticeActivity;
import com.example.accessapp.deleteNotice.Upload_notice;

public class MainActivity extends AppCompatActivity {

    CardView addPos;
    CardView addPhoto;
    CardView addmember;
    CardView delete;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addPos=findViewById(R.id.addposter);
        addPhoto=findViewById(R.id.addphoto);
        addmember=findViewById(R.id.addmembers);
        delete=findViewById(R.id.delete);


        addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, UpdateMember.class);
                startActivity(intent);
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this,UploadGallaryImg.class);
                startActivity(intent);
            }
        });
        addPos.setOnClickListener(view -> {
            intent = new Intent(MainActivity.this, Upload_notice.class);
            startActivity(intent);
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, DeleteNoticeActivity.class);
                startActivity(intent);
            }
        });
    }
}