package com.mediol.bilgiuygulama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SifreSifirlama extends AppCompatActivity {

    private EditText email_sifresifirla_edit;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_sifirlama);
        mAuth = FirebaseAuth.getInstance();
        email_sifresifirla_edit = findViewById(R.id.email_sifresifirla_edit);
    }
    public void sifresifirla(View v) {
        String email = email_sifresifirla_edit.getText().toString();
        if(!TextUtils.isEmpty(email)) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SifreSifirlama.this, "Şifre sıfırlama bağlantısı email adresinize gönderildi.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SifreSifirlama.this,GirisActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SifreSifirlama.this, "Şifre sıfırlama bağlantısı gönderilemedi. Lütfen email adresinizi kontrol edin.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(SifreSifirlama.this, "Lütfen email adresinizi girin.", Toast.LENGTH_SHORT).show();
        }


     }



    }