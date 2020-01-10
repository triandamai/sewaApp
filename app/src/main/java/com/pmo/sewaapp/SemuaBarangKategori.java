package com.pmo.sewaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.sewaapp.adapters.adapter_list_barang_kategori;
import com.pmo.sewaapp.models.barangmodel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SemuaBarangKategori extends AppCompatActivity {

    @BindView(R.id.rv_semua_Barang)
    RecyclerView rvSemuaBarang;
    @BindView(R.id.ll_adadata)
    LinearLayout llAdadata;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_kosong)
    LinearLayout llKosong;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private List<barangmodel> barangmodelList = new ArrayList<>();
    private adapter_list_barang_kategori adapter;
    private Context context = SemuaBarangKategori.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semua_barang_kategori);
        ButterKnife.bind(this);
        llAdadata.setVisibility(View.GONE);
        llKosong.setVisibility(View.GONE);
        llLoading.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        if (intent.hasExtra("Kategori")) {
            tvTitle.setText("Menampilkan " + intent.getStringExtra("Kategori"));

            tampilPerkategori(intent.getStringExtra("Kategori"));
        } else {
            tvTitle.setText("Menampilkan Semua Barang");
            tampilPerkategori("");
        }
    }

    private void tampilSemua() {
        databaseReference.child(globalval.TABLE_BARANG)
                .orderByChild("waktuditambah").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    llAdadata.setVisibility(View.VISIBLE);
                    llKosong.setVisibility(View.GONE);
                    llLoading.setVisibility(View.GONE);
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        barangmodel barangmodel;
                        barangmodel = data.getValue(barangmodel.class);
                        barangmodel.setIdbarang(data.getKey());
                        assert barangmodel != null;
                        barangmodelList.add(barangmodel);
                    }

                    adapter = new adapter_list_barang_kategori(context, barangmodelList);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                    rvSemuaBarang.setLayoutManager(layoutManager);
                    rvSemuaBarang.setAdapter(adapter);
                } else {
                    llAdadata.setVisibility(View.GONE);
                    llKosong.setVisibility(View.VISIBLE);
                    llLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void tampilPerkategori(String kategori) {
        databaseReference.child(globalval.TABLE_BARANG).orderByChild("kategori").startAt(kategori).endAt(kategori).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    llAdadata.setVisibility(View.VISIBLE);
                    llKosong.setVisibility(View.GONE);
                    llLoading.setVisibility(View.GONE);
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        barangmodel barangmodel;
                        barangmodel = data.getValue(barangmodel.class);
                        barangmodel.setIdbarang(data.getKey());
                        assert barangmodel != null;
                        barangmodelList.add(barangmodel);
                    }

                    adapter = new adapter_list_barang_kategori(context, barangmodelList);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                    rvSemuaBarang.setLayoutManager(layoutManager);
                    rvSemuaBarang.setAdapter(adapter);
                } else {
                    llAdadata.setVisibility(View.GONE);
                    llKosong.setVisibility(View.VISIBLE);
                    llLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
