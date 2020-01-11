package com.pmo.sewaapp;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.sewaapp.models.usermodel;
import com.pmo.sewaapp.penyedia.Penyedia;
import com.pmo.sewaapp.penyewa.Penyewa;


public class FullscreenActivity extends AppCompatActivity {
    //TODO:: inisiasi kebutuhan firebase (firbase authentication,realtime database,firebase storage)
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private com.pmo.sewaapp.models.usermodel usermodel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        //TODO :: Cek apakah firebase auth nya sudah login ?
        if(firebaseAuth.getUid()!=null  ){
            //sudah login dan di cek ke database datanya ada atau tidak
            databaseReference.child(globalval.TABLE_USER)
                    .child(firebaseAuth.getUid())
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String nama = dataSnapshot.child("nama").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String hp = dataSnapshot.child("nohp").getValue(String.class);
                        String level = dataSnapshot.child("level").getValue(String.class);
                        if(nama == null || email == null || hp == null || level == null){
                            //jika kosong akan diarahkan mengisi Biodata
                            startActivity(new Intent(FullscreenActivity.this,LengkapiBiodataActivity.class));
                            finish();
                        }else {
                            //ijka tidak kosong akan lanjut ke Activity Home
                            startActivity(new Intent(FullscreenActivity.this, Penyedia.class));
                            finish();
                        }
                    }else {
                        //jika kosong akan diarahkan mengisi Biodata
                        Toast.makeText(FullscreenActivity.this,"Tidak ada satupun data",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(FullscreenActivity.this,LengkapiBiodataActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(FullscreenActivity.this,"database error"+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(FullscreenActivity.this,Register.class));
                }
            },200);
        }


    }
}
