package com.pmo.sewaapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.sewaapp.adapters.adapter_list_kategori;
import com.pmo.sewaapp.models.kategorimodel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Kategori extends AppCompatActivity {

    /*
     * Membuat view binding (Pengganti findview by id ) dengan BUtter knife
     * */
    @BindView(R.id.rv_kategori)
    RecyclerView rvKategori;
    @BindView(R.id.ll_kosong)
    LinearLayout llKosong;

    //TODO:: inisiasi kebutuhan firebase (firbase authentication,realtime database,firebase storage)
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Context context = Kategori.this;
    private List<kategorimodel> kategorimodelList = new ArrayList<>();
    private adapter_list_kategori adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);
        ButterKnife.bind(this);
        fetchData();
    }

    private void fetchData() {
        databaseReference.child(globalval.TABLE_KATEGORI).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //Toast.makeText(context, "Ada", Toast.LENGTH_LONG).show();
                    //jika data ada
                    llKosong.setVisibility(View.GONE);
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        kategorimodel kategorimodel;
                        kategorimodel = data.getValue(kategorimodel.class);
                        kategorimodel.setIdkategori(data.getKey());
                        assert kategorimodel != null;
                        kategorimodelList.add(kategorimodel);
                    }
                    adapter = new adapter_list_kategori(context, kategorimodelList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    rvKategori.setLayoutManager(layoutManager);
                    rvKategori.setAdapter(adapter);
                } else {
                    //jika data kosong
                    llKosong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
