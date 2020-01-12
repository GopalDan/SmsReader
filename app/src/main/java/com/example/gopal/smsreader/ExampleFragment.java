package com.example.gopal.smsreader;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExampleFragment extends Fragment {


    public ExampleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_example, container, false);

        ArrayList<Message> messageArrayList = readUserMessages();
        int size = messageArrayList.size();
        ArrayList<String> onlyMessageList = new ArrayList<>();
        ArrayList<String> onlyMessageAddress = new ArrayList<>();
        for(Message message : messageArrayList){
            if(!onlyMessageList.contains(message.getMsg())){
                onlyMessageList.add(message.getMsg());
                onlyMessageAddress.add(message.getAddress());
            }

        }
        RecyclerView recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MessageAdapter messageAdapter = new MessageAdapter(onlyMessageList, onlyMessageAddress);
        recyclerView.setAdapter(messageAdapter);

        return view;
    }

    private ArrayList<Message> readUserMessages() {
        ArrayList<Message> messageArrayList = new ArrayList<>();
        Message message;
        Cursor cursor = getContext().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
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

}
