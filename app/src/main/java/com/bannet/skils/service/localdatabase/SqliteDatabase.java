package com.bannet.skils.service.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bannet.skils.explore.response.CategoryLocalREsponse;

import java.util.ArrayList;
public class SqliteDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "Contacts";
    private static final String TABLE_CONTACTS = "Contacts";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "contactName";
    private static final String COLUMN_IMAGE = "phoneNumber";
    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE "
                + TABLE_CONTACTS + "(" + COLUMN_ID
                + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }
    public ArrayList<CategoryLocalREsponse> listContacts() {
        String sql = "select * from " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CategoryLocalREsponse> storeContacts = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                //int id = Integer.parseInt(cursor.getString(0));
                String cId = cursor.getString(0);
                String cName = cursor.getString(1);
                String cImage = cursor.getString(2);
                Log.e("cImage",cImage);

                storeContacts.add(new CategoryLocalREsponse(cId, cName, cImage));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeContacts;
    }
    public void addContacts(CategoryLocalREsponse category) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, category.getName());
        values.put(COLUMN_ID, category.getId());
        values.put(COLUMN_IMAGE, category.getImage());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CONTACTS, null, values);
    }
    void updateContacts(CategoryLocalREsponse category) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, category.getName());
        values.put(COLUMN_IMAGE, category.getId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_CONTACTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(category.getId())});
    }
    void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
    public void  deletaAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_CONTACTS);
    }
}