package com.pmo.sewaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.sewaapp.models.barangmodel;
import com.pmo.sewaapp.models.tokomodel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailBarangActivity extends AppCompatActivity {

    @BindView(R.id.iv_gambar_barang)
    ImageView ivGambarBarang;
    @BindView(R.id.tv_namabarang)
    TextView tvNamabarang;
    @BindView(R.id.tv_namatoko)
    TextView tvNamatoko;
    @BindView(R.id.tv_stok)
    TextView tvStok;
    @BindView(R.id.tv_harga)
    TextView tvHarga;
    @BindView(R.id.btn_pesan)
    Button btnPesan;

    private Context context = DetailBarangActivity.this;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String idToko,idBarang;
    private tokomodel tokomodel = new tokomodel();
    private  barangmodel barangmodel = new barangmodel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(!intent.hasExtra("idToko") || !intent.hasExtra("idBarang")){
            Toast.makeText(context,"Gagal Mendapatkan data",Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },200);
        }else {
            this.idToko = intent.getStringExtra("idToko");
            this.idBarang = intent.getStringExtra("idBarang");
            fetchDataBarang();
            fetchDataToko();
        }
    }

    private void fetchDataBarang(){
        databaseReference.child(globalval.TABLE_BARANG).child(idToko).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    barangmodel = dataSnapshot.getValue(barangmodel.class);
                    assert barangmodel != null;
                    tvNamabarang.setText(barangmodel.getNama());
                    tvHarga.setText("Rp "+barangmodel.getHargasewa());
                    tvStok.setText(barangmodel.getStokasli());
                    if(idToko.equals(barangmodel.getIdtoko())){
                        btnPesan.setEnabled(false);
                    }else {
                        btnPesan.setEnabled(true);
                    }

                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void fetchDataToko(){
        databaseReference.child(globalval.TABLE_TOKO).child(idToko).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    tokomodel = dataSnapshot.getValue(tokomodel.class);
                    assert  tokomodel != null;
                    tvNamatoko.setText(tokomodel.getNamatoko().toString());
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btn_pesan)
    public void onViewClicked() {
        startActivity(new Intent(context,CheckoutPesanan.class).putExtra("idBarang",idBarang));

        Toast.makeText(context,barangmodel.getIdbarang(),Toast.LENGTH_LONG).show();
    }
}
