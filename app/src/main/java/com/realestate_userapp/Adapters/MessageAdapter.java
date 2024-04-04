package com.realestate_userapp.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.realestate_userapp.Models.MessageModel;
import com.realestate_userapp.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MessageModel> messages;

    private Context context;

    String userId;

    SharedPreferences sharedPreferencesss;

    private static final int VIEW_TYPE_TEXT_RECEIVER = 1;
    private static final int VIEW_TYPE_TEXT_SENDER = 2;

    public MessageAdapter(List<MessageModel> messages,String userId, Context context) {
        this.messages = messages;
        this.userId = userId;
        this.context = context;
    }


    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        switch (viewType) {
            case VIEW_TYPE_TEXT_RECEIVER:
                View receiverTextView = inflater.inflate(R.layout.wrapper_sender, parent, false);
                return new TextReceiverViewHolder(receiverTextView);
            case VIEW_TYPE_TEXT_SENDER:
                View senderTextView = inflater.inflate(R.layout.wrapper_reciever, parent, false);
                return new TextSenderViewHolder(senderTextView);
            default:
                return null;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel message = messages.get(position);
        //holder.messageText.setText(message.getMessageText());

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_TEXT_RECEIVER:
                ((TextReceiverViewHolder) holder).bind(message);
                break;
            case VIEW_TYPE_TEXT_SENDER:
                ((TextSenderViewHolder) holder).bind(message);
                break;
        }
        // Set other message properties as needed
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messages.get(position);


        // Determine if the message is a text message
        if (message.getSenderUserId() != null && userId.equals(message.getSenderUserId())) {
            return VIEW_TYPE_TEXT_SENDER;
        } else {
            return VIEW_TYPE_TEXT_RECEIVER;
        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }



    static class TextReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMessageTextView;
        ImageView receiverImageView;

        TextReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMessageTextView = itemView.findViewById(R.id.message);
        }

        void bind(MessageModel message) {
            receiverMessageTextView.setText(message.getMessageText());


        }
    }

    static class TextSenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMessageTextView;

        TextSenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageTextView = itemView.findViewById(R.id.message);
        }

        void bind(MessageModel message) {
            senderMessageTextView.setText(message.getMessageText());
        }
    }

}
