package com.pmo.sewaapp.penyedia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pmo.sewaapp.R;
import com.pmo.sewaapp.globalval;
import com.pmo.sewaapp.models.tokomodel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuattokoActivty extends AppCompatActivity {

    @BindView(R.id.iv_prev_banner)
    ImageView ivPrevBanner;
    @BindView(R.id.et_namaToko)
    EditText etNamaToko;
    @BindView(R.id.et_pemilik)
    EditText etPemilik;
    @BindView(R.id.et_alamatToko)
    EditText etAlamatToko;
    @BindView(R.id.btn_simpan)
    Button btnSimpan;
    @BindView(R.id.inputText)
    ScrollView inputText;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buattoko_activty);
        ButterKnife.bind(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                ivPrevBanner.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            //
            StorageReference myref = storageReference.child(globalval.TABLE_TOKO).child("images/" + firebaseAuth.getCurrentUser().getUid().toString());
           ivPrevBanner.setDrawingCacheEnabled(true);
           ivPrevBanner.buildDrawingCache();
           Bitmap bitmap = ((BitmapDrawable)ivPrevBanner.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = myref.putBytes(data);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return myref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri donloadUri = task.getResult();
                        tokomodel tokomodel = new tokomodel();
                        tokomodel.setBannertoko(donloadUri.toString());
                        tokomodel.setIdtoko(firebaseAuth.getCurrentUser().getUid());
                        tokomodel.setPemiliktoko(etPemilik.getText().toString());
                        tokomodel.setNamatoko(etNamaToko.getText().toString());
                        databaseReference.child(globalval.TABLE_TOKO).child(firebaseAuth.getCurrentUser().getUid()).setValue(tokomodel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(BuattokoActivty.this,"Berhasil",Toast.LENGTH_LONG).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        onBackPressed();
                                    }
                                },400);
                            }
                        });
                    }else {
                        Toast.makeText(BuattokoActivty.this,"gagal",Toast.LENGTH_LONG).show();
                    }
                }
            });



            }
        }
     public boolean cek(){
        return !etAlamatToko.getText().toString().isEmpty() || !etNamaToko.getText().toString().isEmpty() || !etPemilik.getText().toString().isEmpty();
     }
        @OnClick({R.id.iv_prev_banner, R.id.btn_simpan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_prev_banner:
                SelectImage();
                break;
            case R.id.btn_simpan:
                if(cek()){
                    uploadImage();
                }else {
                    Toast.makeText(BuattokoActivty.this,"Harap Isi semua",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
