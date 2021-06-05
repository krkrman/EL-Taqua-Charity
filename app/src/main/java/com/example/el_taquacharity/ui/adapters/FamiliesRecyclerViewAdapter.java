package com.example.el_taquacharity.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.el_taquacharity.R;
import com.example.el_taquacharity.pojo.Family;
import com.example.el_taquacharity.ui.Activities.AllFamiliesActivity;

import java.util.ArrayList;
import java.util.List;

public class FamiliesRecyclerViewAdapter extends RecyclerView.Adapter<FamiliesRecyclerViewAdapter.FamilyViewHolder> {
    public static final int ORDINARY_ITEM = 100;
    public static final int SELECTED_ITEM = 200;
    public List<Family> familyList = new ArrayList<>();
    Context context;
    public List<Family> selectedFamilyList = new ArrayList<>();

    public FamiliesRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FamilyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view = null;
        if (viewType == ORDINARY_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_item, parent, false);
        } else if (viewType == SELECTED_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_selected_item, parent, false);
        }
        final FamilyViewHolder viewHolder = new FamilyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FamilyViewHolder holder, final int position) {
        holder.id.setText(context.getResources().getString(R.string.id_number) + " : " + familyList.get(position).getID());
        holder.husbandName.setText(context.getResources().getString(R.string.husband_name) + " : " + familyList.get(position).getHusbandName());
        holder.wifeName.setText(context.getResources().getString(R.string.wife_name) + " : " + familyList.get(position).getWifeName());
        holder.phoneNumber.setText(context.getResources().getString(R.string.phone_number) + " : " + familyList.get(position).getPhoneNumber());
        holder.status.setText(context.getResources().getString(+R.string.status) + " : " + familyList.get(position).getStatus());
    }

    @Override
    public int getItemViewType(int position) {
        Family family = familyList.get(position);
        if (selectedFamilyList.contains(family))
            return SELECTED_ITEM;
        else
            return ORDINARY_ITEM;
    }

    @Override
    public int getItemCount() {
        return familyList.size();
    }

    public void setList(List<Family> FamilyList,List<Family> selectedFamilyList) {
        this.familyList = FamilyList;
        this.selectedFamilyList = selectedFamilyList;
        notifyDataSetChanged();
    }

    public static class FamilyViewHolder extends RecyclerView.ViewHolder {
        TextView id, husbandName, wifeName, status, phoneNumber;
        LinearLayout wholeItem;

        //initialize the view of the ite
        public FamilyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id_text_view);
            husbandName = itemView.findViewById(R.id.husband_name_text_view);
            wifeName = itemView.findViewById(R.id.wife_name_text_view);
            status = itemView.findViewById(R.id.status_text_view);
            phoneNumber = itemView.findViewById(R.id.phone_number_text_view);
            wholeItem = itemView.findViewById(R.id.whole_family_item);
        }
    }
}

