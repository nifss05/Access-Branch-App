package com.example.accessapp.addMembers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.accessapp.NoticeData;
import com.example.accessapp.R;
import com.example.accessapp.Upload_poster;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddMember extends AppCompatActivity {

    ImageView Addmemberimg;
    EditText addmembername,addmemberEmail,addmemberpost;
    Button addmemberbtn;
    final int REQ =1;
    Bitmap bitmap=null;
    private String name,email,post,downloadUri="";
    ProgressDialog pd;
    DatabaseReference reference,dbref;
    StorageReference storageReference;
    String p="";


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

        reference= FirebaseDatabase.getInstance().getReference().child("members");
        storageReference = FirebaseStorage.getInstance().getReference().child("MEMBERimg");
        pd= new ProgressDialog(this);

        Addmemberimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openGallary();
            }
        });

        addmemberbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = addmembername.getText().toString();
                String email = addmemberEmail.getText().toString();
                String post = addmemberpost.getText().toString();
                if(name.isEmpty())
                {
                    addmembername.setError("Empty!");
                    addmembername.requestFocus();
                }else if(email.isEmpty())
                {
                    addmemberEmail.setError("Empty!");
                    addmemberEmail.requestFocus();
                }else if(post.isEmpty())
                {
                    addmemberpost.setError("Empty!");
                    addmemberpost.requestFocus();
                } else if (bitmap == null) {
                    Toast.makeText(AddMember.this, "Add Image !!", Toast.LENGTH_SHORT).show();
                }else {
                    uploadImg();
                }
            }
        });



    }

     private void uploadData() {

        String name = addmembername.getText().toString();
        String email = addmemberEmail.getText().toString();
        String post = addmemberpost.getText().toString();

         dbref = reference.child("MEMBERS");
         final String uq = dbref.push().getKey();

        MemberData memberData = new MemberData(name,email,post,downloadUri,uq);
        dbref.child(uq).setValue(memberData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(AddMember.this, "Data uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddMember.this, "somthing went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void uploadImg() {
        pd.setMessage("Uploading....");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImg = baos.toByteArray();

        final StorageReference filepath;
        filepath = storageReference.child(finalImg +"jpg");
        final UploadTask uploadTask = filepath.putBytes(finalImg);
        uploadTask.addOnCompleteListener(AddMember.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUri = String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(AddMember.this, "Somthing Went wrong !!!", Toast.LENGTH_SHORT).show();
                }
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