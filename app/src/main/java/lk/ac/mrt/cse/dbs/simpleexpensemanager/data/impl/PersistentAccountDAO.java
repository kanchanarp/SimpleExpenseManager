package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
/**
 * Created by kanchanaR on 12/4/2015.
 */
public class PersistentAccountDAO implements AccountDAO {
    private final DBHelper dbHelper;

    public PersistentAccountDAO(Context context) {
        dbHelper=new DBHelper(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> account_list = new ArrayList<String>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor res=db.rawQuery("select accountNo from Account",null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            account_list.add(res.getString(0));
            res.moveToNext();
        }
        return account_list;
    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> account_list = new ArrayList<Account>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor res=db.rawQuery("select * from Account",null);
        res.moveToFirst();
        String AccountNo;
        String BankName;
        String AccountHolder;
        double Amount;
        while(res.isAfterLast() == false){
            AccountNo=res.getString(0);
            BankName=res.getString(1);
            AccountHolder=res.getString(2);
            Amount=res.getDouble(3);
            Account account=new Account(AccountNo,BankName,AccountHolder,Amount);
            account_list.add(account);
            res.moveToNext();
        }
        return account_list;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor res=db.rawQuery("select * from Account where accountNo=?",new String[] {accountNo});
        res.moveToFirst();
        String AccountNo;
        String BankName;
        String AccountHolder;
        double Amount;
        if(res.getCount()>0){
            AccountNo=res.getString(0);
            BankName=res.getString(1);
            AccountHolder=res.getString(2);
            Amount=res.getDouble(3);
            Account account=new Account(AccountNo,BankName,AccountHolder,Amount);
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo", account.getAccountNo());
        contentValues.put("bankName", account.getBankName());
        contentValues.put("accountHolderName", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());
        db.insert("Account", null, contentValues);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.delete("Account","accountNo=?",new String[] {accountNo})==0) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Account account = getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("balance", account.getBalance());
        db.update("Account", contentValues,"accountNo = ?",new String[] {accountNo});
    }
}
