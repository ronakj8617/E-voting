package com.example.sharekhancalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class DownloadFirebase extends AppCompatActivity {
Button btn;
ImageView i1;

    StorageReference storageRef;
    FirebaseStorage storage;
    File localFile;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_firebase);

        btn=(Button)findViewById(R.id.btn);
        i1=(ImageView)findViewById(R.id.iv);
        progressDialog= new ProgressDialog(this);
        storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
         storageRef= storage.getReferenceFromUrl("gs://e-voting-ab239.appspot.com/new");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progressDialog.show();
                    download();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    void download() throws IOException {

       localFile = File.createTempFile("images", "png");


        storageRef.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                Bitmap bmp = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                i1.setImageBitmap(bmp);

                        //    load();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(DownloadFirebase.this, exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // progress percentage
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

            }
        });;

    }

}