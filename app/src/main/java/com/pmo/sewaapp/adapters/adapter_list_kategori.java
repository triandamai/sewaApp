package com.pmo.sewaapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pmo.sewaapp.R;
import com.pmo.sewaapp.SemuaBarangKategori;
import com.pmo.sewaapp.models.kategorimodel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class adapter_list_kategori extends RecyclerView.Adapter<adapter_list_kategori.MyViewHolder> {

    private Context context;
    private List<kategorimodel> kategorimodelList = new ArrayList<>();

    public adapter_list_kategori(Context context, List<kategorimodel> kategorimodelList) {
        this.context = context;
        this.kategorimodelList = kategorimodelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_kategori_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvNamakategori.setText(kategorimodelList.get(position).getNamakategori());
        holder.cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SemuaBarangKategori.class).putExtra("Kategori",kategorimodelList.get(position).getNamakategori()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return kategorimodelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /*
         * Membuat view binding (Pengganti findview by id ) dengan BUtter knife
         * */
        @BindView(R.id.cv_parent)
        CardView cvParent;
        @BindView(R.id.tv_namakategori)
        TextView tvNamakategori;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
