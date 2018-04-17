package com.greenapex.callhelper.Adpter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.greenapex.callhelper.Model.conctactPojo
import com.greenapex.callhelper.R
import com.greenapex.callhelper.Util.CommonUtils
import kotlinx.android.synthetic.main.activity_main.view.*

/**
 * Created by GreenApex on 2/1/18.
 */

class adpterInfo(internal var context: Context, internal var arraySchedule: List<conctactPojo>) : RecyclerView.Adapter<adpterInfo.RecyclerViewHolder>() {
  //  internal var inflater: LayoutInflater


    init {
       // inflater = context.getSystemService(context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.app_inforow, null, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val second = Integer.parseInt(arraySchedule[position].callDuration)

        holder.txtCallDuration.text = CommonUtils.timeConversion(second)
        holder.txtlogType.text = arraySchedule[position].callType
        holder.txtlogDate.text = arraySchedule[position].callDate
    }

    override fun getItemCount(): Int {
        return arraySchedule.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var txtlogType: TextView
        internal var txtlogDate: TextView
        internal var txtCallDuration: TextView

        init {

            txtlogDate = itemView.findViewById<View>(R.id.txt_logInfoDateTime) as TextView
            txtlogType = itemView.findViewById<View>(R.id.txt_logInfoType) as TextView
            txtCallDuration = itemView.findViewById<View>(R.id.txt_logInfoDuration) as TextView
        }
    }
}
