package com.example.cinefast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SnackDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cinefast.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SNACKS = "snacks";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE = "image";

    private Context context;

    public SnackDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_SNACKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_IMAGE + " TEXT)";
        db.execSQL(createTable);

        // Insert initial snack data
        insertInitialSnacks(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNACKS);
        onCreate(db);
    }

    private void insertInitialSnacks(SQLiteDatabase db) {
        insertSnack(db, "Popcorn", 8.99, "popcorn");
        insertSnack(db, "Nachos", 7.99, "nachos");
        insertSnack(db, "Soft Drink", 5.99, "soft_drink");
        insertSnack(db, "Candy Mix", 6.99, "candy");
    }

    private void insertSnack(SQLiteDatabase db, String name, double price, String image) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE, image);
        db.insert(TABLE_SNACKS, null, values);
    }

    public List<Snack> getAllSnacks() {
        List<Snack> snackList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SNACKS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                String imageName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));

                int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                if (resId == 0) {
                    resId = R.drawable.popcorn; // fallback
                }

                snackList.add(new Snack(id, name, price, resId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return snackList;
    }
}
