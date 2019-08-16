package cn.com.gxrb.lib.directory.business.view.widget;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RbAdapterViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    private RbAdapterViewHolder(Context context, ViewGroup parent, int layoutId,
                                int position) {
        this.mViews = new SparseArray<View>();
        this.mPosition = position;
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId,
                parent, false);
        this.mConvertView.setTag(this);
    }

    public static RbAdapterViewHolder get(Context context, View convertView,
                                          ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new RbAdapterViewHolder(context, parent, layoutId, position);
        } else {
            RbAdapterViewHolder holder = (RbAdapterViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V getView(int resId) {
        View v = mViews.get(resId);
        if (null == v) {
            v = mConvertView.findViewById(resId);
            mViews.put(resId, v);
        }
        return (V) v;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public int getPosition() {
        return mPosition;
    }

    public RbAdapterViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }
}
