package com.colombia.credit.view.email;

import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by weisl on 2019/6/6.
 */
public class EmailFilter extends Filter {

    private ArrayList<String> mEmailList;
    private IFilterResultListener mListener;

    public EmailFilter(ArrayList<String> emailList) {
        this.mEmailList = emailList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint == null || constraint.length() == 0) {
            results.values = new ArrayList<EmailInputBean>();
            results.count = 0;
        } else {
            String inputText = constraint.toString();
            ArrayList<EmailInputBean> newList = new ArrayList<>();
            int lastIndex = inputText.lastIndexOf("@");
            EmailInputBean emailInputBean;
            StringBuilder sb = new StringBuilder();
            if (lastIndex == -1) {
                for (String s : mEmailList) {
                    emailInputBean = new EmailInputBean(inputText,
                            sb.append(getSubText(inputText)).append(s).toString());
                    newList.add(emailInputBean);
                    sb.delete(0, sb.length());
                }
            } else {
                String text = inputText.substring(lastIndex);
                for (String s : mEmailList) {
                    if (s.startsWith(text)) {
                        emailInputBean = new EmailInputBean(inputText,
                                sb.append(getSubText(inputText)).append(s).toString());
                        newList.add(emailInputBean);
                        sb.delete(0, sb.length());
                    }
                }
            }
            results.values = newList;
            results.count = newList.size();
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (mListener != null) {
            ArrayList<EmailInputBean> list = new ArrayList<>();
            try {
                if (results != null && results.count > 0) {//有符合过滤规则的数据
                    ArrayList<EmailInputBean> tempList = (ArrayList<EmailInputBean>) results.values;
                    list.addAll(tempList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mListener.filterResult(constraint, list);
        }
    }


    private String getSubText(String text) {
        int indexOf = text.indexOf("@");
        if (indexOf == -1) {
            return text;
        }
        return text.substring(0, indexOf);
    }

    public interface IFilterResultListener {
        void filterResult(CharSequence constraint, ArrayList<EmailInputBean> results);
    }

    public void setIFilterResultListener(IFilterResultListener listener) {
        this.mListener = listener;
    }

}
