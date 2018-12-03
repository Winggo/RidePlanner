package com.cmps121.rideplanner;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DriverListAdapter extends ArrayAdapter<CarItems> {

    Context context;
    ArrayList<User> attendees = new ArrayList<>();
    ArrayList<String> attendeeNames = new ArrayList<>();
    DatabaseReference dbEvent;
    String groupName;
    String groupCode;
    String eventName;
    CarItems car;
    ArrayList<Integer> selectedMembersIndexList = new ArrayList<>();
    HashMap<String, Object> carUsers;
    ArrayList<User> driverUsers;
    ArrayList<String> passengers;
    ArrayList<CarItems> cars;



    public DriverListAdapter(Context context, ArrayList<CarItems> cars, String groupCode, String groupName, String eventName, ArrayList<User> driverUsers, DatabaseReference dbEvent) {
        super(context, 0, cars);
        this.context = context;
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.eventName = eventName;
        this.dbEvent = dbEvent;
        this.driverUsers = driverUsers;
        this.cars = cars;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_driver, parent, false);
        }

        car = getItem(position);

        final int driverPosition = position;
        TextView driverName = convertView.findViewById(R.id.driverName);
        TextView passengerOne = convertView.findViewById(R.id.passenger1);
        TextView passengerTwo = convertView.findViewById(R.id.passenger2);
        TextView passengerThree = convertView.findViewById(R.id.passenger3);
        TextView passengerFour = convertView.findViewById(R.id.passenger4);
        Button addBtn = convertView.findViewById(R.id.addBtn);
        addBtn.setTag(new Integer(position));

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendees.clear();
                attendeeNames.clear();
                car = getItem((Integer)view.getTag());
             //   final int driverPosition = position;
                //final CarItems car = (CarItems) adapterView.getAdapter().getItem(position);
                Query query = dbEvent.child("attendees").orderByChild("inCar").equalTo("false");

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            attendees.add(ds.getValue(User.class));
                        }

                        for (int i = 0; i < attendees.size(); i++) {
                            attendeeNames.add(attendees.get(i).getUserName());
                        }

                        final boolean[] isSelectedArray = new boolean[attendeeNames.size()];

                        CharSequence[] cs = attendeeNames.toArray(new CharSequence[attendeeNames.size()]);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setTitle("Add passengers to " + car.getDriverName() + "'s car.")
                                .setMultiChoiceItems(cs, isSelectedArray, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if (isChecked) {
                                            if (!selectedMembersIndexList.contains(which)) {
                                                selectedMembersIndexList.add(which);
                                            }
                                        } else if (selectedMembersIndexList.contains(which)) {
                                            selectedMembersIndexList.remove(which);
                                        }
                                    }
                                })
                                .setCancelable(false)
                                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                     /*   carUsers = new HashMap<>();
                                        for (int i = 0; i < selectedMembersIndexList.size(); i++) {
                                            User tempUser = attendees.get(selectedMembersIndexList.get(i));
                                            dbEvent.child("attendees").child(tempUser.getUserID()).child("inCar").setValue(true);
                                            carUsers.put(tempUser.getUserID(), tempUser);
                                        }*/

                                        final String driverID = driverUsers.get(driverPosition).getUserID();

                                        Query query = dbEvent.child("cars")
                                                .orderByChild("driverID")
                                                .equalTo(driverID);

                                        query.addListenerForSingleValueEvent(new ValueEventListener()   {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                carUsers = new HashMap<>();
                                                for (int i = 0; i < selectedMembersIndexList.size(); i++) {
                                                    User tempUser = attendees.get(selectedMembersIndexList.get(i));
                                                    dbEvent.child("attendees").child(tempUser.getUserID()).child("inCar").setValue(driverID);
                                                    carUsers.put(tempUser.getUserID(), tempUser);
                                                }

                                                passengers = new ArrayList<>();
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    ds.getRef().updateChildren(carUsers);
                                                }

                                                Query query1 = dbEvent.child("cars")
                                                        .orderByChild("driverID")
                                                        .equalTo(driverID);
                                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                            for (DataSnapshot childSnapshot : ds.getChildren()) {
                                                                if (childSnapshot.hasChildren() && !childSnapshot.getValue(User.class).getUserID().equals(driverID)) {
                                                                    passengers.add(childSnapshot.getValue(User.class).getUserName());
                                                                }
                                                            }
                                                        }

                                                        while (passengers.size() < 4) {
                                                            passengers.add("Empty");
                                                        }
                                                        cars.set(driverPosition, new CarItems(driverUsers.get(driverPosition).getUserName(), passengers));
                                                        for (int i = 0; i < isSelectedArray.length; i++) {
                                                            isSelectedArray[i] = false;
                                                        }
                                                        selectedMembersIndexList.clear();
                                                        notifyDataSetChanged();

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        notifyDataSetChanged();
                                    }

                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        driverName.setText(car.driverName + " (Driver)");
        passengerOne.setText(car.passenger1);
        passengerTwo.setText(car.passenger2);
        passengerThree.setText(car.passenger3);
        passengerFour.setText(car.passenger4);

        return convertView;
    }
}
