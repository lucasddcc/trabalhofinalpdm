package com.example.trabalhofinalpdmv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import android.content.SharedPreferences;


public class MainActivity extends AppCompatActivity {

    private ListView lvUsers;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verificar se o usuário está logado
        if (!isUserLoggedIn()) {
            // Redirecionar para a tela de login
            Intent intent = new Intent(this, activity_login.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);

        Button btnToCreatePost = findViewById(R.id.btnToCreatePost);
        btnToCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_create_post.class);
                startActivity(intent);
            }
        });

        Button btnConfig = findViewById(R.id.btnConfig);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_user_settings.class);
                startActivity(intent);
            }
        });

        Button btnViewPosts = findViewById(R.id.btnToViewPosts);
        btnViewPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_view_posts.class);
                startActivity(intent);
            }
        });

        Button btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_profile.class);
                startActivity(intent);
            }
        });

        // Botão de Logout
        Button btnLogout = findViewById(R.id.btnLogout); // Adicione este botão no seu layout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        return sharedPreferences.contains("UserCPF");
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("UserCPF");
        editor.apply();

        // Redirecionar para a tela de login
        Intent intent = new Intent(this, activity_login.class);
        startActivity(intent);
        finish();
    }
}

