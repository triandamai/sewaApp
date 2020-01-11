package com.pmo.sewaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends AppCompatActivity {

    /*
    * Membuat view binding (Pengganti findview by id ) dengan BUtter knife
    * */
    @BindView(R.id.et_nomerTelp)
    EditText etNomerTelp;
    @BindView(R.id.btn_kirimKode)
    Button btnKirimKode;
    @BindView(R.id.et_verifikasi)
    EditText etVerifikasi;
    @BindView(R.id.btn_Verifikasi)
    Button btnVerifikasi;
    @BindView(R.id.ll_inputnomer)
    LinearLayout llInputnomer;
    @BindView(R.id.ll_terimakode)
    LinearLayout llTerimakode;

    //TODO:: inisiasi kebutuhan firebase (firbase authentication,realtime database,firebase storage)
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Context context = Register.this;
    private String verificationid ;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        llInputnomer.setVisibility(View.VISIBLE);
        llTerimakode.setVisibility(View.GONE);
    }

    public void sendAuthenticationCode(String phonenumber) {
        Toast.makeText(context, "Mengirim", Toast.LENGTH_LONG).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+62"+phonenumber, 60, TimeUnit.SECONDS, this, callback);
    }

    public void signInwithPhoneNumber(PhoneAuthCredential phoneAuthCredential) {
        Toast.makeText(context, "Memverifikasi..", Toast.LENGTH_LONG).show();
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Berhasil", Toast.LENGTH_LONG).show();
                            databaseReference
                                    .child(globalval.TABLE_USER)
                                    .child(firebaseAuth.getUid())
                                    .child("Nohp")
                                    .setValue(etNomerTelp.getText().toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            startActivity(new Intent(context, LengkapiBiodataActivity.class).putExtra("uid",firebaseAuth.getCurrentUser().getUid()));
                                            finish();
                                        }
                                    });
                        } else {
                            Toast.makeText(context, "gagal" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    @OnClick({R.id.btn_kirimKode, R.id.btn_Verifikasi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_kirimKode:

                if (!etNomerTelp.getText().toString().isEmpty()) {
                    llInputnomer.setVisibility(View.GONE);
                    llTerimakode.setVisibility(View.VISIBLE);
                    sendAuthenticationCode(etNomerTelp.getText().toString());
                }
                break;
            case R.id.btn_Verifikasi:
                if (!verificationid.isEmpty() || verificationid != null) {
                    PhoneAuthCredential credential =
                            PhoneAuthProvider.getCredential(verificationid, etVerifikasi.getText().toString());
                    signInwithPhoneNumber(credential);
                }
                break;
        }
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                etVerifikasi.setText(code);
             //   firebaseAuth.signInWithCredential(phoneAuthCredential);
                signInwithPhoneNumber(phoneAuthCredential);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(context, "Verifikasi gagal" + e.toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Register.this.verificationid = s;
        }


    };
}
