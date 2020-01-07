package com.pmo.sewaapp.penyedia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pmo.sewaapp.R;
import com.pmo.sewaapp.adapters.tabAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class fragment_history extends Fragment {

    @BindView(R.id.tab_lay)
    TabLayout tabLay;
    @BindView(R.id.vp)
    ViewPager vp;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    private tabAdapter tabAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this,v);
            tabAdapter = new tabAdapter(getActivity().getSupportFragmentManager(),1);
            tabAdapter.addFragment(new fragment_history_proses(),"Proses");
            tabAdapter.addFragment(new fragment_history_selesai(),"Selesai");
            tabAdapter.addFragment(new fragment_history_gagal(),"Gagal");

            vp.setAdapter(tabAdapter);
            tabLay.setupWithViewPager(vp);
            return v;
    }
}
