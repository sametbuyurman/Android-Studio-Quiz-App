package com.mediol.bilgiuygulama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class KayitActivity extends AppCompatActivity {

    private EditText username_text, password_text, password_text_tekrar, email_text;
    private Button kayitol_button;
    private String txtmail, txtpassword, txtusername, txtpasswordtekrar;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        username_text = findViewById(R.id.username_text);
        password_text = findViewById(R.id.password_text);
        password_text_tekrar = findViewById(R.id.password_text_tekrar);
        email_text = findViewById(R.id.email_et5);
        kayitol_button = findViewById(R.id.kayitol_button);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }


    public void kayitOl(View view) {
        txtusername = username_text.getText().toString();
        txtmail = email_text.getText().toString();
        txtpassword = password_text.getText().toString();
        txtpasswordtekrar = password_text_tekrar.getText().toString();

        // Şifrelerin eşleştiğini kontrol etme
        if (txtpassword.equals(txtpasswordtekrar)) {
            // E-posta adresinin geçerli olup olmadığını kontrol etme
            if (isValidEmail(txtmail)) {
                // Şifrenin belirli kriterlere uygun olup olmadığını kontrol etme
                if (isValidPassword(txtpassword)) {
                    // Kullanıcı adı, e-posta ve şifre alanlarının dolu olup olmadığını kontrol etme
                    if (!TextUtils.isEmpty(txtusername) && !TextUtils.isEmpty(txtmail) && !TextUtils.isEmpty(txtpassword)) {
                        // Şifrenin kullanıcı adı ile aynı olmadığını kontrol etme
                        if (!txtusername.equals(txtpassword)) {
                            // Firebase Authentication kullanarak kullanıcıyı kaydetme
                            mAuth.createUserWithEmailAndPassword(txtmail, txtpassword)
                                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                mUser = mAuth.getCurrentUser();

                                                if (mUser != null) {
                                                    // Kullanıcı verilerini Firestore'a kaydet
                                                    HashMap<String, Object> mData = new HashMap<>();
                                                    mData.put("kullaniciAdi", txtusername);
                                                    mData.put("kullaniciEmail", txtmail);
                                                    mData.put("kullaniciSifre", txtpassword);
                                                    mData.put("kullaniciId", mUser.getUid());

                                                    mFirestore.collection("Kullanıcılar").document(mUser.getUid())
                                                            .set(mData)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(KayitActivity.this, "Kayıt İşlemi Başarılı", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(KayitActivity.this, MainActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    } else {
                                                                        Toast.makeText(KayitActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            } else {
                                                Toast.makeText(KayitActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(KayitActivity.this, "Şifre kullanıcı adı ile aynı olamaz", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(KayitActivity.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(KayitActivity.this, "Şifre en az 6 karakter içermeli ve büyük harf içermelidir", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(KayitActivity.this, "Geçerli bir e-posta adresi girin", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(KayitActivity.this, "Girilen Şifreler Farklı", Toast.LENGTH_SHORT).show();
        }
    }

    // E-posta adresinin geçerli olup olmadığını kontrol eden metod
    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    // Şifrenin belirli kriterlere uygun olup olmadığını kontrol eden metod
    private boolean isValidPassword(String password) {
        return password.length() >= 6 && password.matches(".*[A-Z].*");
    }


}








