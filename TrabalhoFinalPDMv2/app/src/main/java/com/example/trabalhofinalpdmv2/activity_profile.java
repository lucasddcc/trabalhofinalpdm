package com.example.trabalhofinalpdmv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class activity_profile extends AppCompatActivity {

    private TextView tvName, tvEmail, tvCPF;

    private Button btnUpdateDetails;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvCPF = findViewById(R.id.tvCPF);
        databaseHelper = new DatabaseHelper(this);

        loadUserProfile();

        Button btnUpdateDetails = findViewById(R.id.btnUpdateDetails);
        btnUpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_profile.this, activity_update_datails.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_profile.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userCPF = sharedPreferences.getString("UserCPF", null);

        HashMap<String, String> userDetails = databaseHelper.getUserDetails(userCPF);
        if (userDetails != null) {
            tvName.setText("Nome: " + userDetails.get("name"));
            tvEmail.setText("Email: " + userDetails.get("email"));
            tvCPF.setText("CPF: " + userDetails.get("cpf"));
        }
    }
}
