package com.mediol.bilgiuygulama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GirisActivity extends AppCompatActivity {

    private EditText email_giris_edit,password_giris_edit;
    private String email_txt,password_txt;
    private Button girisyap_button;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        email_giris_edit = findViewById(R.id.email_girs_edit);
        password_giris_edit = findViewById(R.id.password_giris_edit);
        girisyap_button = findViewById(R.id.girisyap_button);
        mAuth = FirebaseAuth.getInstance();


    }

    public void girisyap(View v){
        email_txt = email_giris_edit.getText().toString();
        password_txt = password_giris_edit.getText().toString();
        if (!TextUtils.isEmpty(email_txt) && !TextUtils.isEmpty(password_txt)){
            mAuth.signInWithEmailAndPassword(email_txt,password_txt)
                    .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            mUser = mAuth.getCurrentUser();
                            Intent intent = new Intent(GirisActivity.this,ListActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(GirisActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }else{
            Toast.makeText(this, "Email ve şifre boş olamaz.", Toast.LENGTH_SHORT).show();
        }




    }





    public void sifremiUnuttum(View v){
        Intent intentsifre = new Intent(GirisActivity.this,SifreSifirlama.class);
        startActivity(intentsifre);

    }

}

