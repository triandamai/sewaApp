package com.pmo.sewaapp.penyedia;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.sewaapp.LengkapiBiodataActivity;
import com.pmo.sewaapp.R;
import com.pmo.sewaapp.globalval;
import com.pmo.sewaapp.models.usermodel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class fragment_profile extends Fragment {


    @BindView(R.id.tv_Nama)
    TextView tvNama;
    @BindView(R.id.tv_Alamat)
    TextView tvAlamat;
    @BindView(R.id.tv_namaTelp)
    TextView tvNamaTelp;
    @BindView(R.id.etEmail)
    TextView etEmail;
    @BindView(R.id.btn_ubah)
    Button btnUbah;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public fragment_profile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, v);
        fetch();
        return v;
    }

    public void fetch(){
        databaseReference.child(globalval.TABLE_USER).child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    usermodel model = new usermodel();
                    model = dataSnapshot.getValue(usermodel.class);
                    tvNama.setText(String.valueOf(model.getNama()));
                    tvNamaTelp.setText(String.valueOf(model.nohp));
                    etEmail.setText(String.valueOf(model.getEmail()));
                    tvAlamat.setText(String.valueOf(model.getAlamat()));
                }else {
                    Toast.makeText(getContext(),"TIdak ada apapun",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Database Eror "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.btn_ubah)
    public void onViewClicked() {
        startActivity(new Intent(getActivity().getApplicationContext(), LengkapiBiodataActivity.class).putExtra("uid",firebaseAuth.getCurrentUser().getUid()));

    }
}
