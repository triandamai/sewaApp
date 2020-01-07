package com.pmo.sewaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailTransaksi extends AppCompatActivity {

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
    private Context context = DetailTransaksi.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent.hasExtra("idTransaksi")){

        }else {
            Toast.makeText(context,"Data Tidak ada",Toast.LENGTH_LONG).show();
        }
    }

    @OnClick({R.id.iv_gambar_bukti, R.id.btn_selesai, R.id.btn_tolak, R.id.btn_terima, R.id.ll_buttonaksi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_gambar_bukti:
                break;
            case R.id.btn_selesai:
                break;
            case R.id.btn_tolak:
                break;
            case R.id.btn_terima:
                break;
            case R.id.ll_buttonaksi:
                break;
        }
    }
}
