package com.example.accessapp.addMembers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.accessapp.R;

import java.io.IOException;

public class AddMember extends AppCompatActivity {

    ImageView Addmemberimg;
    EditText addmembername,addmemberEmail,addmemberpost;
    Button addmemberbtn;
    final int REQ =1;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_member);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Addmemberimg = findViewById(R.id.Addmemberimg);
        addmembername=findViewById(R.id.addmembername);
        addmemberEmail=findViewById(R.id.addmemberEmail);
        addmemberpost=findViewById(R.id.addmemberpost);
        addmemberbtn=findViewById(R.id.addmemberBtn);

        Addmemberimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openGallary();
            }
        });



    }

    private void openGallary() {
        Intent pickImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImg,REQ);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode==RESULT_OK)
        {
            Uri uri =data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Addmemberimg.setImageBitmap(bitmap);
        }
    }
}