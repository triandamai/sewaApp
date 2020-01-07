package com.pmo.sewaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmo.sewaapp.R;
import com.pmo.sewaapp.models.kategorimodel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class adapterkategori extends RecyclerView.Adapter<adapterkategori.MyViewHolder> {

    private List<kategorimodel> kategorimodelList;
    private Context context;

    public adapterkategori(Context context, List<kategorimodel> kategorimodels) {
        this.context = context;
        this.kategorimodelList = kategorimodels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_kategori, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvNamakategori.setText(kategorimodelList.get(position).getNamakategori());
    }

    @Override
    public int getItemCount() {
        return kategorimodelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_namakategori)
        TextView tvNamakategori;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
