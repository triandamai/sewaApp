package com.pmo.sewaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pmo.sewaapp.models.barangmodel;
import com.pmo.sewaapp.models.transaksimodel;
import com.pmo.sewaapp.models.usermodel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailTransaksi extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 22;
    @BindView(R.id.iv_gambar_bukti)
    ImageView ivGambarBukti;
    @BindView(R.id.tv_namaPenyewa)
    TextView tvNamaPenyewa;
    @BindView(R.id.tv_alamat_penyewa)
    TextView tvAlamatPenyewa;
    @BindView(R.id.tv_harga_sewa)
    TextView tvHargaSewa;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.btn_selesai)
    Button btnSelesai;
    @BindView(R.id.btn_tolak)
    Button btnTolak;
    @BindView(R.id.btn_terima)
    Button btnTerima;
    @BindView(R.id.ll_buttonaksi)
    LinearLayout llButtonaksi;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    private Context context = DetailTransaksi.this;
    private String idTransaksi ;
    private transaksimodel transaksimodel;
    private usermodel usermodel;
    private Uri filePath ;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor ;
    private String TAG = "Error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);
        ButterKnife.bind(this);
        mSettings = getSharedPreferences("transaksi", Context.MODE_PRIVATE);
        editor = mSettings.edit();
        Intent intent = getIntent();
        if(intent.hasExtra("idTransaksi")){
            this.idTransaksi = intent.getStringExtra("idTransaksi");
           // Toast.makeText(context,intent.getStringExtra("idTransaksi"),Toast.LENGTH_LONG).show();
            editor.putString("idTransaksi",idTransaksi);
            editor.apply();
            fetchData();
        }else {
            Toast.makeText(context,"Data Tidak ada",Toast.LENGTH_LONG).show();
            finish();
        }
    }
    public void fetchData(){
        //ambil data detail barang
        databaseReference.child(globalval.TABLE_TRANSAKSI).child(this.idTransaksi).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    transaksimodel = new transaksimodel();
                    transaksimodel = dataSnapshot.getValue(transaksimodel.class);
                    transaksimodel.setIdTransaksi(dataSnapshot.getKey());
                    assert transaksimodel != null;
                    ivGambarBukti.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (transaksimodel.getIDTOKO().equals(firebaseAuth.getCurrentUser().getUid())){

                            }else {
                                SelectImage();
                            }
                        }
                    });
                    if (transaksimodel.getIDTOKO().equals(firebaseAuth.getCurrentUser().getUid())){
                        llButtonaksi.setVisibility(View.VISIBLE);

                    }else {
                        llButtonaksi.setVisibility(View.GONE);
                    }
                    tvAlamatPenyewa.setText(transaksimodel.getAlamat());
                    tvHargaSewa.setText(transaksimodel.getHarga());
                    tvStatus.setText(transaksimodel.getStatus());
                    if(transaksimodel.getBuktipembayaran().equals("1")){
                        //belum
                        ivGambarBukti.setImageResource(R.drawable.barang_kosong);
                    }else if(transaksimodel.getBuktipembayaran().equals("2")){
                        ivGambarBukti.setImageResource(R.drawable.barang_kosong);
                        //sudah kirim
                    }else if(transaksimodel.getBuktipembayaran() == null){
                        ivGambarBukti.setImageResource(R.drawable.barang_kosong);
                        //kosong
                    }else {
                        Picasso.get().load(transaksimodel.getBuktipembayaran()).into(ivGambarBukti);
                    }
                    databaseReference.child(globalval.TABLE_USER).child(transaksimodel.getIDPENYEWA()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                usermodel = dataSnapshot.getValue(usermodel.class);
                                assert usermodel != null;
                                tvNamaPenyewa.setText(usermodel.getNama());

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //upload
    public void upload(){

            if (filePath != null) {

                // Code for showing progressDialog while uploading
                ProgressDialog progressDialog
                        = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                String id = databaseReference.push().getKey();
                //
                StorageReference myref = storageReference.child(globalval.TABLE_TRANSAKSI).child("images/" + id);
                ivGambarBukti.setDrawingCacheEnabled(true);
                ivGambarBukti.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable)ivGambarBukti.getDrawable()).getBitmap();
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


                            databaseReference
                                    .child(globalval.TABLE_TRANSAKSI)
                                    .child(mSettings.getString("idTransaksi","null"))
                                    .child("buktipembayaran")
                                    .setValue(donloadUri);
                            progressDialog.dismiss();
                            Toast.makeText(context,task.getException().toString(),Toast.LENGTH_LONG).show();

                        }else {

                            Toast.makeText(context,"gagal",Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG,e.getMessage());
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                });

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
                ivGambarBukti.setImageBitmap(bitmap);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        upload();
                    }
                },200);

            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
    @OnClick({R.id.iv_gambar_bukti, R.id.btn_selesai, R.id.btn_tolak, R.id.btn_terima, R.id.ll_buttonaksi})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_selesai:
                databaseReference.child(globalval.TABLE_TRANSAKSI).child(idTransaksi).child("status").setValue("selesai");
                break;
            case R.id.btn_tolak:
                databaseReference.child(globalval.TABLE_TRANSAKSI).child(idTransaksi).child("status").setValue("ditolak");
                break;
            case R.id.btn_terima:
                databaseReference.child(globalval.TABLE_TRANSAKSI).child(idTransaksi).child("status").setValue("terima");
                break;
            case R.id.ll_buttonaksi:

                break;
        }
    }
}
