package com.example.trabalhofinalpdmv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AppDatabase.db";
    private static final int DATABASE_VERSION = 4;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (cpf TEXT PRIMARY KEY, name TEXT, email TEXT, password TEXT)");
        db.execSQL("CREATE TABLE posts (id INTEGER PRIMARY KEY AUTOINCREMENT, cpf TEXT, content TEXT, FOREIGN KEY (cpf) REFERENCES users(cpf))");
        db.execSQL("CREATE TABLE user_settings ( cpf TEXT PRIMARY KEY, notifications INTERGER DEFAULT 0, email_notifications INTERGER DEFAULT 0, FOREIGN KEY (cpf) REFERENCES users(cpf))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS posts");
        db.execSQL("DROP TABLE IF EXISTS user_settings");
        onCreate(db);
    }


    public void deleteAllPosts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM posts");
        db.close();
    }
    public ArrayList<String> getAllUsers() {
        ArrayList<String> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);

        if (cursor.moveToFirst()) {
            do {
                int cpfIndex = cursor.getColumnIndex("cpf");
                int nameIndex = cursor.getColumnIndex("name");
                int emailIndex = cursor.getColumnIndex("email");

                if (cpfIndex != -1 && nameIndex != -1 && emailIndex != -1) {
                    String userData = "CPF: " + cursor.getString(cpfIndex) +
                            ", Nome: " + cursor.getString(nameIndex) +
                            ", Email: " + cursor.getString(emailIndex);
                    users.add(userData);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }
    public void addPost(String cpf, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cpf", cpf);
        values.put("content", content);
        db.insert("posts", null, values);
    }


    public ArrayList<String> getAllPosts() {
        ArrayList<String> posts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT users.name, posts.content FROM posts INNER JOIN users ON users.cpf = posts.cpf";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String post = cursor.getString(0) + " postou:\n" + cursor.getString(1);
                posts.add(post);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return posts;
    }

    public boolean updateUserDetails(String cpf, String newName, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("email", newEmail);

        int updateStatus = db.update("users", values, "cpf=?", new String[]{cpf});
        db.close();
        return updateStatus > 0;
    }
    public HashMap<String, String> getUserDetails(String cpf) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"name", "email", "cpf"}, "cpf=?", new String[]{cpf}, null, null, null);
        HashMap<String, String> userDetails = new HashMap<>();

        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("name");
            int emailIndex = cursor.getColumnIndex("email");
            int cpfIndex = cursor.getColumnIndex("cpf");

            if (nameIndex != -1 && emailIndex != -1 && cpfIndex != -1) {
                userDetails.put("name", cursor.getString(nameIndex));
                userDetails.put("email", cursor.getString(emailIndex));
                userDetails.put("cpf", cursor.getString(cpfIndex));
            }

            cursor.close();
            return userDetails;
        }

        if (cursor != null) {
            cursor.close();
        }

        return null; // Retorna null se o usuário não for encontrado
    }

    public void saveUserSetting(String cpf, String settingName, boolean value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(settingName, value ? 1 : 0);

        int rowsAffected = db.update("user_settings", values, "cpf=?", new String[]{cpf});
        if (rowsAffected == 0) {
            values.put("cpf", cpf);
            db.insert("user_settings", null, values);
        }
    }

    public HashMap<String, Boolean> loadUserSettings(String cpf) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("user_settings", new String[]{"notifications", "email_notifications"}, "cpf=?", new String[]{cpf}, null, null, null);

        HashMap<String, Boolean> settings = new HashMap<>();
        if (cursor != null && cursor.moveToFirst()) {
            int notifIndex = cursor.getColumnIndex("notifications");
            int emailNotifIndex = cursor.getColumnIndex("email_notifications");
            // Verificar se os índices das colunas são válidos
            if (notifIndex != -1 && emailNotifIndex != -1) {
                boolean notifications = cursor.getInt(notifIndex) == 1;
                boolean emailNotifications = cursor.getInt(emailNotifIndex) == 1;

                settings.put("notifications", notifications);
                settings.put("email_notifications", emailNotifications);
            }
            cursor.close();
            return settings;
        }
        if (cursor != null) {
            cursor.close();
        }
        return settings; // Retorna um mapa vazio se não encontrar configurações
    }


}



