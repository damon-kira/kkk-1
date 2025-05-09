package com.datepicker.lib;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.manu.mdatepicker.R;
import com.manu.mdatepicker.databinding.DialogDatePickerBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public class MDatePicker extends Dialog implements PickerView.OnSelectListener, View.OnClickListener {
    private static final String TAG = MDatePicker.class.getSimpleName();
    private static final int YEAR_SPACE = 5;
    private static final int MAX_YEAR = 9999;

    private Context mContext;
    private DialogDatePickerBinding binding;
    private Resources mResources;

    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;
//    private int mCurrentHour;
//    private int mCurrentMinute;

    private String mTitle;
    private String mFontType;
    private int mGravity;
    private int mYearValue;
    private int mMonthValue;
    private int mDayValue;
    private boolean isCanceledTouchOutside;
    //    private boolean isSupportTime;
    private boolean isOnlyYearMonth;
    private boolean isTwelveHour;
    @ColorInt
    private int mConfirmTextColor;
    @ColorInt
    private int mCancelTextColor;
    @ColorInt
    private int mTitleTextColor;
    @ColorInt
    private int mDateNormalTextColor;
    @ColorInt
    private int mDateSelectTextColor;

    private String mLeftText;
    private String mRightText;

    private OnDateResultListener mOnDateResultListener;

    private MDatePicker(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogDatePickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        onView();
        onData();
    }

    public static Builder create(Context context) {
        return new Builder(context);
    }

    private void onView() {
        binding.tvDialogTopCancel.setText(mLeftText);
        binding.tvDialogTopConfirm.setText(mRightText);
        binding.mpvYear.setOnSelectListener(this);
        binding.mpvMonth.setOnSelectListener(this);
        binding.mpvDay.setOnSelectListener(this);

        binding.tvDialogTopCancel.setOnClickListener(this);
        binding.tvDialogTopConfirm.setOnClickListener(this);
    }

    private void onData() {
        Calendar mCalendar = Calendar.getInstance();
        mResources = mContext.getResources();
        List<String> mDataYear = new ArrayList<>();
        List<String> mDataMonth = new ArrayList<>();
//        List<String> mDataHour = new ArrayList<>();
//        List<String> mDataMinute = new ArrayList<>();

//        binding.mpvDialogYear.setText(mContext.getString(R.string.strDateYear));
//        binding.mpvDialogMonth.setText(mContext.getString(R.string.strDateMonth));
//        binding.mpvDialogDay.setText(mContext.getString(R.string.strDateDay));

        // Year
        int currentYear = mCalendar.get(Calendar.YEAR);
        int offset = 18;
        int maxYear = currentYear - (offset + 47);
        int minYear = currentYear - offset;
        for (int i = maxYear; i <= minYear; i++) {
            mDataYear.add(String.valueOf(i));
        }
        binding.mpvYear.setData(mDataYear);
        if (mCurrentYear > minYear || mCurrentYear < maxYear) {
            mCurrentYear = minYear;
        }
        binding.mpvYear.setDefaultValue(String.valueOf(mCurrentYear), DateType.YEAR, "-1");
        // Month
        DecimalFormat mDecimalFormat = new DecimalFormat("#00");
        for (int i = 1; i < 13; i++) {
            mDataMonth.add(mDecimalFormat.format(i));
        }
        binding.mpvMonth.setData(mDataMonth);

        if (mMonthValue > 12 || mMonthValue < 1) {
            mCurrentMonth = mCalendar.get(Calendar.MONTH) + 1;
            if (mYearValue != -1) {
                Log.w(TAG, "month init value is illegal, so select current month.");
            }
        } else {
            mCurrentMonth = mMonthValue;
        }
        binding.mpvMonth.setDefaultValue(String.valueOf(mCurrentMonth), DateType.MONTH, "-1");

        // Day
        int daySize = getDayByYearMonth(mCurrentYear, mCurrentMonth);
        if (mDayValue > daySize || mDayValue < 1) {
            mCurrentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
            if (mYearValue != -1) {
                Log.w(TAG, "day init value is illegal, so select current day.");
            }
        } else {
            mCurrentDay = mDayValue;
        }
        updateDay(mCurrentYear, mCurrentMonth);

//        // Hour
//        if (isTwelveHour) {
//            mCurrentHour = mCalendar.get(Calendar.HOUR);
//            addTimeData(mDataHour, 13, 12);
//            binding.mpvDialogHour.setData(mDataHour);
//            binding.mpvDialogHour.setDefaultValue(String.valueOf(mCurrentHour), DateType.HOUR_12, "12");
//        } else {
//            mCurrentHour = mCalendar.get(Calendar.HOUR_OF_DAY);
//            addTimeData(mDataHour, 25, 24);
//            binding.mpvDialogHour.setData(mDataHour);
//            binding.mpvDialogHour.setDefaultValue(String.valueOf(mCurrentHour), DateType.HOUR_12, "24");
//        }

        // Minute
//        addTimeData(mDataMinute, 61, 60);
//        binding.mpvDialogMinute.setData(mDataMinute);
//        mCurrentMinute = mCalendar.get(Calendar.MINUTE);
//        binding.mpvDialogMinute.setDefaultValue(String.valueOf(mCurrentMinute), DateType.MINUTE, "60");

        // Setting
        if (!TextUtils.isEmpty(mTitle)) binding.tvDialogTitle.setText(mTitle);

        Window window = getWindow();
        WindowManager.LayoutParams params = Objects.requireNonNull(window).getAttributes();
        params.gravity = mGravity;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // cancel dialog default padding.
        window.getDecorView().setPadding(0, 0, 0, 0);
        setCanceledOnTouchOutside(isCanceledTouchOutside);

        // gravity
        if (mGravity == Gravity.BOTTOM) {
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
//            binding.llDialogBottom.setVisibility(View.GONE);
            binding.llDialog.setBackgroundResource(R.drawable.dialog_date_picker_bottom_bg);
        } else if (mGravity == Gravity.CENTER) {
            params.width = (int) (Util.getScreenWidth((Activity) mContext) * 8 / 9);
            binding.tvDialogTopCancel.setVisibility(View.GONE);
            binding.tvDialogTopConfirm.setVisibility(View.GONE);
            binding.llDialog.setBackgroundResource(R.drawable.dialog_date_picker_center_bg);
        } else {
            params.width = (int) (Util.getScreenWidth((Activity) mContext) * 8 / 9);
            binding.tvDialogTopCancel.setVisibility(View.GONE);
            binding.tvDialogTopConfirm.setVisibility(View.GONE);
            binding.llDialog.setBackgroundResource(R.drawable.dialog_date_picker_center_bg);
        }
//        window.setBackgroundDrawableResource(android.R.color.white);

        // support day
//        if (isOnlyYearMonth) {
//            isSupportTime = false;
//        }

        // support time
//        if (isSupportTime) {
//            binding.mpvDialogHour.setVisibility(View.VISIBLE);
//            binding.mpvDialogMinute.setVisibility(View.VISIBLE);
//            float weight = -0.4f * mResources.getDisplayMetrics().density + 2.6f;
//            Log.i(TAG, "weight:" + weight);
//            binding.mpvDialogYear.setLayoutParams(
//                    new LinearLayout.LayoutParams(0, Util.dpToPx(mContext, 160), weight));
//        } else {
//            binding.mpvDialogHour.setVisibility(View.GONE);
//            binding.mpvDialogMinute.setVisibility(View.GONE);

//            if (isOnlyYearMonth) {
//                binding.mpvDialogDay.setVisibility(View.GONE);
//            }
//        }

        // set font size
//        float fontSize = getTextSize(mFontType);
//        binding.tvDialogTopConfirm.setTextSize(fontSize);
//        binding.tvDialogTopCancel.setTextSize(fontSize);
//        binding.tvDialogBottomConfirm.setTextSize(fontSize);
//        binding.tvDialogBottomCancel.setTextSize(fontSize);
//        binding.tvDialogTitle.setTextSize(fontSize + 1);

        // set text color
//        if (mConfirmTextColor != 0) {
//            binding.tvDialogBottomConfirm.setTextColor(mConfirmTextColor);
//            binding.tvDialogTopConfirm.setTextColor(mConfirmTextColor);
//        }

//        if (mCancelTextColor != 0) {
//            binding.tvDialogTopCancel.setTextColor(mCancelTextColor);
//            binding.tvDialogBottomCancel.setTextColor(mCancelTextColor);
//        }

//        if (mTitleTextColor != 0) {
//            binding.tvDialogTitle.setTextColor(mTitleTextColor);
//        }

        if (mDateNormalTextColor != 0) {
            binding.mpvYear.setNormalColor(mDateNormalTextColor);
            binding.mpvMonth.setNormalColor(mDateNormalTextColor);
            binding.mpvDay.setNormalColor(mDateNormalTextColor);
//            binding.mpvDialogHour.setNormalColor(mDateNormalTextColor);
//            binding.mpvDialogMinute.setNormalColor(mDateNormalTextColor);
        }

        if (mDateSelectTextColor != 0) {
            binding.mpvYear.setSelectColor(mDateSelectTextColor);
            binding.mpvMonth.setSelectColor(mDateSelectTextColor);
            binding.mpvDay.setSelectColor(mDateSelectTextColor);
//            binding.mpvDialogHour.setSelectColor(mDateSelectTextColor);
//            binding.mpvDialogMinute.setSelectColor(mDateSelectTextColor);
        }
        window.setAttributes(params);
    }


    @Override
    public void onSelect(View v, String data) {
        int i = v.getId();
        if (i == R.id.mpv_year) {
            mCurrentYear = Integer.parseInt(data);
            updateDay(mCurrentYear, mCurrentMonth);
        } else if (i == R.id.mpv_month) {
            mCurrentMonth = Integer.parseInt(data);
            updateDay(mCurrentYear, mCurrentMonth);
        } else if (i == R.id.mpv_day) {
            mCurrentDay = Integer.parseInt(data);
        }/* else if (i == R.id.mpvDialogHour) {
            mCurrentHour = Integer.parseInt(data);
        } else if (i == R.id.mpvDialogMinute) {
            mCurrentMinute = Integer.parseInt(data);
        }*/
        Log.i(TAG, mCurrentYear + "-" + mCurrentMonth + "-" + mCurrentDay);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvDialogTopCancel) {
            dismiss();
        } else if (i == R.id.tvDialogTopConfirm) {
            Log.i(TAG, mCurrentYear + "-" + mCurrentMonth + "-" + mCurrentDay);
            if (mOnDateResultListener != null) {
//                if (isSupportTime) {
//                    mOnDateResultListener.onDateResult(getDateMills(mCurrentYear, mCurrentMonth, mCurrentDay, mCurrentHour, mCurrentMinute));
//                } else {
                if (isOnlyYearMonth) {
                    mOnDateResultListener.onDateResult(getDateMills(mCurrentYear, mCurrentMonth, 1, 0, 0));
                } else {
                    mOnDateResultListener.onDateResult(getDateMills(mCurrentYear, mCurrentMonth, mCurrentDay, 0, 0));
                }
//                }
                dismiss();
            }
        }
    }

    public void setOnDateResultListener(OnDateResultListener onDateResultListener) {
        this.mOnDateResultListener = onDateResultListener;
    }

    private int getDayByYearMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void updateDay(int year, int month) {
        List<String> mDataDay = new ArrayList<>();
        int daySize = getDayByYearMonth(year, month);
        Log.i(TAG, "updateDay > year:" + year + ",month:" + month + ",daySize:" + daySize + ",mCurrentDay:" + mCurrentDay);
        addTimeData(mDataDay, daySize + 1, 32);
        binding.mpvDay.setData(mDataDay);
        if (mCurrentDay > mDataDay.size()) {
            mCurrentDay = mDataDay.size();
        }
        binding.mpvDay.setDefaultValue(String.valueOf(mCurrentDay), DateType.DAY, "-1");
    }

    private void addTimeData(List<String> list, int size, int equal) {
        for (int i = 1; i < size; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else if (i == equal) {
                list.add("00");
            } else {
                list.add(String.valueOf(i));
            }
        }
    }

    private long getDateMills(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, 0);
        return calendar.getTimeInMillis();
    }

    private float getTextSize(@FontType.Type String type) {
        float fontSize;
        if (FontType.LARGE.equals(type)) {
            fontSize = mResources.getDimension(R.dimen.date_picker_setting_large);
        } else if (FontType.MEDIUM.equals(type)) {
            fontSize = mResources.getDimension(R.dimen.date_picker_setting_medium);
        } else if (FontType.NORMAL.equals(type)) {
            fontSize = mResources.getDimension(R.dimen.date_picker_setting_normal);
        } else if (FontType.SMALL.equals(type)) {
            fontSize = mResources.getDimension(R.dimen.date_picker_setting_small);
        } else {
            fontSize = mResources.getDimension(R.dimen.date_picker_setting_normal);
        }
        return Util.pxToSp(mContext, fontSize);
    }

    public static class Builder {
        private final Context mContext;
        private String mTitle;
        private int mGravity;
        private int mYearValue = -1;
        private int mMonthValue = -1;
        private int mDayValue = -1;
        private boolean isCanceledTouchOutside;
        private boolean isSupportTime;
        private boolean isOnlyYearMonth;
        private boolean isTwelveHour;
        private @ColorInt
        int mConfirmTextColor;
        private @ColorInt
        int mCancelTextColor;
        private @ColorInt
        int mTitleTextColor;
        private @ColorInt
        int mDateNormalTextColor;
        private @ColorInt
        int mDateSelectTextColor;
        private String mFontType = FontType.MEDIUM;
        private OnDateResultListener mOnDateResultListener;
        private String mLeftText;
        private String mRightText;
        private int mCurrYear = 0;
        private int mCurrMonth = 0;
        private int mCurrDay = 0;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setTitle(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }

        public Builder setGravity(int mGravity) {
            this.mGravity = mGravity;
            return this;
        }

        public Builder setYearValue(@IntRange(from = 5, to = 9999) int yearValue) {
            this.mYearValue = yearValue;
            return this;
        }

        public Builder setMonthValue(@IntRange(from = 1, to = 12) int monthValue) {
            this.mMonthValue = monthValue;
            return this;
        }

        public Builder setDayValue(int dayValue) {
            this.mDayValue = dayValue;
            return this;
        }

        public Builder setCanceledTouchOutside(boolean canceledTouchOutside) {
            isCanceledTouchOutside = canceledTouchOutside;
            return this;
        }

        public Builder setSupportTime(boolean supportTime) {
            isSupportTime = supportTime;
            return this;
        }

        public Builder setOnlyYearMonth(boolean onlyYearMonth) {
            isOnlyYearMonth = onlyYearMonth;
            return this;
        }

        public Builder setTwelveHour(boolean twelveHour) {
            isTwelveHour = twelveHour;
            return this;
        }

        public Builder setFontType(@FontType.Type String type) {
            this.mFontType = type;
            return this;
        }

        public Builder setConfirmTextColor(@ColorInt int confirmTextColor) {
            this.mConfirmTextColor = confirmTextColor;
            return this;
        }

        public Builder setCancelTextColor(@ColorInt int cancelTextColor) {
            this.mCancelTextColor = cancelTextColor;
            return this;
        }

        public Builder setTitleTextColor(@ColorInt int titleTextColor) {
            this.mTitleTextColor = titleTextColor;
            return this;
        }

        public Builder setDateNormalTextColor(@ColorInt int normalTextColor) {
            this.mDateNormalTextColor = normalTextColor;
            return this;
        }

        public Builder setDateSelectTextColor(@ColorInt int selectTextColor) {
            this.mDateSelectTextColor = selectTextColor;
            return this;
        }

        public Builder setOnDateResultListener(OnDateResultListener onDateResultListener) {
            this.mOnDateResultListener = onDateResultListener;
            return this;
        }

        public Builder setLeftText(String text) {
            this.mLeftText = text;
            return this;
        }

        public Builder setRightText(String text) {
            this.mRightText = text;
            return this;
        }

        public Builder setLeftText(@StringRes int resId) {
            this.mLeftText = mContext.getString(resId);
            return this;
        }

        public Builder setRightText(@StringRes int resId) {
            this.mRightText = mContext.getString(resId);
            return this;
        }

        public Builder setCurrYear(int currYear) {
            this.mCurrYear = currYear;
            return this;
        }

        public Builder setCurrMonth(int currMonth) {
            this.mCurrMonth = currMonth;
            return this;
        }

        public Builder setCurrDay(int currDay) {
            this.mCurrDay = currDay;
            return this;
        }

        private void applyConfig(MDatePicker dialog) {
            if (this.mGravity == 0) this.mGravity = Gravity.CENTER;
            dialog.mContext = this.mContext;
            dialog.mTitle = this.mTitle;
            dialog.mFontType = this.mFontType;
            dialog.mGravity = this.mGravity;
            dialog.mYearValue = this.mYearValue;
            dialog.mMonthValue = this.mMonthValue;
            dialog.mDayValue = this.mDayValue;
//            dialog.isSupportTime = this.isSupportTime;
            dialog.isOnlyYearMonth = this.isOnlyYearMonth;
            dialog.isTwelveHour = this.isTwelveHour;
            dialog.mConfirmTextColor = this.mConfirmTextColor;
            dialog.mCancelTextColor = this.mCancelTextColor;
            dialog.mTitleTextColor = this.mTitleTextColor;
            dialog.mDateNormalTextColor = this.mDateNormalTextColor;
            dialog.mDateSelectTextColor = this.mDateSelectTextColor;
            dialog.isCanceledTouchOutside = this.isCanceledTouchOutside;
            dialog.mOnDateResultListener = this.mOnDateResultListener;
            dialog.mLeftText = this.mLeftText;
            dialog.mRightText = this.mRightText;
            dialog.mCurrentYear = this.mCurrYear;
            dialog.mCurrentMonth = this.mCurrMonth;
            dialog.mCurrentDay = this.mCurrDay;
        }

        public MDatePicker build() {
            MDatePicker dialog = new MDatePicker(mContext);
            applyConfig(dialog);
            return dialog;
        }
    }

    public interface OnDateResultListener {
        void onDateResult(long date);
    }
}
