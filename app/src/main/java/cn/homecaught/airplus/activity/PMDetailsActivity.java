package cn.homecaught.airplus.activity;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import cn.homecaught.airplus.MyApplication;
import cn.homecaught.airplus.R;
import cn.homecaught.airplus.bean.DeviceBean;
import cn.homecaught.airplus.bean.PM25Bean;
import cn.homecaught.airplus.bean.UserBean;
import cn.homecaught.airplus.util.HttpData;
import cn.homecaught.airplus.view.ImageCycleView;


import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class PMDetailsActivity extends AppCompatActivity {
    private ImageCycleView mAdView;

    private LineChartView chart;
    private LineChartData data;
    private TextView tvValue;
    private TextView tvOutValue;
    private TextView tvX;
    private TextView tvStatus;
    private Button btnAdd;

    private String from = null;
    private DeviceBean deviceBean;
    private String outPM25s[];


    private int numberOfLines = 2;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 24;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    private boolean hasAxes = true;
    private boolean hasAxesNames = false;
    private boolean hasLines = true;
    private boolean hasPoints = false;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmdetails);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mAdView = (ImageCycleView) findViewById(R.id.ad_view);
        //mAdView.setLocalImageResources(mImages, mAdCycleViewListener);

        chart = (LineChartView) findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        chart.setInteractive(false);

        tvValue = (TextView) findViewById(R.id.tvValue);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvX = (TextView) findViewById(R.id.tvX);
        tvOutValue = (TextView) findViewById(R.id.tvOutValue);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        Intent intent = this.getIntent();
        deviceBean = (DeviceBean)intent.getSerializableExtra("device");
        from = intent.getStringExtra("from");

        if(from.equals("main")){
            btnAdd.setText("- REMOVE FROM HOME");
        }else {
            btnAdd.setText("+ ADD TO HOME");
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnAdd.getText().equals("+ ADD TO HOME")){
                    new AddUserDeviceTask().execute();
                }else {
                    new DelUserDeviceTask().execute();
                }
            }
        });

        PM25Bean pm25Bean = deviceBean.getPm25Beans().get(deviceBean.getPm25Beans().size() - 1);
        actionBar.setTitle(deviceBean.getName());
        tvValue.setText(pm25Bean.getReading());

        if (pm25Bean.getDisplayStatus().equals("orange")){
            tvStatus.setText("Light polluted");
            tvStatus.setBackgroundResource(R.drawable.orange_shape);
        }
        if (pm25Bean.getDisplayStatus().equals("yellow")){
            tvStatus.setText("Moderate");
            tvStatus.setBackgroundResource(R.drawable.yellow_shape);
        }
        if (pm25Bean.getDisplayStatus().equals("green")){
            tvStatus.setText("Good");
            tvStatus.setBackgroundResource(R.drawable.green_shape);
        }
        if (pm25Bean.getDisplayStatus().equals("red")){
            tvStatus.setText("Unhealthy");
            tvStatus.setBackgroundResource(R.drawable.red_shape);
        }

        if (pm25Bean.getDisplayStatus().equals("purple")){
            tvStatus.setText("Very Unhealthy");
            tvStatus.setBackgroundResource(R.drawable.purple_shape);
        }

        if (pm25Bean.getDisplayStatus().equals("maroon")){
            tvStatus.setText("Hazardous");
            tvStatus.setBackgroundResource(R.drawable.maroon_shape);
        }

        new GetOutDoorPMDataTask().execute();

    }

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
        @Override
        public void displayImageURL(String imageURL, ImageView imageView) {
            DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
            ImageLoader.getInstance().displayImage(imageURL, imageView, imageOptions);
        }

        @Override
        public void displayImageResId(Integer resId, ImageView imageView) {
        }

        @Override
        public void onImageClick(int position, View imageView) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void generateValues() {
        ArrayList<String> hours = new ArrayList<String>();
        ArrayList<String> pms =  new ArrayList<String>();
        List<PM25Bean> pm25Beans = deviceBean.getPm25Beans();
        Collections.reverse(pm25Beans);
        for (int i = 0; i < pm25Beans.size(); i ++){
            PM25Bean pm25Bean = pm25Beans.get(i);
            String date = pm25Bean.getReadingDate();
            String hour = date.substring(11, 13);
            if(hours.isEmpty()){
                hours.add(hour);
                pms.add(pm25Bean.getReading());
            }else {
                if (!hours.get(hours.size() - 1).equals(hour)){
                    hours.add(hour);
                    pms.add(pm25Bean.getReading());
                }
            }
            if(hours.size() == 24){
                break;
            }
        }
        Collections.reverse(pms);


        for (int i = 0; i < numberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                if(i == 0) randomNumbersTab[i][j] = Integer.valueOf(outPM25s[j]);
                if(i == 1){
                    randomNumbersTab[i][j] = Integer.valueOf(pms.get(j));
                }

            }
        }

    }

    private void reset() {
        numberOfLines = 2;

        hasAxes = true;
        hasAxesNames = true;
        hasLines = true;
        hasPoints = true;
        shape = ValueShape.CIRCLE;
        isFilled = false;
        hasLabels = false;
        isCubic = false;
        hasLabelForSelected = false;
        pointsHaveDifferentColor = false;

        chart.setValueSelectionEnabled(hasLabelForSelected);
        resetViewport();
    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;

        int max = 0;
        for(int i = 0; i < outPM25s.length; i ++){
            int value = Integer.valueOf(outPM25s[i]);
            if(value > max) max = value;
        }
        v.top = max;
        v.left = 0;
        v.right = numberOfPoints - 1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    private void generateData() {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            line.setStrokeWidth(1);
            if (pointsHaveDifferentColor){
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            axisX.setHasTiltedLabels(false);
            List<AxisValue> list = new ArrayList<AxisValue>();
            for (int i = 0; i < 24; i ++){
                AxisValue axisValue = new AxisValue(i);
                axisValue.setLabel(".");
                list.add(axisValue);
            }
            axisX.setValues(list);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("");
                axisY.setName("");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

    }

    /**
     * Adds lines to data, after that data should be set again with
     * {@link LineChartView#setLineChartData(LineChartData)}. Last 4th line has non-monotonically x values.
     */
    private void addLineToData() {
        if (data.getLines().size() >= maxNumberOfLines) {
            return;
        } else {
            ++numberOfLines;
        }

        generateData();
    }

    private void toggleLines() {
        hasLines = !hasLines;

        generateData();
    }

    private void togglePoints() {
        hasPoints = !hasPoints;

        generateData();
    }

    private void toggleCubic() {
        isCubic = !isCubic;

        generateData();

        if (isCubic) {
            // It is good idea to manually set a little higher max viewport for cubic lines because sometimes line
            // go above or below max/min. To do that use Viewport.inest() method and pass negative value as dy
            // parameter or just set top and bottom values manually.
            // In this example I know that Y values are within (0,100) range so I set viewport height range manually
            // to (-5, 105).
            // To make this works during animations you should use Chart.setViewportCalculationEnabled(false) before
            // modifying viewport.
            // Remember to set viewport after you call setLineChartData().
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.bottom = -5;
            v.top = 105;
            // You have to set max and current viewports separately.
            chart.setMaximumViewport(v);
            // I changing current viewport with animation in this case.
            chart.setCurrentViewportWithAnimation(v);
        } else {
            // If not cubic restore viewport to (0,100) range.
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.bottom = 0;
            v.top = 100;

            // You have to set max and current viewports separately.
            // In this case, if I want animation I have to set current viewport first and use animation listener.
            // Max viewport will be set in onAnimationFinished method.
            chart.setViewportAnimationListener(new ChartAnimationListener() {

                @Override
                public void onAnimationStarted() {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationFinished() {
                    // Set max viewpirt and remove listener.
                    chart.setMaximumViewport(v);
                    chart.setViewportAnimationListener(null);

                }
            });
            // Set current viewpirt with animation;
            chart.setCurrentViewportWithAnimation(v);
        }

    }

    private void toggleFilled() {
        isFilled = !isFilled;

        generateData();
    }

    private void togglePointColor() {
        pointsHaveDifferentColor = !pointsHaveDifferentColor;

        generateData();
    }

    private void setCircles() {
        shape = ValueShape.CIRCLE;

        generateData();
    }

    private void setSquares() {
        shape = ValueShape.SQUARE;

        generateData();
    }

    private void setDiamonds() {
        shape = ValueShape.DIAMOND;

        generateData();
    }

    private void toggleLabels() {
        hasLabels = !hasLabels;

        if (hasLabels) {
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled(hasLabelForSelected);
        }

        generateData();
    }

    private void toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected;

        chart.setValueSelectionEnabled(hasLabelForSelected);

        if (hasLabelForSelected) {
            hasLabels = false;
        }

        generateData();
    }

    private void toggleAxes() {
        hasAxes = !hasAxes;

        generateData();
    }

    private void toggleAxesNames() {
        hasAxesNames = !hasAxesNames;

        generateData();
    }

    /**
     * To animate values you have to change targets values and then call {@link Chart#startDataAnimation()}
     * method(don't confuse with View.animate()). If you operate on data that was set before you don't have to call
     * {@link LineChartView#setLineChartData(LineChartData)} again.
     */
    private void prepareDataAnimation() {
        for (Line line : data.getLines()) {
            for (PointValue value : line.getValues()) {
                // Here I modify target only for Y values but it is OK to modify X targets as well.
                value.setTarget(value.getX(), (float) Math.random() * 100);
            }
        }
    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    private class GetSchoolImagesTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            return HttpData.getSchoolImages(deviceBean.getSchoolId());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<String> images = new ArrayList<>();

            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray =  jsonObject.getJSONObject("data").getJSONArray("images");
                for (int i = 0; i < jsonArray.length(); i++){
                    images.add(jsonArray.getString(i));
                }

            }catch (JSONException e){

            }

            mAdView.setNetWorkImageResources(images, mAdCycleViewListener);
        }
    }

    private class GetOutDoorPMDataTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            return HttpData.getOutdoorData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int count = 0;
            for (int i = 0; i < result.length(); i++) {
                char c = result.charAt(i);

                if (c == 'd') {
                    String mark = result.substring(i, i + 6);
                    if(mark.equals("data:[")){
                        if (count < 1) {
                            count ++;
                            continue;
                        }
                        int j = i;
                        while (result.charAt(j)!= ']') {
                            j ++;
                        }
                        String pm25Str = result.substring(i+ 6, j);
                        outPM25s = pm25Str.split(",");
                        break;
                    }
                }

            }
            // Generate some randome values.
            float outValue = Float.valueOf(outPM25s[outPM25s.length - 1]);
            PM25Bean pm25Bean = deviceBean.getPm25Beans().get(deviceBean.getPm25Beans().size() - 1);
            float intValue = Float.valueOf(pm25Bean.getReading());
            tvX.setText(String .format("%.1f", outValue / intValue) + "x");
            tvOutValue.setText(outPM25s[outPM25s.length - 1]);
            generateValues();

            generateData();

            // Disable viewpirt recalculations, see toggleCubic() method for more info.
            chart.setViewportCalculationEnabled(false);
            chart.startDataAnimation();

            resetViewport();

            new GetSchoolImagesTask().execute();

            //new Thread(new MyThread()).start();

        }
    }


    private class AddUserDeviceTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            return HttpData.addUserDevice(UserBean.getInstance().getUid(), deviceBean.getUid());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            btnAdd.setText("- REMOVE FROM HOME");
            refresh();

        }
    }

    private class DelUserDeviceTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            return HttpData.delUserDevice(UserBean.getInstance().getUid(), deviceBean.getUid());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            btnAdd.setText("+ ADD TO HOME");
            refresh();

        }
    }
    private void refresh(){
        Intent intent = new Intent();
        intent.setAction(MyApplication.REFRESH_DATA_NOTIFICATION);
        sendBroadcast(intent);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            super.handleMessage(msg);
            chart.clearAnimation();
            chart.resetViewports();
            generateData();
            chart.startDataAnimation();

        }
    };
    public class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    Thread.sleep(5 * 1000);// 线程暂停10秒，单位毫秒
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


}
