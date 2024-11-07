package com.example.accessapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Upload_poster extends AppCompatActivity {

    CardView uploadImg ;
    final int REQ =1;
    Bitmap bitmap;
    ImageView posimgView;
    String downloadUri ="";

    EditText posterTitle;
    Button posbtn;

    DatabaseReference reference,dbref;
    StorageReference storageReference;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_poster);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pd = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        posterTitle =findViewById(R.id.posterTitle);
        posbtn = findViewById(R.id.posterbtn);

        uploadImg=findViewById(R.id.uploadImage);
        posimgView=findViewById(R.id.posimgView);


        uploadImg.setOnClickListener(view -> openGallary());

        posbtn.setOnClickListener(view -> {
            if(posterTitle.getText().toString().isEmpty())
            {
                posterTitle.setError("Empty");
                posterTitle.requestFocus();
            } else if (bitmap == null) {
                uploadData();
            }
            else {
                uploadImg();
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
        filepath = storageReference.child("Notice").child(finalImg +"jpg");
        final UploadTask uploadTask = filepath.putBytes(finalImg);
       uploadTask.addOnCompleteListener(Upload_poster.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                Toast.makeText(Upload_poster.this, "Somthing Went wrong 111", Toast.LENGTH_SHORT).show();
            }
           }
       });


    }

    private void uploadData() {

        dbref = reference.child("Notice");
        final String uq = dbref.push().getKey();

        String title = posterTitle.getText().toString();

        Calendar calfordate =  Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MM-yy");
        String date = currentdate.format(calfordate.getTime());


        Calendar calfortime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("hh-mm a");
        String time = currenttime.format(calfortime.getTime());

        NoticeData noticeData = new NoticeData(title,downloadUri,date,time,uq);
        reference.child(uq).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(Upload_poster.this, "Data uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(Upload_poster.this, "somthing went wrong", Toast.LENGTH_SHORT).show();

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
            posimgView.setImageBitmap(bitmap);
        }
    }
}