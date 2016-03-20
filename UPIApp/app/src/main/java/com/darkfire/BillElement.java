package com.darkfire;

import java.io.Serializable;

/**
 * Created by Siddharth on 3/9/2016.
 */
public class BillElement implements Serializable {
    String name;
    int noOfUnits;
    double costPerUnit;
    double amount;

    double calculateAmount(){
        amount = noOfUnits*costPerUnit;
        return amount;
    }


}
