package com.greenapex.callhelper.Adpter;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.greenapex.callhelper.Interface.IVibrate;
import com.greenapex.callhelper.R;
import com.greenapex.callhelper.Util.OnOneOffClickListener;
import com.greenapex.callhelper.Util.Pref;

/**
 * Created by GreenApex on 15/3/18.
 */

public class AdapterVibrate extends RecyclerView.Adapter<AdapterVibrate.RecyclerViewHolder> {

    LayoutInflater inflater;
    Context context;
    String[] SubjectValues;
    IVibrate iVibrate;
    private int lastSelectedPosition = 1;


    public AdapterVibrate(IVibrate iVibrate, Context context, String[] SubjectValues1) {
        this.iVibrate = iVibrate;
        SubjectValues = SubjectValues1;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_text_ringtone, null, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {

        holder.radioButton.setText(SubjectValues[position]);

        if (!TextUtils.isEmpty(Pref.getValue(context, "vibrator", "0"))) {
            int selectedBtn = Integer.parseInt(Pref.getValue(context, "vibrator", "0"));
            if (position == selectedBtn) {
                holder.radioButton.setChecked(true);
            } else {
                holder.radioButton.setChecked(false);
            }
        }

        holder.radioButton.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                lastSelectedPosition = holder.getAdapterPosition();

                iVibrate.yourVibrateMethod(position, SubjectValues[position]);

                Pref.setValue(context, "vibrator", ""+position);

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return SubjectValues.length;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        RadioButton radioButton;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView.findViewById(R.id.rbRingTone);
        }
    }

}
