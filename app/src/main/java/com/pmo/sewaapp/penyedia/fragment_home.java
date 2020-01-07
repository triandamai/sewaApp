package com.pmo.sewaapp.penyedia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.sewaapp.R;
import com.pmo.sewaapp.adapters.adapter_list_barang_terbaru;
import com.pmo.sewaapp.globalval;
import com.pmo.sewaapp.models.barangmodel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class fragment_home extends Fragment {


    @BindView(R.id.dataKosong)
    LinearLayout dataKosong;
    @BindView(R.id.rv_barang_terbaru)
    RecyclerView rvBarangTerbaru;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private List<barangmodel> listBarang = new ArrayList<>();
    private adapter_list_barang_terbaru adapter;

    public fragment_home() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,v);
        fetchTerbaru();
        return v;
    }

    public void fetchTerbaru() {
        databaseReference.child(globalval.TABLE_BARANG)
                .orderByChild("waktuditambah")
                .limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        barangmodel barangmodel;
                        barangmodel = data.getValue(barangmodel.class);
                        assert barangmodel != null;
                        barangmodel.setIdbarang(data.getKey());
                        listBarang.add(barangmodel);
                    }
                    adapter = new adapter_list_barang_terbaru(getContext(),listBarang);
  LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                     rvBarangTerbaru.setLayoutManager(layoutManager);
                    rvBarangTerbaru.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Tidak ada data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
