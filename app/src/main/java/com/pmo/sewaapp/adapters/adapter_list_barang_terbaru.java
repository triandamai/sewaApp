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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.sewaapp.DetailBarangActivity;
import com.pmo.sewaapp.R;
import com.pmo.sewaapp.globalval;
import com.pmo.sewaapp.models.barangmodel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class adapter_list_barang_terbaru extends RecyclerView.Adapter<adapter_list_barang_terbaru.MyViewHolder> {

    private Context context;
    private List<barangmodel> data;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public adapter_list_barang_terbaru(Context context, List<barangmodel> barang) {
        this.context = context;
        this.data = barang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_barang_terbaru, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvNamabarang.setText(data.get(position).getNama());
        Picasso.get().load(data.get(position).getGambar()).into(holder.ivBarang);
        databaseReference.child(globalval.TABLE_TOKO).child(data.get(position).getIdtoko()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String namatoko = dataSnapshot.child("namatoko").getValue(String.class);
                    assert namatoko != null;
                    holder.tvNamatoko.setText(namatoko);
                }else {
                    holder.tvNamatoko.setText("Toko Tidak Ditemukan");
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
                        .putExtra("idBarang",data.get(position).getIdbarang()).putExtra("idToko",data.get(position).getIdtoko()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
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
