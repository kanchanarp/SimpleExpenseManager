package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by kanchanaR on 12/4/2015.
 */
public class DBHelper extends SQLiteOpenHelper{
    public static final String DB_NAME = "130519D.db";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Account (accountNo TEXT PRIMARY KEY ,bankName TEXT,accountHolderName TEXT,balance DECIMAL(12,2))");
        db.execSQL("create table Transaction (ID INTEGER PRIMARY KEY AUTOINCREMENT,date DATE,accountNo TEXT,expenseType BOOLEAN,amount DECIMAL(12,2), FOREIGN KEY (accountNo) REFERENCES Account(accountNo))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Account");
        db.execSQL("DROP TABLE IF EXISTS Transaction");
        onCreate(db);
    }

}
