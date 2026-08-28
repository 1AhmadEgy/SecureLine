package com.secureline.secureline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.secureline.secureline.util.DateUtils;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 0;

    private final List<String> messages;

    public MessageAdapter(List<String> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_sent, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String message = messages.get(position);
        String displayText = message;
        long timestamp = System.currentTimeMillis();

        if (message.startsWith("[")) {
            int closingBracket = message.indexOf("]");
            if (closingBracket > 0) {
                String timestampStr = message.substring(1, closingBracket);
                try {
                    timestamp = Long.parseLong(timestampStr);
                } catch (Exception e) {
                    // Keep default
                }
                displayText = message.substring(closingBracket + 1).trim();
            }
        }

        holder.messageBody.setText(displayText);
        holder.messageTime.setText(DateUtils.formatMessageTime(timestamp));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        String message = messages.get(position);
        if (message.contains("أنا:")) {
            return TYPE_SENT;
        }
        return TYPE_RECEIVED;
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageBody;
        TextView messageTime;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.message_body);
            messageTime = itemView.findViewById(R.id.message_time);
        }
    }
}
