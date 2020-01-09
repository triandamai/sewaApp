package com.pmo.sewaapp.penyedia;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.pmo.sewaapp.adapters.adapterkategori;
import com.pmo.sewaapp.globalval;
import com.pmo.sewaapp.models.barangmodel;
import com.pmo.sewaapp.models.kategorimodel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class fragment_home extends Fragment {
    @BindView(R.id.dataKosong)
    LinearLayout dataKosong;
    @BindView(R.id.rv_barang_terbaru)
    RecyclerView rvBarangTerbaru;
    @BindView(R.id.btn_add)
    Button btnAdd;

    @BindView(R.id.v_flipper)
    ViewFlipper vFlipper;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private List<barangmodel> listBarang = new ArrayList<>();
    private List<kategorimodel> kategorimodelList = new ArrayList<>();
    private adapter_list_barang_terbaru adapter;
    private adapterkategori adapterkategori;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        slideshow();
        fetchTerbaru();

        return v;
    }

    private void slideshow() {
        int images[] = {
          R.drawable.banner1,
          R.drawable.banner2,
          R.drawable.banner3
        };

        for(int i = 0; i<images.length; i++){
            fliverImage(images[i] );
        }
        for (int image : images)
            fliverImage(image);
    }

    private void fliverImage(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);
        vFlipper.addView(imageView);
        vFlipper.setFlipInterval(3500);
        vFlipper.setAutoStart(true);

        vFlipper.setInAnimation(getContext(),android.R.anim.slide_in_left);
        vFlipper.setOutAnimation(getContext(),android.R.anim.slide_out_right);


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
                    adapter = new adapter_list_barang_terbaru(getContext(), listBarang);
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

    private void DialogForm() {
        dialog = new AlertDialog.Builder(getContext());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.item_add_kategori, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Tambah Kategori");
        EditText nama = dialogView.findViewById(R.id.nama_kategori);

        dialog.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = databaseReference.push().getKey();
                kategorimodel kategorimodel = new kategorimodel();
                kategorimodel.setIdkategori(id);
                kategorimodel.setNamakategori(nama.getText().toString());
                databaseReference.child(globalval.TABLE_KATEGORI).child(id).setValue(kategorimodel);
            }
        });
        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        DialogForm();
    }
}
