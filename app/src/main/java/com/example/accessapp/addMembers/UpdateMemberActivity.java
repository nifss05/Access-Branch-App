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

import com.example.accessapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateMemberActivity extends AppCompatActivity {

    private ImageView updateMemberImage;
    private EditText update_membername,update_memberemail,update_memberpost;
    private Button upMemberBtn,delMemberbtn;
    private String name,email,post,image;
    final int REQ =1;
    Bitmap bitmap=null;
    ProgressDialog pd;
    DatabaseReference reference;
    StorageReference storageReference;
    String downloadUri="",uq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_member2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        reference= FirebaseDatabase.getInstance().getReference().child("members");
        storageReference = FirebaseStorage.getInstance().getReference().child("MEMBERimg");
        pd= new ProgressDialog(this);
        updateMemberImage=findViewById(R.id.updateMemberImage);
        update_membername=findViewById(R.id.update_membername);
        update_memberemail=findViewById(R.id.update_memberemail);
        update_memberpost=findViewById(R.id.update_memberpost);
        upMemberBtn=findViewById(R.id.upMemberBtn);
        delMemberbtn=findViewById(R.id.delMemberbtn);

        uq = getIntent().getStringExtra("key");

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        post = getIntent().getStringExtra("post");
        image = getIntent().getStringExtra("image");

        try {
            Picasso.get().load(image).into(updateMemberImage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        update_membername.setText(name);
        update_memberemail.setText(email);
        update_memberpost.setText(post);

        updateMemberImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallary();
            }
        });
        
        upMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = update_membername.getText().toString();
                post = update_memberpost.getText().toString();
                email = update_memberemail.getText().toString();
                checkValidation();
                
            }
        });
        delMemberbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
                
            }
        });


    }

    private void deleteData() {
        reference.child("MEMBERS").child(uq).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UpdateMemberActivity.this, "Data deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateMemberActivity.this,UpdateMember.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateMemberActivity.this, "Somthing Went wrong !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkValidation() {

        if(name.isEmpty())
        {
            update_membername.setError("Empty");
            update_membername.requestFocus();
        } else if (email.isEmpty()) {
            update_memberemail.setError("Empty");
            update_memberemail.requestFocus();
        }else if (post.isEmpty())
        {
            update_memberpost.setError("Empty");
            update_memberpost.requestFocus();
        }else if (bitmap==null){
            uploadData(image);
        }
        else {
            uploadImg();
        }
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
        uploadTask.addOnCompleteListener(UpdateMemberActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    uploadData(downloadUri);
                                }
                            });
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(UpdateMemberActivity.this, "Somthing Went wrong !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void uploadData(String s) {

        HashMap hp = new HashMap();
        hp.put("name",name);
        hp.put("email",email);
        hp.put("post",post);
        hp.put("img",s);

        reference.child("MEMBERS").child(uq).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                pd.dismiss();
                Toast.makeText(UpdateMemberActivity.this, "Data updated ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateMemberActivity.this,UpdateMember.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateMemberActivity.this, "Somthing Went wrong !!!", Toast.LENGTH_SHORT).show();

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
            updateMemberImage.setImageBitmap(bitmap);
        }
    }
}