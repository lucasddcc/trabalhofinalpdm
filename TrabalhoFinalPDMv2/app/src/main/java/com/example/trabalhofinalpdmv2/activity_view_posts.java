package com.example.trabalhofinalpdmv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class activity_view_posts extends AppCompatActivity {

    private ListView listViewPosts;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_posts);

        listViewPosts = findViewById(R.id.listViewPosts);
        databaseHelper = new DatabaseHelper(this);


        Button btnToCreatePost = findViewById(R.id.btnVoltar);
        btnToCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_view_posts.this, MainActivity.class);
                startActivity(intent);
            }
        });
        loadPosts();
    }

    private void loadPosts() {
        try {
            ArrayList<String> posts = databaseHelper.getAllPosts();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_post, R.id.tvPostItem, posts);
            listViewPosts.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("ActivityViewPosts", "Erro ao carregar posts", e);
        }
    }
}
