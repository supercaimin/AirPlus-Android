package cn.homecaught.airplus.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.design.widget.Snackbar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.homecaught.airplus.R;
import cn.homecaught.airplus.adapter.MySelectAdapter;
import cn.homecaught.airplus.bean.CityBean;
import cn.homecaught.airplus.bean.DeviceBean;
import cn.homecaught.airplus.bean.PM25Bean;
import cn.homecaught.airplus.bean.SchoolBean;
import cn.homecaught.airplus.bean.UserBean;
import cn.homecaught.airplus.util.HttpData;
import cn.homecaught.airplus.util.SharedPreferenceManager;

public class SelectActivity extends AppCompatActivity {

    private ListView listView = null;
    private TextView tvTip;
    private String action;
    private String type;

    private String id;

    private List<Object> items;

    private MySelectAdapter mySelectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Intent intent = this.getIntent();
        action = intent.getStringExtra("action");
        type = intent.getStringExtra("type");
        tvTip = (TextView) findViewById(R.id.tvTip);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.listView);

        if(type.equals("city")){
            actionBar.setTitle("Select City");
            tvTip.setText("Please choose your city.");
            if (!action.equals("add")){

                Snackbar.make(tvTip, "Welcome to sign up with AirPlus.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
            new GetCityTask().execute();
        }else if(type.equals("school")){
            actionBar.setTitle("Select School");
            id = intent.getStringExtra("id");
            new GetSchoolTask().execute();
            tvTip.setText("Please choose your school.");

        }else {
            actionBar.setTitle("Select Device");
            id = intent.getStringExtra("id");
            new GetDeviceTask().execute();
            tvTip.setText("Please choose one or all locations for PM2.5 datas.");

        }

        final List<String> xitems = new ArrayList<String>();
        mySelectAdapter = new MySelectAdapter(this, xitems, xitems);
        listView.setAdapter(mySelectAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(type.equals("city")){
                    Intent intent = new Intent(SelectActivity.this, SelectActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type",  "school");
                    bundle.putString("action", action);
                    CityBean cityBean = (CityBean) items.get(position);
                    bundle.putString("id", cityBean.getUid());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                if(action.equals("add") && type.equals("school")){
                    SchoolBean schoolBean = (SchoolBean) items.get(position);

                    if (schoolBean.getIsOpen().equals("1")) {
                        Intent intent = new Intent(SelectActivity.this, SelectActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("type",  "device");
                        bundle.putString("action", action);
                        bundle.putString("id", schoolBean.getUid());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else {
                        if (schoolBean.getUid().equals(UserBean.getInstance().getSchoolId())){
                            Intent intent = new Intent(SelectActivity.this, SelectActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("type",  "device");
                            bundle.putString("action", action);
                            bundle.putString("id", schoolBean.getUid());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else {
                            Snackbar.make(tvTip, "The school is not allowed to access. Please contact the admin to get the account.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            return;
                        }
                    }

                }
                if(action.equals("add") && type.equals("device")){
                    try{
                        DeviceBean deviceBean = (DeviceBean) items.get(position);
                        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getApplicationContext(),
                                SharedPreferenceManager.PREFERENCE_FILE);
                        JSONObject pmJsonObject = new JSONObject(preferenceManager.getPMData());
                        JSONArray pmArray = pmJsonObject.getJSONArray("data");

                        List<PM25Bean> pm25BeanList = new ArrayList<PM25Bean>();

                        for (int j = 0; j < pmArray.length(); j ++){
                            JSONObject pmObject = pmArray.getJSONObject(j);
                            if (pmObject.getString("serial").equals(deviceBean.getSerial())){
                                PM25Bean pm25Bean = new PM25Bean();
                                pm25Bean.setReading(pmObject.getString("reading"));
                                pm25Bean.setReadingDate(pmObject.getString("readingDate"));
                                pm25Bean.setDisplayStatus(pmObject.getString("displayStatus"));
                                pm25BeanList.add(pm25Bean);
                            }
                        }
                        deviceBean.setPm25Beans(pm25BeanList);
                        Intent intent = new Intent(SelectActivity.this, PMDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("device",  deviceBean);
                        bundle.putString("from", "select");
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }

                if(action.equals("register") && type.equals("school")){
                    SchoolBean schoolBean = (SchoolBean) items.get(position);

                    if (schoolBean.getIsOpen().equals("1")){
                        Intent intent = new Intent(SelectActivity.this, RegisterActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("schoolId", schoolBean.getUid());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else {
                        Snackbar.make(tvTip, "The school is not allowed to access. Please contact the admin to get the account.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }

                }
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetCityTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            return HttpData.getCity();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            items = new ArrayList<Object>();
            List<String> datas = new ArrayList<>();
            List<String> logoDatas = new ArrayList<>();

            try{

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i ++){
                    JSONObject o = jsonArray.getJSONObject(i);
                    CityBean cityBean = new CityBean();
                    cityBean.setKey(o.getString("city_key"));
                    cityBean.setName(o.getString("city_name"));
                    cityBean.setUid(o.getString("id"));
                    cityBean.setLogo(o.getString("logo"));
                    items.add(cityBean);
                    datas.add(cityBean.getKey());
                    logoDatas.add(cityBean.getLogo());
                }

            }catch (JSONException e){
                e.printStackTrace();
            }

            mySelectAdapter.reload(datas, logoDatas);
        }
    }

    private class GetSchoolTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            return HttpData.getSchoolsByCity(id);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            items = new ArrayList<Object>();
            List<String> datas = new ArrayList<>();
            List<String> logoDatas = new ArrayList<>();
            Log.i("AirPlus", s);

            try{

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i ++){
                    JSONObject o = jsonArray.getJSONObject(i);
                    SchoolBean schoolBean = new SchoolBean();
                    schoolBean.setName(o.getString("name"));
                    schoolBean.setUid(o.getString("id"));
                    schoolBean.setLogo(o.getString("logo"));
                    schoolBean.setIsOpen(o.getString("is_open"));
                    items.add(schoolBean);
                    datas.add(schoolBean.getName());
                    logoDatas.add(schoolBean.getLogo());
                }

            }catch (JSONException e){
                e.printStackTrace();
            }

            mySelectAdapter.reload(datas, logoDatas);
        }
    }

    private class GetDeviceTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            return HttpData.getDeviceBySchool(id);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            items = new ArrayList<Object>();
            List<String> datas = new ArrayList<>();
            Log.i("AirPlus", s);
            try{

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i ++){
                    JSONObject o = jsonArray.getJSONObject(i);
                    DeviceBean deviceBean = new DeviceBean();
                    deviceBean.setName(o.getString("name"));
                    deviceBean.setUid(o.getString("id"));
                    deviceBean.setSerial(o.getString("serial"));
                    deviceBean.setSchoolId(o.getString("school_id"));
                    deviceBean.setCityKey(o.getString("city_key"));
                    items.add(deviceBean);
                    datas.add(deviceBean.getName());
                }

            }catch (JSONException e){
                e.printStackTrace();
            }

            mySelectAdapter.reload(datas, null);
        }
    }
}
