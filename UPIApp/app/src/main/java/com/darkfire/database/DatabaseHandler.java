package com.darkfire.database;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.ls.LSInput;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.darkfire.Payment;

public class DatabaseHandler extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "MyPayment.db";
	//SQLiteDatabase db;
	public DatabaseHandler(Context context){
		super(context, DATABASE_NAME , null, 1);
	}
	
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table if not exists payments (payerVirAdd string,payeeVirAdd string,payerName string,payeeName string,tranId string primary key,amount double,paidStatus string)");
	}

	public boolean add(Payment payment){
		try{
			 SQLiteDatabase db = this.getWritableDatabase();
		      ContentValues contentValues = new ContentValues();
		      contentValues.put("payerVirAdd", payment.getPayerVirtualAdd());
		      contentValues.put("payeeVirAdd", payment.getPayeeVirtualAdd() );
		      contentValues.put("payerName", payment.getPayerName());
		      contentValues.put("payeeName",payment.getPayeeName());
		      contentValues.put("tranId", payment.getTransactionId());
		      contentValues.put("amount", payment.getAmount());
		      contentValues.put("paidStatus", payment.isPaid());
		      //contentValues.put("condition", cond);
		      db.insert("payments", null, contentValues);
			return true;
		}catch(SQLException e){
			Log.i("Erprfsdfljsldfdsljfl", e.getMessage());
			return false;
		}
}
	public List<String> getPayDeatil(){
		SQLiteDatabase db = this.getReadableDatabase();
		List<String> paymentDetail = new ArrayList<String>();
		Cursor c = db.rawQuery("SELECT payerName, payeeName, tranId, amount, paidStatus FROM payments",null);
		if(c.moveToFirst()){
			do{
				String pyName = c.getString(0);
				String peName = c.getString(1);
				String TId = c.getString(2);
				double amount = c.getDouble(3);
				String pStatus = c.getString(4);
				
				String next = pyName + "##" + peName + "##" + TId + "##" + amount + "##" + pStatus;
				paymentDetail.add(next);
			}while(c.moveToNext());
		}
		return paymentDetail;
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}
