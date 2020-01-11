package com.pmo.sewaapp.penyedia;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.sewaapp.R;
import com.pmo.sewaapp.TambahBarangActivity;
import com.pmo.sewaapp.adapters.adapter_list_barang_toko;
import com.pmo.sewaapp.globalval;
import com.pmo.sewaapp.models.barangmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class fragment_toko extends Fragment {

    /*
     * Membuat view binding (Pengganti findview by id ) dengan BUtter knife
     * */
    @BindView(R.id.btn_buattoko)
    Button btnBuattoko;
    @BindView(R.id.toko_kosong)
    LinearLayout tokoKosong;
    @BindView(R.id.adaData)
    ScrollView adaData;
    @BindView(R.id.loadingToko)
    LinearLayout loadingToko;
    @BindView(R.id.barang_kosong)
    LinearLayout barangKosong;
    @BindView(R.id.rv_barang_toko)
    RecyclerView rvBarangToko;
    @BindView(R.id.iv_edit_toko)
    ImageView ivEditToko;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    @BindView(R.id.iv_bannerToko)
    ImageView ivBannerToko;
    @BindView(R.id.tv_nama_toko)
    TextView tvNamaToko;
    @BindView(R.id.rl_fab)
    RelativeLayout rlFab;

    //TODO:: inisiasi kebutuhan firebase (firbase authentication,realtime database,firebase storage)
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private List<barangmodel> listBarang = new ArrayList<>();
    private adapter_list_barang_toko adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_toko, container, false);
        ButterKnife.bind(this, v);

        loadingToko.setVisibility(View.VISIBLE);
        tokoKosong.setVisibility(View.GONE);
        adaData.setVisibility(View.GONE);
        barangKosong.setVisibility(View.GONE);
        rvBarangToko.setVisibility(View.GONE);
        rlFab.setVisibility(View.GONE);
        fetch();
        fetchBarang();
        return v;
    }

    public void fetch() {
        databaseReference.child(globalval.TABLE_TOKO).child(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        loadingToko.setVisibility(View.GONE);
                        if (dataSnapshot.exists()) {

                            tokoKosong.setVisibility(View.GONE);
                            adaData.setVisibility(View.VISIBLE);
                            rlFab.setVisibility(View.VISIBLE);
                            String namatoko = dataSnapshot.child("namatoko").getValue(String.class);
                            String banner = dataSnapshot.child("bannertoko").getValue(String.class);
                            Picasso.get().load(banner).into(ivBannerToko);
                            tvNamaToko.setText(namatoko);
                        } else {
                            rlFab.setVisibility(View.GONE);
                            tokoKosong.setVisibility(View.VISIBLE);
                            adaData.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Error " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void fetchBarang() {

        databaseReference.child(globalval.TABLE_BARANG).orderByChild("idtoko").equalTo(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listBarang.clear();
                if (dataSnapshot.exists()) {
                    barangKosong.setVisibility(View.GONE);
                    rvBarangToko.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity().getApplicationContext(), "ada", Toast.LENGTH_LONG).show();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        barangmodel barangmodel;

                        barangmodel = ds.getValue(barangmodel.class);
                        assert barangmodel != null;
                        barangmodel.setIdbarang(ds.getKey());
                        listBarang.add(barangmodel);
                    }

                    adapter = new adapter_list_barang_toko(getContext(), listBarang);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                    rvBarangToko.setLayoutManager(layoutManager);
                    rvBarangToko.setAdapter(adapter);
                } else {
                    barangKosong.setVisibility(View.VISIBLE);
                    rvBarangToko.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "tidak ada", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "database Error" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @OnClick({R.id.iv_edit_toko, R.id.fab_add, R.id.btn_buattoko})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_edit_toko:
                break;
            case R.id.fab_add:
                startActivity(new Intent(getContext(), TambahBarangActivity.class));
                break;
            case R.id.btn_buattoko:
                startActivity(new Intent(getContext(), BuattokoActivity.class));
                break;
        }
    }
}
