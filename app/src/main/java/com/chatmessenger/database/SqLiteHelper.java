package com.chatmessenger.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.chatmessenger.model.ChatMessageModel;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by prakashk on 7/8/2017.
 */

public class SqLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Chat";
    private static final int DATABASE_VERSION = 1;

    // Sender table name
    private static final String TABLE_SENDER = "sender_table";
    // Sender Table Columns names
    private static final String KEY_MSG = "msg_type";
    private static final String KEY_ROW_TYPE = "row_type";
    private static final String KEY_MSG_TEXT = "msg_text";
    private static final String KEY_MSG_TIME = "msg_time";


    public SqLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

       /* String CREATE_SENDER_TABLE = "CREATE TABLE " + TABLE_SENDER + "(" + KEY_MSG + "INTEGER," +
                KEY_ROW_TYPE + " INTEGER ," + KEY_MSG_TEXT + " TEXT,"
                + KEY_MSG_TIME + " INTEGER " + ")";*/
        String CREATE_SENDER_TABLE = "CREATE Table IF NOT EXISTS " +
                TABLE_SENDER + "  (" +
                "msg_type INTEGER," +
                "row_type INTEGER," +
                "msg_text TEXT," +
                "msg_time INTEGER" + ");";
        sqLiteDatabase.execSQL(CREATE_SENDER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SENDER);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // Adding new sender data
    public void addSenderData(ChatMessageModel chatMessageModel) {
        SQLiteDatabase db = SqLiteHelper.this.getWritableDatabase();

//        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_MSG, chatMessageModel.getMessageStatus());
            values.put(KEY_ROW_TYPE, chatMessageModel.getType_row());
            values.put(KEY_MSG_TEXT, chatMessageModel.getMessageText());
            values.put(KEY_MSG_TIME, chatMessageModel.getMessageTime());


            // Inserting Row
            db.insert(TABLE_SENDER, null, values);
            db.close(); // Closing database connection


        } catch (SQLException e) {

        }


    }

    // Getting sender data list
    public ArrayList<ChatMessageModel> getSendeData() {
        ArrayList<ChatMessageModel> chatList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SENDER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChatMessageModel chatMessageModel = new ChatMessageModel();
                /*chatMessageModel.setType_row(cursor.getInt(0));
                chatMessageModel.setMessageText(cursor.getString(1));
                chatMessageModel.setMessageTime(cursor.getLong(2));
                chatMessageModel.setMessageStatus(cursor.getInt(3));*/
                chatMessageModel.setMessageStatus(cursor.getInt(0));
                chatMessageModel.setType_row(cursor.getInt(1));
                chatMessageModel.setMessageText(cursor.getString(2));
                chatMessageModel.setMessageTime(cursor.getLong(3));

                chatList.add(chatMessageModel);
            } while (cursor.moveToNext());
        }
       /* try {
            SQLiteDatabase db = this.getReadableDatabase();

            if (doesTableExist(db, TABLE_SENDER)) {
                Cursor cursor = db.query(TABLE_SENDER, null,
                        null, null, null,
                        null, null);

                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {
                    ChatMessageModel chatMessageModel = new ChatMessageModel();
                    chatMessageModel.setType_row(cursor.getInt(0));
                    chatMessageModel.setMessageText(cursor.getString(1));
                    chatMessageModel.setMessageTime(cursor.getLong(2));
                    chatMessageModel.setMessageStatus(1);
                    Gson gson = new Gson();
                    Log.e("KK get", gson.toJson(chatMessageModel) + "");
                    chatList.add(chatMessageModel);
                    cursor.moveToNext();

                }
                cursor.close();
            }

        }catch (SQLException e){

        }*/

        return chatList;
    }

    public boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

}
