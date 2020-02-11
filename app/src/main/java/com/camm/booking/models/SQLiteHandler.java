package com.camm.booking.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TABLE_IMAGE = "Image";
    private static final String IMAGE_ID = "Id";
    private static final String IMAGE_DATA = "Data";

    public SQLiteHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s BLOB)",
                TABLE_IMAGE, IMAGE_ID, IMAGE_DATA);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    // TODO: Save username
    public void insertImage(byte[] imageData){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO Image VALUES(null, ?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindBlob(1, imageData);
        statement.executeInsert();
    }
}
