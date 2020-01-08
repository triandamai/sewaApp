package com.pmo.sewaapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.sewaapp.DetailTransaksi;
import com.pmo.sewaapp.R;
import com.pmo.sewaapp.globalval;
import com.pmo.sewaapp.models.barangmodel;
import com.pmo.sewaapp.models.transaksimodel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class adapter_list_history extends RecyclerView.Adapter<adapter_list_history.MyViewHolder> {

    private List<transaksimodel> data = new ArrayList<>();
    private Context context;
    private barangmodel barangmodel;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public adapter_list_history(Context context, List<transaksimodel> data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_transaksi, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //pindah ke halamn detail dan menampilkan detailnya
        holder.cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context,data.get(position).getIdTransaksi(),Toast.LENGTH_LONG).show();
               context.startActivity(new Intent(context, DetailTransaksi.class).putExtra("idTransaksi",data.get(position).getIdTransaksi()));
            }
        });
        databaseReference.child(globalval.TABLE_BARANG).child(data.get(position).getIDBARANG()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    barangmodel = dataSnapshot.getValue(barangmodel.class);
                    assert  barangmodel != null;
                    holder.tvNamaBarang.setText(barangmodel.getNama());
                    holder.tvAlamat.setText(data.get(position).getAlamat());
                    holder.tvHarga.setText(data.get(position).getHarga()+" Status "+data.get(position).getStatus());
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_namaBarang)
        TextView tvNamaBarang;
        @BindView(R.id.tv_alamat)
        TextView tvAlamat;
        @BindView(R.id.tv_harga)
        TextView tvHarga;
        @BindView(R.id.cv_parent)
        CardView cvParent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
