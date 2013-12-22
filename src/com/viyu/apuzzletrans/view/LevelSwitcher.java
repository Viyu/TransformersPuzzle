package com.viyu.apuzzletrans.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.viyu.apuzzletrans.R;


public class LevelSwitcher extends ViewSwitcher
{
    public LevelSwitcher(Context context)
    {
        super(context);
    }
    
    public LevelSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLevelInfo(int imageResid, String text, String num)
    {
        View root = this.getNextView();
        TextView textView = (TextView)root.findViewById(R.id.levelswitcher_text);
        TextView numView = (TextView)root.findViewById(R.id.levelswitcher_levelnum);
        ImageView imageView = (ImageView)root.findViewById(R.id.levelswitcher_image);
        textView.setText(text);
        numView.setText(num);
        imageView.setImageResource(imageResid);
        //
        showNext();
    }
}
