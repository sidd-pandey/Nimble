package com.darkfire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.zxing.client.android.R;


public class MainActivity extends Activity {

    Button regis, login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        regis = (Button) findViewById(R.id.registerbtn);
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registration = new Intent(MainActivity.this, Registration.class);
                startActivity(registration);
            }
        });
        login = (Button)findViewById(R.id.loginbtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentUser user = CurrentUser.getInstance();
                user.setMobile(((EditText)findViewById(R.id.mobileno)).getText().toString());
                user.setPassword(((EditText)findViewById(R.id.password)).getText().toString());
                new LoginUserTask(MainActivity.this).execute(CurrentUser.getInstance());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

}
