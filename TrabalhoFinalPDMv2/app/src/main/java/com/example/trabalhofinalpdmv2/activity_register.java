package com.example.trabalhofinalpdmv2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class activity_register extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etCPF;
    private Button btnRegister, btnBackToLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etRegisterName);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        etCPF = findViewById(R.id.etRegisterCPF);
        btnRegister = findViewById(R.id.btnRegister);
        dbHelper = new DatabaseHelper(this);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_register.this, activity_login.class);
                startActivity(intent);
            }
        });
    }

    private void register() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String cpf = etCPF.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Verificar se o CPF já existe
        Cursor cursor = db.query("users", new String[]{"cpf"}, "cpf=?", new String[]{cpf}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            Toast.makeText(activity_register.this, "Erro: CPF já cadastrado!", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("cpf", cpf);
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);

        long newRowId = db.insert("users", null, values);
        if (newRowId != -1) {
            Toast.makeText(activity_register.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity_register.this, activity_login.class);
            startActivity(intent);
        } else {
            Toast.makeText(activity_register.this, "Erro ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
        }
    }
}

