package com.pmo.sewaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.sewaapp.models.usermodel;
import com.pmo.sewaapp.penyedia.Penyedia;

import java.sql.Timestamp;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LengkapiBiodataActivity extends AppCompatActivity {

    @BindView(R.id.et_Nama)
    EditText etNama;
    @BindView(R.id.et_Email)
    EditText etEmail;
    @BindView(R.id.et_Nohp)
    EditText etNohp;
    @BindView(R.id.et_Alamat)
    EditText etAlamat;
    @BindView(R.id.btn_simpan)
    Button btnSimpan;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lengkapi_biodata);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent.hasExtra("uid")){
            setVAl();
        }
    }

    private void setVAl() {
        databaseReference.child(globalval.TABLE_USER).child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    usermodel model = new usermodel();
                    model = dataSnapshot.getValue(usermodel.class);
                    etNama.setText(String.valueOf(model.getNama()));
                    etNohp.setText(String.valueOf(model.nohp));
                    etEmail.setText(String.valueOf(model.getEmail()));
                    etAlamat.setText(String.valueOf(model.getAlamat()));
                }else {
                    Toast.makeText(LengkapiBiodataActivity.this,"TIdak ada apapun",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LengkapiBiodataActivity.this,"Database Eror "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.btn_simpan)
    public void onViewClicked() {
        long time = new Date().getTime();
        if(cek()){
            usermodel user = new usermodel(
                    firebaseAuth.getUid().toString(),
                    etNama.getText().toString(),
                    etNohp.getText().toString(),
                    etAlamat.getText().toString(),
                    etEmail.getText().toString(),
                    "User",
                     time);
            databaseReference.child(globalval.TABLE_USER).child(firebaseAuth.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(LengkapiBiodataActivity.this, Penyedia.class));
                    finish();
                }
            });
        }
    }

    private boolean cek(){
        return !etAlamat.getText().toString().isEmpty() || !etEmail.getText().toString().isEmpty() || !etNohp.getText().toString().isEmpty() || !etNama.getText().toString().isEmpty();
    }
}
