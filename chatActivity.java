package com.example.user.myapplication;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.*;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class chatActivity extends AppCompatActivity {
    Button sendbutton,imbutton,filmbutton;
    EditText ed1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        MyXMPP.getInstance().setActivity(this);
        MyXMPP.getInstance().createMessageListener();
        MyXMPP.getInstance().createFileListener();
        addListenerOnMButton();   //listener for sending messages
        addListenerOnIButton();   //listener for sending images
        addListenerOnFButton();  //listener for sending films
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            MyXMPP.getInstance().disconnect();
        } catch (Exception e) {

        }
    }
    public void addListenerOnMButton() {
        sendbutton = (Button) findViewById(R.id.sendbutton);
        ed1 = (EditText)findViewById(R.id.msg);
        final String finalBuddy = getBuddy();
        sendbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    MyXMPP.getInstance().sendMessage(ed1.getText().toString(), finalBuddy +"@lenovo");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

    }
    public void addListenerOnIButton() {

        imbutton = (Button) findViewById(R.id.imbutton);
        final String finalBuddy = getBuddy();
        imbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Resources res = getResources();
                Drawable drawable = res.getDrawable(R.drawable.hello);
                File image = MyXMPP.getInstance().getImage(drawable,getApplicationContext().getCacheDir());
                MyXMPP.getInstance().sendFile(image,finalBuddy);
            }

        });

    }
    public void addListenerOnFButton() {

        filmbutton = (Button) findViewById(R.id.filmbutton);
        final String finalBuddy = getBuddy();
        filmbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    MyXMPP.getInstance().sendFile(getFilm("vid.mp4"),finalBuddy);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }

    public String getBuddy()
    {
        Bundle extras = getIntent().getExtras();
        String user = extras.getString("username");
        String buddy = null;
        if (user.equals("user1"))
            buddy = "user2";
        else
            buddy = "user1";
        return buddy;
    }

    private File getFilm(String fileName) throws Exception {
        File file = new File(getApplicationContext().getCacheDir(),fileName);
        InputStream ins = getResources().openRawResource(R.raw.video);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int size = 0;
        // Read the entire resource into a local byte buffer.
        byte[] buffer = new byte[1024];
        while ((size = ins.read(buffer, 0, 1024)) >= 0) {
            outputStream.write(buffer, 0, size);
        }
        ins.close();
        buffer = outputStream.toByteArray();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(buffer);
        fos.close();
        return file;}
}
