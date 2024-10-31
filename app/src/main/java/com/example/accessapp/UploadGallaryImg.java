package com.example.accessapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class UploadGallaryImg extends AppCompatActivity {

    Spinner Gcatagory;
    Button uploadGbtn;
    CardView uploadGimg;
    ImageView GimgView;
    Bitmap bitmap;
    String category;
    final int req=1;

    ProgressDialog pd;
    StorageReference storageReference;
    String downloadUri;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_gallary_img);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Gcatagory =findViewById(R.id.Gcategory);
        uploadGimg =findViewById(R.id.uploadGimg);
        uploadGbtn =findViewById(R.id.gallarybtn);
        GimgView =findViewById(R.id.GalimgView);
        pd = new ProgressDialog(this);

        storageReference= FirebaseStorage.getInstance().getReference().child("gallery");
        reference= FirebaseDatabase.getInstance().getReference().child("gallery");


        String[] items = new String[]{"Select Category","Placements","Events","Freshers","farewell"};
        Gcatagory.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items));

        Gcatagory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = Gcatagory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        uploadGimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallary();
            }
        });

        uploadGbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmap == null)
                {
                    Toast.makeText(UploadGallaryImg.this, "Upload a image ", Toast.LENGTH_SHORT).show();
                }
                else if(category.equals("Select Category"))
                {
                    Toast.makeText(UploadGallaryImg.this, "Select a category", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadImg();
                }
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
        uploadTask.addOnCompleteListener(UploadGallaryImg.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(UploadGallaryImg.this, "Somthing Went wrong 111", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void uploadData() {

        reference = reference.child(category);
       final String uq = reference.push().getKey();
       // GalleryFirebase FG = new GalleryFirebase(downloadUri,uq);
        reference.child(uq).setValue(downloadUri).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(UploadGallaryImg.this, "Data uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadGallaryImg.this, "somthing went wrong", Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void openGallary() {
        Intent pickImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImg,req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==req && resultCode==RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            GimgView.setImageBitmap(bitmap);
        }
    }
}