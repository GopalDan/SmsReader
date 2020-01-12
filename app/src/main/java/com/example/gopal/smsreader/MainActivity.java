package com.example.gopal.smsreader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.gopal.smsreader.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";

    ArrayList<String> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap = new HashMap<>();

    private static final int REQUEST_READ_CONTACTS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            //mobileArray = getAllContacts();
            //int size = mobileArray.size();

            ArrayList<Message> messageArrayList = readUserMessagesNew();
            int size = messageArrayList.size();
            ArrayList<String> onlyMessageList = new ArrayList<>();
            ArrayList<String> onlyMessageAddress = new ArrayList<>();
            for (Message message : messageArrayList) {
                if (!onlyMessageList.contains(message.getMsg())) {
                    onlyMessageList.add(message.getMsg());
                    onlyMessageAddress.add(message.getAddress());
                }

            }

            int size1 = onlyMessageList.size();
            int size2 = onlyMessageAddress.size();
            ArrayList<String> uniqueAddressList = new ArrayList<>();
            for (String address : onlyMessageAddress) {
                if (!uniqueAddressList.contains(address))
                    uniqueAddressList.add(address);
            }

            int size3 = uniqueAddressList.size();
            for (String address : uniqueAddressList) {
                String messageBody = hashMap.get(address);
                String s1 = messageBody;
            }


            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            MessageAdapter messageAdapter = new MessageAdapter(onlyMessageList, onlyMessageAddress);
            recyclerView.setAdapter(messageAdapter);


            extractDataFromMessage();


        } else {
            requestPermission();
        }

    }

    private ArrayList<Message> readUserMessages() {
        ArrayList<Message> messageArrayList = new ArrayList<>();
        Message message;
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int totalSms = cursor.getCount();

        if (cursor.moveToFirst()) {

            for (int i = 0; i < totalSms; i++) {
                message = new Message();
                message.setId(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                message.setAddress(cursor.getString(cursor
                        .getColumnIndexOrThrow("address")));
                message.setMsg(cursor.getString(cursor.getColumnIndexOrThrow("body")));
                message.setReadState(cursor.getString(cursor.getColumnIndex("read")));
                message.setTime(cursor.getString(cursor.getColumnIndexOrThrow("date")));

                messageArrayList.add(message);
                cursor.moveToNext();

            }

        }
        cursor.close();
        return messageArrayList;

    }

    private ArrayList<Message> readUserMessagesNew() {
        ArrayList<Message> messageArrayList = new ArrayList<>();
        Message message;
        Cursor cursor = null;
        Uri path = Uri.parse("content://sms/inbox");
        String[] COLUMNS = {"Distinct address", "body"};
        String selection = "address IS NOT NULL) GROUP BY (address";
        //  https://stackoverflow.com/questions/2315203/android-distinct-and-groupby-in-contentresolver
        //  String projection = COLUMN_NAME + "'a_condition' GROUP BY " + COLUMN_NAME"

        try {
            cursor = getContentResolver().query(path, COLUMNS, selection, null, null);

        } catch (Exception ex) {
            Log.e(TAG, "readUserMessagesNew: ");
        }
        int totalSms = cursor.getCount();
        String address = null;
        String body = null;

        if (cursor.moveToFirst()) {

            for (int i = 0; i < totalSms; i++) {
                message = new Message();
                int addressColumnIndex = cursor.getColumnIndexOrThrow("address");
                int bodyColumnIndex = cursor.getColumnIndexOrThrow("body");
                address = cursor.getString(addressColumnIndex);
                body = cursor.getString(bodyColumnIndex);
                message.setAddress(address);
                message.setMsg(body);

                hashMap.put(address, body);

                messageArrayList.add(message);
                cursor.moveToNext();

            }

        }

        cursor.close();
        int size = messageArrayList.size();
        int hashSize = hashMap.size();
        return messageArrayList;

    }


    private void extractDataFromMessage() {
        String message = "Your a/c no. XXXXXXXX0048 is debited for Rs. 40.00 on 09-11-19 20:03:26 (UPI Ref no 931380481371).";
        String regex = "a\\/c\\s*no\\.?\\s*(X*\\d*)\\s*is\\s*debited\\s*for\\s*Rs.\\s*([\\d\\.\\,]*)\\s*on\\s*([\\d\\-\\:\\s]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {

            String accNumber = matcher.group(1);
            String transaction = matcher.group(2);
            String date = matcher.group(3);
            Log.e(TAG, "extractDataFromMessage: " + "accNumber: " + accNumber + " transaction: " + transaction + " date: " + date);
        }
    }

    private void readUserMeassages1() {
        // public static final String INBOX = "content://sms/inbox";
// public static final String SENT = "content://sms/sent";
// public static final String DRAFT = "content://sms/draft";
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                }
                arrayList.add(msgData);
                // use msgData
                Log.e(TAG, "message: " + msgData);
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }

        int size = arrayList.size();
    }


    private void requestPermission() {
       /* if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }*/
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
                REQUEST_READ_CONTACTS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // mobileArray = getAllContacts();
                    // int size = mobileArray.size();
                    readUserMessages();
                } else {
                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
