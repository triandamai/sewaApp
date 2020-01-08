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

import com.pmo.sewaapp.DetailBarangActivity;
import com.pmo.sewaapp.R;
import com.pmo.sewaapp.models.barangmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class adapter_list_barang_toko extends RecyclerView.Adapter<adapter_list_barang_toko.MyViewHolder> {
    public List<barangmodel> data = new ArrayList<>();
    public Context context;


    //bangun constructor
    public adapter_list_barang_toko(Context context, List<barangmodel> barang) {
        this.context = context;
        this.data = barang;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //memanggil tamplan untuk ditampilkan di recyclerview
        View v = LayoutInflater.from(context).inflate(R.layout.item_barang_toko, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //set data ke masing masing view
        Picasso.get().load(data.get(position).getGambar()).into(holder.ivBarang);
        holder.tvNamabarang.setText(data.get(position).getNama());

        holder.ivBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DetailBarangActivity.class)
                        .putExtra("idToko",data.get(position).getIdtoko()));
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}
