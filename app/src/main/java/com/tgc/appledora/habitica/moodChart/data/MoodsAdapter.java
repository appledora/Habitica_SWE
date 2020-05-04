package com.tgc.appledora.habitica.moodChart.data;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;

import com.maseno.franklinesable.lifereminder.R;
import com.tgc.appledora.habitica.moodChart.utils.Preferences;
import com.tgc.appledora.habitica.moodChart.utils.SpecialUtils;
import com.tgc.appledora.habitica.moodChart.views.CellView;

import java.util.List;


public class MoodsAdapter extends BaseAdapter {
    private final Context mContext;
    private List<Mood> mMoods;
    private float mDensity;
    private boolean mIsPortraitMode;

    public MoodsAdapter(Context context, float density, boolean isPortraitMode) {
        mContext = context;
        mDensity = density;
        mIsPortraitMode = isPortraitMode;
    }

    @Override
    public int getCount() {
        return mMoods != null ? mMoods.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mMoods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Mood mood = mMoods.get(position);
        CellView moodCellView;

        if (convertView == null) {
            moodCellView = new CellView(mContext);
            if (mIsPortraitMode) {
                moodCellView.setHeight((int) (28 * mDensity));
            } else {
                moodCellView.setHeight((int) (50 * mDensity));
            }
            moodCellView.setGravity(Gravity.CENTER);
        } else {
            moodCellView = (CellView) convertView;
        }

        // Set text
        if (position == 0) {
            // If position is 0, let the cell empty
            moodCellView.setText("");
        } else if (position <= 12) {
            // Otherwise, if position is between 1 and 12, set in each cell the initial of
            // the corresponding month (i.e. 1 = J, 2 = F, 3 = M,...)
            moodCellView.setText(SpecialUtils.getMonthInitial(mContext, position));
        } else if (position % 13 == 0) {
            // Otherwise, each time the remainder of position divided into 13 is 0,
            // set in that cell the quotient of the division, which represents the day of the month
            // (i.e. 1, 2, 3,... 30, 31)
            moodCellView.setText(String.valueOf(position / 13));
        } else {
            // Otherwise, the cell represents a day of the year and has no text, only colors
            moodCellView.setText("");
        }

        // Set background color
        if (position <= 12 || position % 13 == 0) {
            moodCellView.setBackgroundColor(Color.WHITE);
            moodCellView.setTriangleColor(Color.TRANSPARENT);
        } else if (position == 392 || position == 405 || position == 407 ||
                position == 409 || position == 412 || position == 414) {
            moodCellView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGray));//Color.DKGRAY);
            moodCellView.setTriangleColor(Color.TRANSPARENT);
        } else if (position == 379) {
            if (SpecialUtils.isLeapYear(Preferences.getSelectedYear(mContext))) {
                //TODO: code is repeating here
                if (mood.getFirstColor() != 0) {
                    moodCellView.setBackgroundColor(SpecialUtils.getColor(mContext, mood.getFirstColor()));
                    if (mood.getSecondColor() != 0) {
                        moodCellView.setTriangleColor(SpecialUtils.getColor(mContext, mood.getSecondColor()));
                    } else {
                        moodCellView.setTriangleColor(Color.TRANSPARENT);
                    }
                } else {
                    moodCellView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.item_selector));
                    moodCellView.setTriangleColor(Color.TRANSPARENT);
                }
            } else {
                moodCellView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGray));//Color.DKGRAY);
                moodCellView.setTriangleColor(Color.TRANSPARENT);
            }
        } else {
            //TODO: code is repeating here
            if (mood.getFirstColor() != 0) {
                moodCellView.setBackgroundColor(SpecialUtils.getColor(mContext, mood.getFirstColor()));
                if (mood.getSecondColor() != 0) {
                    moodCellView.setTriangleColor(SpecialUtils.getColor(mContext, mood.getSecondColor()));
                } else {
                    moodCellView.setTriangleColor(Color.TRANSPARENT);
                }
            } else {
                moodCellView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.item_selector));
                moodCellView.setTriangleColor(Color.TRANSPARENT);
            }
        }

        return moodCellView;
    }

    public void setMoods(List<Mood> moods) {
        mMoods = moods;
    }
}
