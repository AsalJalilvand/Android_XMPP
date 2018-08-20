package com.example.user.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.Toast;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.filetransfer.*;
import org.jxmpp.util.XmppStringUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

public class MyXMPP implements ChatMessageListener {
    private static MyXMPP ourInstance = new MyXMPP();
    XMPPTCPConnection connection;
    ChatManager chatManager;
    FileTransferManager transferManager;
    Activity activity;

    public static MyXMPP getInstance() {
        return ourInstance;
    }

    private MyXMPP() {
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean login(String userName, String password) {
        HostnameVerifier verifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return false;
            }
        };
        XMPPTCPConnectionConfiguration config;
        //10.0.2.2
        //192.168.1.1
        config = XMPPTCPConnectionConfiguration.builder()
                .setHost("192.168.1.3")
                .setPort(5222)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setHostnameVerifier(verifier)
                .setServiceName("192.168.1.3")
                .setDebuggerEnabled(true)
                .build();
        connection = new XMPPTCPConnection(config);

        try {
            connection.connect();
            connection.login(userName, password);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public void createMessageListener()
    {
        chatManager = ChatManager.getInstanceFor(connection);
        chatManager.addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                chat.addMessageListener(new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) {
                        if (message.getType() == Message.Type.chat || message.getType() == Message.Type.normal) {
                            if (message.getBody() != null) {
                                System.out.println(chat.getParticipant() + " says: " + message.getBody());
                                showMessage(message.getBody());
                            }
                        }
                    }
                });
            }
        });
    }

    public void sendMessage(String message, String to) throws XMPPException {
        Chat chat = chatManager.createChat(to, this);
        //JidCreate.from(to)
        try {
            chat.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void sendFile(File file, String to) {
        //this line chooses IBB between IBB and Bytestream negotiation method between two parties
        FileTransferNegotiator.IBB_ONLY = true;
        //configure entity that we want to send file to
        OutgoingFileTransfer oft = transferManager.createOutgoingFileTransfer(XmppStringUtils.completeJidFrom(to, "lenovo", "Smack"));
        try {
            //gets a File object from drawable and sends it to destination
            oft.sendFile(file, "");
        } catch (SmackException e) {
            e.printStackTrace();
        }
        //we can monitor delivery state with the following code and give notification to user if needed
        int rs = oft.getStatus().compareTo(FileTransfer.Status.complete);
        long timeOut = 60000;
        long sleepMin = 3000;
        long spTime = 0;
        while (rs != 0) {
            rs = oft.getStatus().compareTo(FileTransfer.Status.complete);
            spTime = spTime + sleepMin;
            if (spTime > timeOut) {
                return;
            }
            try {
                Thread.sleep(sleepMin);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void createFileListener()
    {
        transferManager = FileTransferManager.getInstanceFor(connection);
        FileTransferNegotiator.IBB_ONLY = true;
        transferManager.addFileTransferListener(new FileTransferListener() {
            public void fileTransferRequest(final FileTransferRequest request) {
                new Thread(){
                    @Override
                    public void run() {
                        IncomingFileTransfer transfer = request.accept();
                        File path = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS);
                        File file = new File(path, "/" +  transfer.getFileName());
                        try{
                            transfer.recieveFile(file);
                            while(!transfer.isDone()) {
                                try{
                                    Thread.sleep(1000L);
                                }catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if(transfer.getStatus().equals(FileTransfer.Status.error)) {
                                   System.out.println(transfer.getError());
                                }
                                if(transfer.getException() != null) {
                                    transfer.getException().printStackTrace();
                                }
                            }
                            showMessage("New File Received!");
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                }.start();
            }
        });
    }

    public void displayBuddyList() {
        Roster roster = Roster.getInstanceFor(connection);
        Collection<RosterEntry> entries = roster.getEntries();

        System.out.println("\n\n" + entries.size() + " buddy(ies):");
        for (RosterEntry r : entries) {
            System.out.println(r.getUser());
        }
    }

    public void disconnect() {
        connection.disconnect();
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        if (message.getType() == Message.Type.chat) {
            System.out.println(chat.getParticipant() + " says: " + message.getBody());
        }
    }

    public File getImage(Drawable image, File cacheDir) {

        File f = new File(cacheDir, "temp.png");
        //convert drawable to bitmap
        Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
        //Convert bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] bitMapData = stream.toByteArray();
        //write the bytes in file
        try {
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitMapData);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }


    public void showMessage(final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
        // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
