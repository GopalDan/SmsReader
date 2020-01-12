package com.example.gopal.smsreader;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gopal on 11/10/2019.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    ArrayList<Message>  messageArrayList;
    ArrayList<String> messageList;
    ArrayList<String> messageAddressList;

    public MessageAdapter( ArrayList<String> messageList,  ArrayList<String> messageAddressList){
        this.messageList = messageList;
        this.messageAddressList = messageAddressList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
      holder.messageIdTextView.setText(messageAddressList.get(position));
      holder.messageTextView.setText(messageList.get(position));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView messageIdTextView, messageTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageIdTextView = itemView.findViewById(R.id.message_id_text_view);
            messageTextView = itemView.findViewById(R.id.message_text_view);
        }
    }
}
