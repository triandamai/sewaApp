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


public class fragment_history_gagal extends Fragment {


    /*
     * Membuat view binding (Pengganti findview by id ) dengan BUtter knife
     * */
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.lladadata)
    LinearLayout lladadata;
    @BindView(R.id.llkosong)
    LinearLayout llkosong;
    @BindView(R.id.ll_kosong)
    LinearLayout llKosong;

    //TODO:: inisiasi kebutuhan firebase (firbase authentication,realtime database,firebase storage)
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
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_history_gagal, container, false);
        ButterKnife.bind(this, v);
        lladadata.setVisibility(View.GONE);
        llkosong.setVisibility(View.GONE);
        llKosong.setVisibility(View.VISIBLE);
        fetchData();
        return v;
    }

    public void fetchData() {
        databaseReference.child(globalval.TABLE_TRANSAKSI)
                .orderByChild("idtoko")
                .startAt(firebaseAuth.getCurrentUser().getUid())
                .endAt(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    lladadata.setVisibility(View.VISIBLE);
                    llkosong.setVisibility(View.GONE);
                    llKosong.setVisibility(View.GONE);
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        transaksimodel transaksimodel;
                        transaksimodel = data.getValue(transaksimodel.class);
                        transaksimodel.setIdTransaksi(data.getKey());
                        assert  transaksimodel != null;
                        if (transaksimodel.getStatus().equals(globalval.STATUS_GAGAL)){

                        }else {
                            transaksimodels.add(transaksimodel);
                        }

                    }
                    adapter_list_history = new adapter_list_history(getContext(), transaksimodels);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvHistory.setLayoutManager(layoutManager);
                    rvHistory.setAdapter(adapter_list_history);

                } else {
                    lladadata.setVisibility(View.GONE);
                    llkosong.setVisibility(View.VISIBLE);
                    llKosong.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
