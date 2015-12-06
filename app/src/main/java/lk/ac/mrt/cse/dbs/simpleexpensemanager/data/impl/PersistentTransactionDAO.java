package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import java.text.ParseException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
/**
 * Created by kanchanaR on 12/4/2015.
 */
public class PersistentTransactionDAO implements TransactionDAO{
    private final DBHelper dbHelper;

    public PersistentTransactionDAO(Context context) {
        dbHelper=new DBHelper(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date.getTime());
        contentValues.put("accountNo", accountNo);
        if(expenseType==ExpenseType.INCOME){
            contentValues.put("expenseType", true);
        }
        else {
            contentValues.put("expenseType", false);
        }
        contentValues.put("amount", amount);
        db.insert("Transaction",null ,contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        ArrayList<Transaction> transactions= new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor res=db.rawQuery("select * from Transaction order by date desc",null);
        res.moveToFirst();
        Transaction transaction;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        String accountNo;
        ExpenseType expenseType;
        double amount;
        while(res.isAfterLast() == false){
            try {
                date=format.parse(format.format(new Date(res.getLong(1))));
                accountNo=res.getString(2);
                if(res.getInt(3)>0){
                    expenseType=ExpenseType.INCOME;
                }else{
                    expenseType=ExpenseType.EXPENSE;
                }
                amount=res.getDouble(4);
                transaction = new Transaction(date, accountNo, expenseType, amount);
                transactions.add(transaction);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            res.moveToNext();
        }
        return transactions;

    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        ArrayList<Transaction> transactions= new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor res=db.rawQuery("select * from Transaction order by date desc limit "+Integer.toString(limit),null);
        res.moveToFirst();
        Transaction transaction;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        String accountNo;
        ExpenseType expenseType;
        double amount;
        while(res.isAfterLast() == false){
            try {
                date=format.parse(format.format(new Date(res.getLong(1))));
                accountNo=res.getString(2);
                if(res.getInt(3)>0){
                    expenseType=ExpenseType.INCOME;
                }else{
                    expenseType=ExpenseType.EXPENSE;
                }
                amount=res.getDouble(4);
                transaction = new Transaction(date, accountNo, expenseType, amount);
                transactions.add(transaction);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            res.moveToNext();
        }
        return transactions;

    }
}
