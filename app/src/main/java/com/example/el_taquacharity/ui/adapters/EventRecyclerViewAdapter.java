package com.example.el_taquacharity.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.el_taquacharity.R;
import com.example.el_taquacharity.pojo.Event;
import com.example.el_taquacharity.ui.Activities.EventDataActivity;

import java.util.ArrayList;
import java.util.List;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.EventViewHolder> {
    public List<Event> events = new ArrayList<>();
    Context context;

    public EventRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false)); //Here write the family_item layout
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, final int position) {
        holder.eventNameTextView.setText(context.getResources().getString(R.string.event_name)+" : " +events.get(position).getEventName());
        holder.eventDescriptionTextView.setText(context.getResources().getString(R.string.event_description)+" : " +events.get(position).getEventDescription());
        holder.familiesNumberTextView.setText(context.getResources().getString(R.string.families_number)+" : " +String.valueOf(events.get(position).getNumberOfFamilies()));
        holder.remainingFamiliesTextView.setText(context.getResources().getString(R.string.remaining_families)+" : " +String.valueOf(events.get(position).getRemainingFamilies()));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setList(List<Event> EventList) {
        this.events = EventList;
        notifyDataSetChanged();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView , eventDescriptionTextView , familiesNumberTextView , remainingFamiliesTextView;
        LinearLayout wholeItem;
        //initialize the view of the family_item
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.event_name);
            eventDescriptionTextView = itemView.findViewById(R.id.discreption);
            familiesNumberTextView = itemView.findViewById(R.id.number_of_families);
            remainingFamiliesTextView = itemView.findViewById(R.id.remaining_families);
            wholeItem = itemView.findViewById(R.id.wholeItem);
        }
    }
}

