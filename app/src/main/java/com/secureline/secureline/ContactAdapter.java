package com.secureline.secureline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private final List<String> contacts;
    private OnItemClickListener clickListener;

    public ContactAdapter(List<String> contacts) {
        this.contacts = contacts;
    }

    public void setClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        String contact = contacts.get(position);
        holder.contactName.setText(contact);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClick(position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;
        TextView contactFingerprint;
        ImageView contactAvatar;
        ImageView contactVerifiedIcon;

        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            contactFingerprint = itemView.findViewById(R.id.contact_fingerprint);
            contactAvatar = itemView.findViewById(R.id.contact_avatar);
            contactVerifiedIcon = itemView.findViewById(R.id.contact_verified_icon);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position, View view);
    }
}
