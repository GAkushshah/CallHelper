package com.greenapex.callhelper.Util;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by GreenApex on 26/12/17.
 */

public class MySpannable extends ClickableSpan {

    private boolean isUnderline = true;

    /**
     * Constructor
     */
    public MySpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isUnderline);
        ds.setColor(Color.parseColor("#e0c907"));
    }

    @Override
    public void onClick(View widget) {

    }
}
