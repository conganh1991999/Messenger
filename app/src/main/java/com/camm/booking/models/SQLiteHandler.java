package com.camm.booking.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TABLE_FRIEND = "Friend";
    private static final String FRIEND_ID = "Id";
    private static final String FRIEND_NAME = "Name";
    private static final String FRIEND_IMAGE = "Image";
    private static final String FRIEND_MESSAGE = "Message";
    private static final String MESSAGE_DATE = "Date";

    public SQLiteHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE %s (%s VARCHAR PRIMARY KEY, %s VARCHAR, %s VARCHAR, %s VARCHAR, %s VARCHAR)",
                TABLE_FRIEND, FRIEND_ID, FRIEND_NAME, FRIEND_IMAGE, FRIEND_MESSAGE, MESSAGE_DATE);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public void updateLatestMessage(String friendId, String Message, String messageDate){
        SQLiteDatabase db = getWritableDatabase();
        String sql1 = "UPDATE Friend SET Message ='" + Message + "'";
        String sql2 = "UPDATE Friend SET Date ='" + messageDate + "'";
        db.execSQL(sql1);
        db.execSQL(sql2);
    }
}

//    // usage
//    database = new SQLiteHandler(this, "friends.sqlite", null, 1);
//    SQLiteDatabase db = database.getReadableDatabase();
//    Cursor cursorFriend = db.query("Friend", null, null, null, null, null, null);
