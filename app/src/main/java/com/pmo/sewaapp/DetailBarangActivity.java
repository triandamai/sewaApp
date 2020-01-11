package com.pmo.sewaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailBarangActivity extends AppCompatActivity {

    /*
     * Membuat view binding (Pengganti findview by id ) dengan BUtter knife
     * */
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
    @BindView(R.id.btn_hapus)
    Button btnHapus;
    @BindView(R.id.btn_ubah)
    Button btnUbah;
    @BindView(R.id.lladaedit)
    LinearLayout lladaedit;

    //TODO:: inisiasi kebutuhan firebase (firbase authentication,realtime database,firebase storage)
    private Context context = DetailBarangActivity.this;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String idToko, idBarang;
    private tokomodel tokomodel = new tokomodel();
    private barangmodel barangmodel = new barangmodel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (!intent.hasExtra("idToko") || !intent.hasExtra("idBarang")) {
            Toast.makeText(context, "Gagal Mendapatkan data", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 200);
        } else {
            idToko = intent.getStringExtra("idToko");
            idBarang = intent.getStringExtra("idBarang");
            //  Toast.makeText(context,"toko"+idToko+" barang "+idBarang,Toast.LENGTH_LONG).show();
            if (intent.hasExtra("isOwner")) {
                lladaedit.setVisibility(View.VISIBLE);
                btnPesan.setVisibility(View.GONE);
            }
            fetchDataBarang();
            fetchDataToko();
        }
    }

    private void fetchDataBarang() {
        databaseReference.child(globalval.TABLE_BARANG).child(idBarang).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    barangmodel = dataSnapshot.getValue(barangmodel.class);
                    assert barangmodel != null;
                    tvNamabarang.setText(barangmodel.getNama());
                    tvHarga.setText("Rp " + barangmodel.getHargasewa());
                    tvStok.setText(barangmodel.getStokasli());
                    Picasso.get().load(barangmodel.getGambar()).into(ivGambarBarang);

                    // Toast.makeText(context,barangmodel.getGambar(),Toast.LENGTH_LONG).show();
                    if (firebaseAuth.getCurrentUser().getUid().equals(barangmodel.getIdtoko())) {

                        btnPesan.setEnabled(false);
                        btnPesan.setVisibility(View.GONE);
                        lladaedit.setVisibility(View.VISIBLE);
                    } else {
                        btnPesan.setEnabled(true);
                        btnPesan.setVisibility(View.VISIBLE);
                        lladaedit.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(context, "Tidak ada", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Tidak ada" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // Picasso.get().load(barangmodel.getGambar().toString()).into(ivGambarBarang);
    }

    private void fetchDataToko() {
        databaseReference.child(globalval.TABLE_TOKO).child(idToko).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    tokomodel = dataSnapshot.getValue(tokomodel.class);
                    assert tokomodel != null;
                    tvNamatoko.setText(tokomodel.getNamatoko().toString());

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @OnClick({R.id.btn_hapus, R.id.btn_ubah, R.id.btn_pesan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_hapus:
                databaseReference.child(globalval.TABLE_BARANG).child(idBarang).removeValue();
                finish();
                break;
            case R.id.btn_ubah:
                startActivity(new Intent(context, TambahBarangActivity.class).putExtra("id", idBarang));
                break;
            case R.id.btn_pesan:
                startActivity(new Intent(context, CheckoutPesanan.class).putExtra("idBarang", idBarang));

                Toast.makeText(context, barangmodel.getIdbarang(), Toast.LENGTH_LONG).show();
                break;
        }
    }
}
