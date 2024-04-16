package com.example.trabalhofinalpdmv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class activity_update_datails extends AppCompatActivity {

    private EditText editTextUpdateName, editTextUpdateEmail;
    private DatabaseHelper databaseHelper;
    private String userCPF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_datails);

        editTextUpdateName = findViewById(R.id.editTextUpdateName);
        editTextUpdateEmail = findViewById(R.id.editTextUpdateEmail);
        databaseHelper = new DatabaseHelper(this);

        userCPF = getCurrentUserCPF(); // Implementar este método para obter o CPF do usuário logado

        findViewById(R.id.btnSaveUpdates).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_update_datails.this, activity_profile.class);
                startActivity(intent);
            }
        });
    }

    private void updateUserInfo() {
        String newName = editTextUpdateName.getText().toString().trim();
        String newEmail = editTextUpdateEmail.getText().toString().trim();

        // pega os trem do banco
        HashMap<String, String> userDetails = databaseHelper.getUserDetails(userCPF);
        if (userDetails == null) {
            Toast.makeText(activity_update_datails.this, "Erro ao buscar dados do usuário.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newName.isEmpty()) {
            newName = userDetails.get("name");
        }
        if (newEmail.isEmpty()) {
            newEmail = userDetails.get("email");
        }
        if (databaseHelper.updateUserDetails(userCPF, newName, newEmail)) {
            Toast.makeText(activity_update_datails.this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity_update_datails.this, "Falha ao atualizar perfil.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentUserCPF() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        return sharedPreferences.getString("UserCPF", null); // Retorna null se não houver CPF salvo
    }

}
