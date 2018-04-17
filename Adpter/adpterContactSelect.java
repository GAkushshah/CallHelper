package com.greenapex.callhelper.Adpter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.greenapex.callhelper.Model.conctactPojo;
import com.greenapex.callhelper.R;
import com.greenapex.callhelper.Util.OnOneOffClickListener;
import com.greenapex.callhelper.Util.Pref;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GreenApex on 24/11/17.
 */

public class adpterContactSelect extends RecyclerView.Adapter<adpterContactSelect.RecyclerViewHolder> implements Filterable {

    Context context;
    ArrayList<conctactPojo> arrayContact;
    ArrayList<conctactPojo> mFilteredList;
    LayoutInflater inflater;
    ValueFilter valueFilter;

    public adpterContactSelect(Context context, ArrayList<conctactPojo> arrayContact) {
        this.arrayContact = arrayContact;
        mFilteredList = arrayContact;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_contactrow, null, false);
        RecyclerViewHolder holder = new adpterContactSelect.RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder viewHolder, final int position) {
        final String image;
        final conctactPojo conctactPojo = arrayContact.get(position);
        viewHolder.txtContactName.setText(arrayContact.get(position).getContactName());
        viewHolder.txtContactNumber.setText(arrayContact.get(position).getContactNumber());
        image = arrayContact.get(position).getCallPhotoUri();
        if (image != null) {
            Uri uri = Uri.parse(image);
            Picasso.with(context).load(uri).into(viewHolder.imgContact);
        } else {
            viewHolder.imgContact.setImageResource(R.drawable.user);
        }
        if (conctactPojo.isSelected()) {
            //For Keyboard Hide

            viewHolder.itemView.setBackgroundColor(context.getResources()
                    .getColor(R.color.white));
            String number = arrayContact.get(position).getContactNumber();
            String name = arrayContact.get(position).getContactName();
            String imagePath = arrayContact.get(position).getCallPhotoUri();

            Pref.setValue(context, "contactNumber", number);
            Pref.setValue(context, "contactName", name);
            Pref.setValue(context, "contactImage", imagePath);

        } else {
            viewHolder.itemView.setBackgroundColor(context.getResources()
                    .getColor(R.color.background));
        }

        viewHolder.itemView.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(viewHolder.itemView.getWindowToken(), 0);

                for (conctactPojo conctactPojo : arrayContact) {
                    conctactPojo.setSelected(false);
                }
                arrayContact.get(position).setSelected(true);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayContact.size();
    }


    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList filterList = new ArrayList();
                for (int i = 0; i < mFilteredList.size(); i++) {

                    if ((mFilteredList.get(i).getContactName().toUpperCase()).startsWith(constraint.toString().toUpperCase())) {
                        filterList.add(mFilteredList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mFilteredList.size();
                results.values = mFilteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            arrayContact = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtContactName, txtContactNumber;
        ImageView imgContact;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtContactName = (TextView) itemView.findViewById(R.id.txt_contactName);
            txtContactNumber = (TextView) itemView.findViewById(R.id.txt_contactNumber);
            imgContact = (ImageView) itemView.findViewById(R.id.img_contact);
        }
    }
}
