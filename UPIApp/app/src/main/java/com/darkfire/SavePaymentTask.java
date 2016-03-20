package com.darkfire;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by Siddharth on 3/19/2016.
 */
public class SavePaymentTask extends AsyncTask<Payment, Integer, Boolean>{

    Context context;

    SavePaymentTask(Context context){
        this.context = context;
    }

    protected Boolean doInBackground(Payment... payments) {

        ObjectOutputStream os = null;

        FileOutputStream fos;
        try {
            File path = context.getFileStreamPath(CurrentUser.getInstance().getMobile());
            if(path == null){
                return false;
            }
            if(!path.exists()){
                //file does not exist
                try {
                    boolean result = path.createNewFile();
                    if(!result)
                        return false;
                    fos = context.openFileOutput(CurrentUser.getInstance().getMobile(), context.MODE_APPEND);
                    os = new ObjectOutputStream(fos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                fos = context.openFileOutput(CurrentUser.getInstance().getMobile(), context.MODE_APPEND);
                os = new AppendingObjectOutputStream(fos);
            }

            //ObjectOutputStream os = new ObjectOutputStream(fos);
            //os = new AppendingObjectOutputStream(fos);
            for (Payment p : payments){
                os.writeObject(p);
            }
            os.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean){
            Toast.makeText(context, "Paymennt Saved!", Toast.LENGTH_SHORT).show();
        }
    }
    public class AppendingObjectOutputStream extends ObjectOutputStream {

        public AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            // do not write a header, but reset:
            // this line added after another question
            // showed a problem with the original
            reset();
        }
    }
}
