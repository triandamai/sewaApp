package com.pmo.sewaapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.sewaapp.DetailBarangActivity;
import com.pmo.sewaapp.R;
import com.pmo.sewaapp.globalval;
import com.pmo.sewaapp.models.barangmodel;
import com.pmo.sewaapp.models.tokomodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class adapter_list_barang_kategori extends RecyclerView.Adapter<adapter_list_barang_kategori.MyViewHolder> {


    private List<barangmodel> barangmodelList = new ArrayList<>();
    private Context context;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private tokomodel tokomodel;

    public adapter_list_barang_kategori(Context context, List<barangmodel> barangmodels) {
        this.context = context;
        this.barangmodelList = barangmodels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_barang_kategori, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(barangmodelList.get(position).getGambar()).into(holder.ivBarang);
        holder.tvNamabarang.setText(barangmodelList.get(position).getNama());
        databaseReference.child(globalval.TABLE_TOKO).child(barangmodelList.get(position).getIdtoko()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tokomodel tokomodel;
                    tokomodel = dataSnapshot.getValue(tokomodel.class);
                    assert  tokomodel != null;
                    holder.tvNamatoko.setText(tokomodel.getNamatoko());
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.ivBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DetailBarangActivity.class)
                        .putExtra("idBarang",barangmodelList.get(position).getIdbarang()).putExtra("idToko",barangmodelList.get(position).getIdtoko()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return barangmodelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /*
         * Membuat view binding (Pengganti findview by id ) dengan BUtter knife
         * */
        @BindView(R.id.iv_barang)
        ImageView ivBarang;
        @BindView(R.id.tv_namabarang)
        TextView tvNamabarang;
        @BindView(R.id.tv_namatoko)
        TextView tvNamatoko;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
