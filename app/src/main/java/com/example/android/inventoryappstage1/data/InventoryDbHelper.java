package com.example.android.inventoryappstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventoryappstage1.data.InventoryContract.InvEntry;

/**
 * Created by ahmed on 6/29/18.
 */


public class InventoryDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "inventor.db";
    public static final int DATABASE_VERSION = 1;

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + InvEntry.TABLE_NAME + " ("
                + InvEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InvEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + InvEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
                + InvEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                + InvEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " INTEGER NOT NULL DEFAULT 0, "
                + InvEntry.COLUMN_PRODUCT_SUPPLIER_PHONE + " INTEGER );";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InvEntry.TABLE_NAME);
    }
}
