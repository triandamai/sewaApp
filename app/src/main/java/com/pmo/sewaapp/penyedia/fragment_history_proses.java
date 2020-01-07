package com.pmo.sewaapp.penyedia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import com.pmo.sewaapp.adapters.adapter_list_history;
import com.pmo.sewaapp.globalval;
import com.pmo.sewaapp.models.transaksimodel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class fragment_history_proses extends Fragment {

    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.lladadata)
    LinearLayout lladadata;
    @BindView(R.id.llkosong)
    LinearLayout llkosong;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private List<transaksimodel> transaksimodels = new ArrayList<>();
    private adapter_list_history adapter_list_history;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_history_proses, container, false);
        ButterKnife.bind(this, v);
        fetchData();
        return v;
    }

    public void fetchData() {
        databaseReference.child(globalval.TABLE_TRANSAKSI).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    lladadata.setVisibility(View.VISIBLE);
                    llkosong.setVisibility(View.GONE);
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        transaksimodel transaksimodel;
                        transaksimodel = data.getValue(transaksimodel.class);

                        transaksimodels.add(transaksimodel);
                    }
                    adapter_list_history = new adapter_list_history(getContext(), transaksimodels);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvHistory.setLayoutManager(layoutManager);
                    rvHistory.setAdapter(adapter_list_history);
                } else {
                    lladadata.setVisibility(View.GONE);
                    llkosong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                lladadata.setVisibility(View.GONE);
                llkosong.setVisibility(View.VISIBLE);
            }
        });
    }


}
