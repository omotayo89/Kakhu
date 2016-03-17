package com.blaizedtrail.kakhu.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.blaizedtrail.kakhu.application.App;

/**
 * Created by McLeroy on 2/11/2016.
 */
public class BariolTextView extends AppCompatTextView{
    public BariolTextView(Context context) {
        super(context);
    }

    public BariolTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BariolTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        setTypeface(Typeface.createFromAsset(App.getContext().getAssets(),"bariol.otf"));
    }
}
