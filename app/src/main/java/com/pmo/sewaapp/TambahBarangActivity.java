package com.pmo.sewaapp;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pmo.sewaapp.models.barangmodel;
import com.pmo.sewaapp.models.tokomodel;
import com.pmo.sewaapp.penyedia.BuattokoActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TambahBarangActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 21;
    @BindView(R.id.iv_gambar)
    ImageView ivGambar;
    @BindView(R.id.et_Namabarang)
    EditText etNamabarang;
    @BindView(R.id.et_Kategori)
    EditText etKategori;
    @BindView(R.id.et_Stok)
    EditText etStok;
    @BindView(R.id.harga)
    EditText harga;
    @BindView(R.id.btn_simpan)
    Button btnSimpan;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    private String id = "unknown";
    private Context context = TambahBarangActivity.this;
    private Uri filePath ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra("id")){
            this.id = intent.getStringExtra("id");
            fetchEdit();
        }else {
            this.id = databaseReference.push().getKey();
        }
    }
    public void fetchEdit(){
        databaseReference.child(globalval.TABLE_BARANG).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    barangmodel barangmodel = new barangmodel();
                    barangmodel = dataSnapshot.getValue(barangmodel.class);
                    etKategori.setText(barangmodel.getKategori());
                    etNamabarang.setText(barangmodel.getNama());
                    harga.setText(barangmodel.getHargasewa());
                }else {
                    Toast.makeText(context,"Barang Tdak ada",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"database Error"+databaseError,Toast.LENGTH_LONG).show();
            }
        });
    }
    //upload
    public void upload(){
        if(cek()){
            if (filePath != null) {

                // Code for showing progressDialog while uploading
                ProgressDialog progressDialog
                        = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                //
                StorageReference myref = storageReference.child(globalval.TABLE_BARANG).child("images/" + id);
                ivGambar.setDrawingCacheEnabled(true);
                ivGambar.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable)ivGambar.getDrawable()).getBitmap();
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

                            barangmodel barangmodel = new barangmodel();
                            barangmodel.setGambar(donloadUri.toString());
                            barangmodel.setHargasewa(harga.getText().toString());
                            barangmodel.setIdbarang(id);
                            barangmodel.setKategori(etKategori.getText().toString());
                            barangmodel.setIdtoko(firebaseAuth.getCurrentUser().getUid());
                            barangmodel.setNama(etNamabarang.getText().toString());
                            barangmodel.setStokasli(etStok.getText().toString());
                            barangmodel.setStoktersedia(etStok.getText().toString());
                            barangmodel.setWaktuditambah(new Date().getTime());

                            databaseReference
                                    .child(globalval.TABLE_BARANG)
                                    .child(id)
                                    .setValue(barangmodel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context,"Berhasil",Toast.LENGTH_LONG).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            onBackPressed();
                                        }
                                    },400);
                                }
                            });
                        }else {
                          Toast.makeText(context,"gagal",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }else {
            Toast.makeText(context,"Mohon Isi Semua",Toast.LENGTH_LONG).show();
        }
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
                ivGambar.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
    public boolean cek(){
        return !etKategori.getText().toString().isEmpty() || !etNamabarang.getText().toString().isEmpty() || !etStok.getText().toString().isEmpty()  || !harga.getText().toString().isEmpty();
    }

    @OnClick({R.id.iv_gambar, R.id.btn_simpan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_gambar:
                SelectImage();
                break;
            case R.id.btn_simpan:
                upload();
                break;
        }
    }
}
