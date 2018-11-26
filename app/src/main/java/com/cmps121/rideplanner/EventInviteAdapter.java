package com.cmps121.rideplanner;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EventInviteAdapter extends ArrayAdapter<EventInviteListItem> {

    Context context;
    public EventInviteAdapter(Context context, ArrayList<EventInviteListItem> eventInvites) {
        super(context, 0, eventInvites);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventInviteListItem eventInviteListItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_eventinvite, parent, false);
        }

        TextView eventName = convertView.findViewById(R.id.eventName);
        TextView groupName = convertView.findViewById(R.id.groupName);

        ImageButton noButton = convertView.findViewById(R.id.noBtn);
        noButton.setTag(position);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                EventInviteListItem eventInviteListItem = getItem(position);
                String groupCodeStr = eventInviteListItem.getGroupCode();
                String eventNameStr = eventInviteListItem.getEventName();
                remove(getItem(position));
                notifyDataSetChanged();
                FirebaseDatabase.getInstance().getReference("users").child(userID).child("eventInvites").
                        child(groupCodeStr).child("events").child(eventNameStr).removeValue();
            }
        });

        eventName.setText(eventInviteListItem.getEventName());
        groupName.setText(eventInviteListItem.getGroupName());

        eventName.setTag(position);
        eventName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                EventInviteListItem eventInviteListItem = getItem(position);
                String eventName = eventInviteListItem.getEventName();
                String groupName = eventInviteListItem.getGroupName();
                String groupCode = eventInviteListItem.getGroupCode();
                Intent intent = new Intent(context, EventPage.class);
                intent.putExtra("groupName", groupName);
                intent.putExtra("groupCode", groupCode);
                intent.putExtra("eventName", eventName);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
