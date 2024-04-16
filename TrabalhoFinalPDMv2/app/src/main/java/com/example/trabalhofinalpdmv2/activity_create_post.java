package com.example.trabalhofinalpdmv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class activity_create_post extends AppCompatActivity {

    private EditText editTextPostContent;
    private Button buttonPost;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        editTextPostContent = findViewById(R.id.editTextPostContent);
        buttonPost = findViewById(R.id.buttonPost);
        databaseHelper = new DatabaseHelper(this);

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postContent();
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_create_post.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void postContent() {
        String content = editTextPostContent.getText().toString();
        String userCPF = getLoggedInUserCPF();

        if (!content.isEmpty()) {
            databaseHelper.addPost(userCPF, content);
            Toast.makeText(activity_create_post.this, "Postado!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity_create_post.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(activity_create_post.this, "ERRO!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getLoggedInUserCPF() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        return sharedPreferences.getString("UserCPF", null);
    }
}
