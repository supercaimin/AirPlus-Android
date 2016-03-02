package cn.homecaught.airplus.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.content.Context;
import android.view.LayoutInflater;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

import cn.homecaught.airplus.R;

/**
 * Created by a1 on 16/2/27.
 */
public class MySelectAdapter extends BaseAdapter {

    private List<String> mItems = null;
    private List<String> mlogoItems = null;

    private LayoutInflater mInflater;
    private Context mContext;

    public MySelectAdapter(Context context, List<String> items, List<String> logoItems) {

        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mItems = items;
        mlogoItems = logoItems;
    }

    public void reload(List<String> items, List<String> logoItems)
    {
        mItems = items;
        mlogoItems = logoItems;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.select_item, null);
            viewHolder.textView = (TextView) convertView
                    .findViewById(R.id.textView);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mlogoItems != null){
            DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
            ImageLoader.getInstance().displayImage(mlogoItems.get(position), viewHolder.imageView, imageOptions);
        }
        viewHolder.textView.setText(mItems.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
