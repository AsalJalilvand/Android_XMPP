package com.example.user.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
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

public class JabberSmackAPI  {
        //implements ChatMessageListener {

   /* XMPPTCPConnection connection;
    FileTransferManager transferManager;

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
                .setHost("10.0.2.2")
                .setPort(5222)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setHostnameVerifier(verifier)
                .setServiceName("10.0.2.2")
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

    public void sendMessage(String message, String to) throws XMPPException {
        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        chatManager.addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                chat.addMessageListener(new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) {
                        if (message.getType() == Message.Type.chat || message.getType() == Message.Type.normal) {
                            if (message.getBody() != null) {
                                System.out.println(chat.getParticipant() + " says: " + message.getBody());
                            }
                        }
                    }
                });
            }
        });
        Chat chat = chatManager.createChat(to, this);
        //JidCreate.from(to)
        try {
            chat.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void sendFile(Drawable image, File cache, String to) {
        transferManager = FileTransferManager.getInstanceFor(connection);
        //this line chooses IBB between IBB and Bytestream negotiation method between two parties
        FileTransferNegotiator.IBB_ONLY = true;
        //configure entity that we want to send file to
        OutgoingFileTransfer oft = transferManager.createOutgoingFileTransfer(XmppStringUtils.completeJidFrom(to, "lenovo", "Lenovo"));
        try {
            //gets a File object from drawable and sends it to destination
            oft.sendFile(getImage(image, cache), "");
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

    public void listeningForMessages() {
        try {
            PacketFilter filter = (PacketFilter) new AndFilter(new PacketTypeFilter(Message.class));
            PacketCollector collector = connection.createPacketCollector(filter);
            Packet packet = collector.nextResult();
            if (packet instanceof Message) {
                Message message = (Message) packet;
                if (message != null && message.getBody() != null) {
                    System.out.println("Received message from "
                            + packet.getFrom() + " : "
                            + (message != null ? message.getBody() : "NULL"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    } */

}
