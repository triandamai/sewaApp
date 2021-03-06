package com.pmo.sewaapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.sewaapp.models.barangmodel;
import com.pmo.sewaapp.models.transaksimodel;
import com.pmo.sewaapp.penyedia.Penyedia;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckoutPesanan extends AppCompatActivity {


    /*
     * Membuat view binding (Pengganti findview by id ) dengan BUtter knife
     * */
    @BindView(R.id.iv_gambarbarang)
    ImageView ivGambarbarang;
    @BindView(R.id.tv_namabarang)
    TextView tvNamabarang;
    @BindView(R.id.tv_hargabarang)
    TextView tvHargabarang;
    @BindView(R.id.btn_minus)
    Button btnMinus;
    @BindView(R.id.tv_jumlah)
    TextView tvJumlah;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.et_nama)
    EditText etNama;
    @BindView(R.id.et_alamat)
    EditText etAlamat;
    @BindView(R.id.total_rp)
    TextView totalRp;
    @BindView(R.id.btn_ceckout)
    Button btnCeckout;
    @BindView(R.id.et_tanggal_ambil)
    EditText etTanggalAmbil;
    @BindView(R.id.et_tanggal_kembali)
    EditText etTanggalKembali;

    //TODO:: inisiasi kebutuhan firebase (firbase authentication,realtime database,firebase storage)
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private double hargaTotal ;
    private double hargaAwal;
    private int jumlahAwal;
    private int jumlahTotal ;
    private String idBarang;
    private barangmodel barangmodel = new barangmodel();
    private Context context = CheckoutPesanan.this;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
     String strambil = "";
     String strkembali = "";
    private int jml =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_pesanan);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent.hasExtra("idBarang")) {
            this.idBarang = intent.getStringExtra("idBarang");
            dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            Toast.makeText(CheckoutPesanan.this, idBarang, Toast.LENGTH_LONG).show();
            fetchData();
        } else {
            finish();
        }

    }

    public void fetchData() {
        databaseReference.child(globalval.TABLE_BARANG).child(idBarang).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    barangmodel = dataSnapshot.getValue(barangmodel.class);
                    assert barangmodel != null;
                    tvNamabarang.setText(barangmodel.getNama());
                    tvHargabarang.setText(barangmodel.getHargasewa());
                    hargaAwal = Double.parseDouble(barangmodel.getHargasewa());
                    Picasso.get().load(barangmodel.getGambar()).into(ivGambarbarang);
                    jumlahAwal = 1;
                    jumlahTotal = jumlahAwal;
                    hargaTotal = hargaAwal;
                    jml = Integer.parseInt(barangmodel.getStokasli());

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick({R.id.btn_minus, R.id.btn_add, R.id.btn_ceckout,R.id.et_tanggal_ambil, R.id.et_tanggal_kembali})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_minus:
                addData(2);
                break;
            case R.id.btn_add:
                addData(1);
                break;
            case R.id.btn_ceckout:
                if(cekform()){
                    cekoutnow();
                }else {
                    Toast.makeText(context,"Harap Isi Semua Form!",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.et_tanggal_ambil:
                showDateDialog(1);
                break;
            case R.id.et_tanggal_kembali:
                showDateDialog(2);
                break;
        }
    }

    private void showDateDialog(int ambil) {

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                if (ambil == 1) {
                    // tvDateResult.setText("Tanggal dipilih : " + dateFormatter.format(newDate.getTime()));
                    etTanggalAmbil.setText(dateFormatter.format(newDate.getTime()).toString());
                    strambil = newDate.getTime().toString();
                } else {
                    etTanggalKembali.setText(dateFormatter.format(newDate.getTime()).toString());
                    strkembali = newDate.getTime().toString();
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    public void cekoutnow() {
        transaksimodel transaksimodel = new transaksimodel();

        transaksimodel.setIDBARANG(idBarang);
        transaksimodel.setIDPENYEWA(firebaseAuth.getCurrentUser().getUid());
        transaksimodel.setIDTOKO(barangmodel.getIdtoko());
        transaksimodel.setAlamat(etAlamat.getText().toString());
        transaksimodel.setBuktipembayaran("1");
        transaksimodel.setStatus(globalval.STATUS_PERTAMA_PESAN);
        transaksimodel.setJumlah(String.valueOf(this.jumlahTotal));
        transaksimodel.setHarga(String.valueOf(this.hargaTotal));
        transaksimodel.setTglambil(etTanggalAmbil.getText().toString());
        transaksimodel.setTanggalkembali(etTanggalKembali.getText().toString());
        transaksimodel.setTanggal(String.valueOf(new Date().getTime()));
        transaksimodel.setTotal(String.valueOf(this.jumlahTotal));


        databaseReference.child(globalval.TABLE_TRANSAKSI).push().setValue(transaksimodel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(CheckoutPesanan.this, Penyedia.class));
                finish();
            }
        });
    }
    public boolean cekform(){
        return !etNama.getText().toString().isEmpty() || !etAlamat.getText().toString().isEmpty() || !etTanggalAmbil.getText().toString().isEmpty() || !etTanggalKembali.getText().toString().isEmpty() || !strambil.isEmpty() || !strkembali.isEmpty();
    }
    public void addData(int act) {
        if (act == 1) {
            if(this.jumlahTotal <= this.jml){
                Toast.makeText(context,"Stok Tidak Tersedia!",Toast.LENGTH_LONG).show();
            }else {
                this.jumlahTotal = this.jumlahTotal + this.jumlahAwal;
                this.hargaTotal = this.hargaTotal + this.hargaAwal;
            }
        } else {
            if (jumlahTotal <= 1) {
                this.jumlahTotal = this.jumlahAwal;
                this.hargaTotal = this.hargaAwal;
            } else {
                this.jumlahTotal = this.jumlahTotal - this.jumlahAwal;
                this.hargaTotal = this.hargaTotal - this.hargaAwal;
            }
        }
        totalRp.setText("Rp " + String.valueOf(hargaTotal));
        tvJumlah.setText(String.valueOf(this.jumlahTotal));
    }


}
