package com.example.user.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.*;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {
    Button button;
    EditText ed1,ed2;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            MyXMPP.getInstance().disconnect();
        } catch (Exception e) {

        }
    }
    public void addListenerOnButton() {

        button = (Button) findViewById(R.id.button);
        ed1 = (EditText)findViewById(R.id.editText);
        ed2 = (EditText)findViewById(R.id.editText2);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String username =  ed1.getText().toString();
                final String password =  ed2.getText().toString();
                new Thread(new Runnable() {
                    public void run() {
                        if(MyXMPP.getInstance().login(username, password))
                        {
                            Intent myIntent = new Intent(MainActivity.this,
                                    chatActivity.class);
                            myIntent.putExtra("username",username);
                            startActivity(myIntent);
                        }
                    }
                }).start();


              /*  Resources res = getResources();
                Drawable drawable = res.getDrawable(R.drawable.hello);
                c.sendFile(drawable,getApplicationContext().getCacheDir(),"user2");   */
              /*  try {
                    c.sendMessage("Hello","user2@lenovo");
                } catch (XMPPException e) {
                    e.printStackTrace();
                }   */
            }

        });

    }
}
