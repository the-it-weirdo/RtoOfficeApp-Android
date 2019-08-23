package com.example.debaleen.project2;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class Typewriter extends AppCompatTextView {

    CharSequence mText;
    int mIndex;
    long mDelay = 150; //in ms

    public Typewriter(Context context){
        super(context);
    }

    public Typewriter(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    Handler mHandler = new Handler();

    Runnable charecterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));

            if(mIndex <= mText.length()){
                mHandler.postDelayed(charecterAdder, mDelay);
            }
        }
    };

    public void animateText(CharSequence txt){
        mText = txt;
        mIndex = 0;

        setText("");
        mHandler.removeCallbacks(charecterAdder);
        mHandler.postDelayed(charecterAdder, mDelay);
    }

    public void setCharecterDelay(long s){
        mDelay = s;
    }
}
