package com.example.trabalhofinalpdmv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class activity_login extends AppCompatActivity {

    private EditText etCPF, etPassword;
    private Button btnLogin, btnToRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCPF = findViewById(R.id.etCPF);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        dbHelper = new DatabaseHelper(this);
        btnToRegister = findViewById(R.id.btnToRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_login.this, activity_register.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        String cpf = etCPF.getText().toString();
        String password = etPassword.getText().toString();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"cpf"}, "cpf=? AND password=?", new String[]{cpf, password}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            saveUserSession(cpf); // Salvar sessão do usuário

            cursor.close();

            Intent intent = new Intent(activity_login.this, MainActivity.class);
            intent.putExtra("USER_CPF", cpf);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(activity_login.this, "Usuário e/ou senha incorretos!", Toast.LENGTH_SHORT).show();
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void saveUserSession(String cpf) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserCPF", cpf);
        editor.apply();
    }
}