package com.example.umar1_mdproject_mtg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class CardDB_Helper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String CARD_DB_NAME = "Card_DB.db";

    private static final String TABLE_USER_CARDS = "user_cards";
    private static final String C_COL_1 = "user_card_ID";
    private static final String C_COL_2 = "card_PK";
    private static final String C_COL_Z = "card_name";
    private static final String C_COL_3 = "card_set";
    private static final String C_COL_4 = "card_color";
    private static final String C_COL_5 = "card_qty";
    private static final String C_COL_6 = "card_price";
    private static final String C_COL_7 = "card_img";


    public CardDB_Helper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, CARD_DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String create_User_Card_Table = "CREATE TABLE " + TABLE_USER_CARDS + "(" +
                C_COL_1 + " Integer PRIMARY KEY AUTOINCREMENT," +
                C_COL_2 + " Text NOT NULL," +
                C_COL_Z + " Text NOT NULL," +
                C_COL_3 + " Text NOT NULL," +
                C_COL_4 + " Text NOT NULL," +
                C_COL_5 + " Text NOT NULL," +
                C_COL_6 + " Text NOT NULL," +
                C_COL_7 + " Text NOT NULL" + ");" ;
        Log.d("DBText","createCardTable: "+ create_User_Card_Table);
        sqLiteDatabase.execSQL(create_User_Card_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        this.onCreate(sqLiteDatabase);
        sqLiteDatabase.execSQL("Drop table " + TABLE_USER_CARDS + ";" );
    }

    //To add a row to a Table manually, do not use in Production, only for testing
    public void addCard(String cpk, String cn, String cs, String cc, String cqty, String cp, String cimg){

        ContentValues values= new ContentValues();

        values.put(C_COL_2, cpk);
        values.put(C_COL_Z, cn);
        values.put(C_COL_3, cs);
        values.put(C_COL_4, cc);
        values.put(C_COL_5, cqty);
        values.put(C_COL_6, cp);
        values.put(C_COL_7, cimg);

        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_USER_CARDS, null, values);

        db.close();
    }

    public void editQty(int loc, int newQty){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(C_COL_5, Integer.toString(newQty));
        db.update(TABLE_USER_CARDS, cv, "_id=?", new String[]{Integer.toString(loc)});
    }

    public void removeEntry(int loc){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_CARDS, "_id=?", new String[]{Integer.toString(loc)});
    }

    //To View Database (basically buts the whole table into the cursor which kind of acts like a buffer)
    public Cursor ViewData(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USER_CARDS, null);
        return c;
    }

    //Clears all rows use carefully
    public void DeleteCardRows(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USER_CARDS + ";");
    }
}