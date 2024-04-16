package com.example.trabalhofinalpdmv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.HashMap;

public class activity_user_settings extends AppCompatActivity {

    private Switch switchNotifications, switchEmailNotifications;
    private DatabaseHelper databaseHelper;
    private String userCPF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        switchNotifications = findViewById(R.id.switchNotifications);
        switchEmailNotifications = findViewById(R.id.switchEmailNotifications);

        databaseHelper = new DatabaseHelper(this);
        userCPF = getCurrentUserCPF(); // Implementar este método

        loadSettings();

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) ->
                databaseHelper.saveUserSetting(userCPF, "notifications", isChecked));

        switchEmailNotifications.setOnCheckedChangeListener((buttonView, isChecked) ->
                databaseHelper.saveUserSetting(userCPF, "email_notifications", isChecked));

        Button btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_user_settings.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadSettings() {
        HashMap<String, Boolean> settings = databaseHelper.loadUserSettings(userCPF);
        if (settings != null) {
            switchNotifications.setChecked(settings.getOrDefault("notifications", false));
            switchEmailNotifications.setChecked(settings.getOrDefault("email_notifications", false));
        }
    }
    private String getCurrentUserCPF() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        return sharedPreferences.getString("UserCPF", null); // Retorna null se não houver CPF salvo
    }
}