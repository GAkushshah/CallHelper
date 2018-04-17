package com.greenapex.callhelper.Model

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Comparator
import java.util.Date

/**
 * Created by GreenApex on 24/11/17.
 */

class conctactPojo {

    var contactName: String? = null
    var contactNumber: String? = null
    var callType: String? = null
    var callDate: String? = null
    var callDuration: String? = null
    var callPhotoUri: String? = null
    var isSelected: Boolean = false

    //    public static class RatingComparator implements Comparator<conctactPojo>{
    //        @Override
    //        public int compare(conctactPojo obj1, conctactPojo obj2) {
    //            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
    //            Date date1 =  null;
    //            Date date2 = null;
    //            try {
    //                date1 = formatter.parse(obj1.getCallDate());
    //                date2 = formatter.parse(obj2.getCallDate());
    //            } catch (ParseException e) {
    //                e.printStackTrace();
    //            }
    //
    //            return (date1.getTime() < date2.getTime()) ? -1 : (date1.getTime() > date2.getTime()) ? 1 : 0;
    //        }
    //    }
}
