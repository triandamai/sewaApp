package com.pmo.sewaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmo.sewaapp.R;
import com.pmo.sewaapp.models.barangmodel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class adapter_list_barang_toko extends RecyclerView.Adapter<adapter_list_barang_toko.MyViewHolder> {
    public List<barangmodel> data = new ArrayList<>();
    public Context context;


    public adapter_list_barang_toko(Context context, List<barangmodel> barang) {
        this.context = context;
        this.data = barang;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_barang_toko, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ivBarang.setIm
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
