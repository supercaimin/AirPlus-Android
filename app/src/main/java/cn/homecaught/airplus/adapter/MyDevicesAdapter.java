package cn.homecaught.airplus.adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

import cn.homecaught.airplus.R;
import cn.homecaught.airplus.bean.DeviceBean;
import cn.homecaught.airplus.bean.PM25Bean;

/**
 * Created by a1 on 16/2/27.
 */
public class MyDevicesAdapter extends BaseAdapter {

    private List<DeviceBean> mItems = null;
    private LayoutInflater mInflater;
    private Context mContext;

    public MyDevicesAdapter(Context context, List<DeviceBean> items) {

        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mItems = items;
    }

    public void reload(List<DeviceBean> items) {
        mItems = items;
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
        DeviceBean deviceBean = mItems.get(position);
        PM25Bean pm25Bean = deviceBean.getPm25Beans().get(deviceBean.getPm25Beans().size() - 1);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.device_item, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvDate = (TextView) convertView
                .findViewById(R.id.tvDate);
        viewHolder.tvName = (TextView) convertView
                .findViewById(R.id.tvName);
        viewHolder.tvSchool = (TextView) convertView
                .findViewById(R.id.tvSchool);
        viewHolder.tvValue = (TextView) convertView
                .findViewById(R.id.tvValue);
        viewHolder.tvStatus = (TextView) convertView
                .findViewById(R.id.tvStatus);

        viewHolder.tvDate.setText(pm25Bean.getReadingDate());
        viewHolder.tvName.setText(deviceBean.getName());
        viewHolder.tvSchool.setText(deviceBean.getSchoolName());
        viewHolder.tvValue.setText(pm25Bean.getReading());
        if (pm25Bean.getDisplayStatus().equals("orange")){
            viewHolder.tvStatus.setText("Light polluted");
            viewHolder.tvStatus.setBackgroundResource(R.drawable.orange_shape);
        }
        if (pm25Bean.getDisplayStatus().equals("yellow")){
            viewHolder.tvStatus.setText("Moderate");
            viewHolder.tvStatus.setBackgroundResource(R.drawable.yellow_shape);
        }
        if (pm25Bean.getDisplayStatus().equals("green")){
            viewHolder.tvStatus.setText("Good");
            viewHolder.tvStatus.setBackgroundResource(R.drawable.green_shape);
        }
        if (pm25Bean.getDisplayStatus().equals("red")){
            viewHolder.tvStatus.setText("Unhealthy");
            viewHolder.tvStatus.setBackgroundResource(R.drawable.red_shape);
        }

        if (pm25Bean.getDisplayStatus().equals("purple")){
            viewHolder.tvStatus.setText("Very Unhealthy");
            viewHolder.tvStatus.setBackgroundResource(R.drawable.purple_shape);
        }

        if (pm25Bean.getDisplayStatus().equals("maroon")){
            viewHolder.tvStatus.setText("Hazardous");
            viewHolder.tvStatus.setBackgroundResource(R.drawable.maroon_shape);
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvDate;
        TextView tvName;
        TextView tvSchool;
        TextView tvValue;
        TextView tvStatus;

    }
}
