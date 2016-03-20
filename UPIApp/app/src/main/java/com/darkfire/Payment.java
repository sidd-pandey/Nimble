package com.darkfire;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Siddharth on 2/24/2016.
 */
public class Payment implements Serializable{
    private String payerVirtualAdd;
    private String payeeVirtualAdd;
    private String payerName;
    private String payeeName;
    private String payerIdNumber;
    private String payeeIdNumber;
    String transactionId;
    String transactionDesc;
    private double amount;
    private boolean payed = false;
    private boolean verified = false;
    private ArrayList<BillElement> bill;

    private static final long serialVersionUID = 2L;

    public static boolean offline = true;

    public static final String PAYER_NAME = "PAYER_NAME";
    public static final String PAYEE_NAME = "PAYEE_NAME";
    public static final String PAYER_VIRTUAL_ADD = "PAYER_VIRTUAL_ADD";
    public static final String PAYEE_VIRTUAL_ADD = "PAYEE_VIRTUAL_ADD";
    public static final String PAYER_ID_NUMBER = "PAYER_ID_NUMBER";
    public static final String PAYEE_ID_NUMBER = "PAYEE_ID_NUMBER";
    public static final String TRANSACTION_ID = "TRANSACTION_ID";
    public static final String TRANSACTION_DESC = "TRANSACTION_DESC";
    public static final String AMOUNT = "AMOUNT";
    public static final String BILL = "BILL";


   Payment(){
        bill = new ArrayList<>();
   }

    public boolean makePaymentObj(HashMap<String, String> vals){
        if(!isValidArgs(vals)){
            return false;
        }

        payerVirtualAdd = vals.get(PAYER_VIRTUAL_ADD);
        payeeVirtualAdd = vals.get(PAYEE_VIRTUAL_ADD);
        payerName = vals.get(PAYER_NAME);
        payeeName = vals.get(PAYEE_NAME);
        payerIdNumber = vals.get(PAYER_ID_NUMBER);
        payeeIdNumber = vals.get(PAYEE_ID_NUMBER);
        transactionId = vals.get(TRANSACTION_ID);
        transactionDesc = vals.get(TRANSACTION_DESC);

        amount = Double.parseDouble(vals.get(AMOUNT));

        return true;
    }

    public boolean makePaymentObj(String[] vals){
        if(!isValidArgs(vals))
            return false;
        payerVirtualAdd = vals[0];
        payeeVirtualAdd = vals[1];
        payerName = vals[2];
        payeeName = vals[3];
        payerIdNumber = vals[4];
        payeeIdNumber = vals[5];
        amount = Double.parseDouble(vals[6]);
        return true;
    }

    private boolean isValidArgs(String[] vals){
        return true;
    }
    private boolean isValidArgs(HashMap<String, String> vals){
        return true;
    }

    public String getPayerVirtualAdd() {
        return payerVirtualAdd;
    }

    public Payment setPayerVirtualAdd(String payerVirtualAdd) {
        this.payerVirtualAdd = payerVirtualAdd;
        return this;
    }

    public String getPayeeVirtualAdd() {
        return payeeVirtualAdd;
    }

    public Payment setPayeeVirtualAdd(String payeeVirtualAdd) {
        this.payeeVirtualAdd = payeeVirtualAdd;
        return this;
    }

    public String getPayerName() {
        return payerName;
    }

    public Payment setPayerName(String payerName) {
        this.payerName = payerName;
        return this;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public Payment setPayeeName(String payeeName) {
        this.payeeName = payeeName;
        return this;
    }

    public String getPayerIdNumber() {
        return payerIdNumber;
    }

    public Payment setPayerIdNumber(String payerIdNumber) {
        this.payerIdNumber = payerIdNumber;
        return this;
    }

    public String getPayeeIdNumber() {
        return payeeIdNumber;
    }

    public Payment setPayeeIdNumber(String payeeIdNumber) {
        this.payeeIdNumber = payeeIdNumber;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public Payment setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public String getTransactionId(){
        return transactionId;
    }
    public String getTransactionDesc(){
        return transactionDesc;
    }

    public boolean verify() {
        if (offline) {
            verified = true;
            return true;
        }
        else{
            //make a contact to http upi server and verify the details
            return false;
        }
    }

    public boolean isVerified(){
        return verified;
    }

    public boolean pay(){
        if(offline && isVerified()) {
            payed = true;
        }
        //make a contact to http upi server and proceed with the transaction
        return false;

    }
    public boolean isPaid(){
        return payed;
    }

    public void setBill(ArrayList<BillElement> bill){
        this.bill = bill;
    }
    public List<BillElement> getBill(){
        return bill;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "amount=" + amount +
                ", transactionDesc='" + transactionDesc + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", payeeIdNumber='" + payeeIdNumber + '\'' +
                ", payerIdNumber='" + payerIdNumber + '\'' +
                ", payeeName='" + payeeName + '\'' +
                ", payerName='" + payerName + '\'' +
                ", payeeVirtualAdd='" + payeeVirtualAdd + '\'' +
                ", payerVirtualAdd='" + payerVirtualAdd + '\'' +
                '}';
    }
}
