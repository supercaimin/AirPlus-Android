package cn.homecaught.airplus.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import cn.homecaught.airplus.MyApplication;
import cn.homecaught.airplus.R;
import cn.homecaught.airplus.adapter.MyDevicesAdapter;
import cn.homecaught.airplus.bean.DeviceBean;
import cn.homecaught.airplus.bean.PM25Bean;
import cn.homecaught.airplus.bean.UserBean;
import cn.homecaught.airplus.util.DialogTool;
import cn.homecaught.airplus.util.HttpData;
import cn.homecaught.airplus.util.SharedPreferenceManager;
import cn.homecaught.airplus.view.RefreshableView;
import cn.homecaught.airplus.view.RefreshableView.PullToRefreshListener;

import android.content.DialogInterface.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mListView = null;
    private List<DeviceBean> mDevices = null;
    private MyDevicesAdapter mMyDevicesAdapter = null;

    RefreshableView mRefreshableView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type",  "city");
                bundle.putString("action", "add");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mListView = (ListView)findViewById(R.id.listView);
        mDevices = new ArrayList<DeviceBean>();

        mMyDevicesAdapter = new MyDevicesAdapter(this, mDevices);
        mListView.setAdapter(mMyDevicesAdapter);

        reloadListViewData();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, PMDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("device",  mDevices.get(position));
                bundle.putString("from", "main");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mRefreshableView = (RefreshableView) findViewById(R.id.refreshableView);
        mRefreshableView.setOnRefreshListener(new PullToRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        }, 0);

        // 注册自定义动态广播消息
        IntentFilter filter_dynamic = new IntentFilter();
        filter_dynamic.addAction(MyApplication.REFRESH_DATA_FINISHED_NOTIFICATION);
        registerReceiver(dynamicReceiver, filter_dynamic);

        refresh();

    }


    private BroadcastReceiver dynamicReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyApplication.REFRESH_DATA_FINISHED_NOTIFICATION)){
                new GetUserInstrumentsTask().execute();
            }
        }

        @Override
        public IBinder peekService(Context myContext, Intent service) {
            return super.peekService(myContext, service);
        }

    };

    private void refresh(){
        Intent intent = new Intent();
        intent.setAction(MyApplication.REFRESH_DATA_NOTIFICATION);
        sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.homeItem) {
            // Handle the camera action
        } else if (id == R.id.langItem) {
            Dialog dialog = DialogTool.createConfirmDialog(this, "Change Language", "Please select your langugage",
                    "English", "中文",
                    new OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    },
                    new OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, DialogTool.NO_ICON);
            dialog.show();
        } else if (id == R.id.aboutItem) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOutItem) {
            new SetInstallationIdTask("").execute();

            Dialog dialog = DialogTool.createConfirmDialog(this, "Confirm", "Sign out?",
                    "YES", "NO",
                    new OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            UserBean.getInstance().clear();

                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                            dialog.dismiss();

                            finish();
                        }
                    },
                    new OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, DialogTool.NO_ICON);
            dialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void reloadListViewData()
    {
        mDevices.clear();
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getApplicationContext(),
                SharedPreferenceManager.PREFERENCE_FILE);

        try{
            JSONObject jsonObject = new JSONObject(preferenceManager.getUserData());
            JSONObject pmJsonObject = new JSONObject(preferenceManager.getPMData());
            JSONArray pmArray = pmJsonObject.getJSONArray("data");
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i ++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                DeviceBean deviceBean = new DeviceBean();
                deviceBean.setUid(jsonObject1.getString("id"));
                deviceBean.setName(jsonObject1.getString("name"));
                deviceBean.setSchoolId(jsonObject1.getString("school_id"));
                deviceBean.setSchoolName(jsonObject1.getString("school_name"));
                deviceBean.setSerial(jsonObject1.getString("serial"));
                deviceBean.setCityKey(jsonObject1.getString("city_key"));

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
                mDevices.add(deviceBean);

            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        mMyDevicesAdapter.reload(mDevices);
    }

    public class  SetInstallationIdTask extends AsyncTask<Void, Void, String> {
        private  final String mInstallationId;

        public SetInstallationIdTask(String installationId){
            mInstallationId = installationId;
        }

        @Override
        protected String doInBackground(Void... params) {
            return HttpData.setInstallationId(UserBean.getInstance().getUid(), mInstallationId);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private class GetUserInstrumentsTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            String uid = UserBean.getInstance().getUid();
            if (uid != null && !uid.isEmpty()) {
                return HttpData.getUserInstruments(uid);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){
                SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getApplicationContext(),
                        SharedPreferenceManager.PREFERENCE_FILE);
                preferenceManager.setUserData(s);
                reloadListViewData();
            }

            mRefreshableView.finishRefreshing();
        }
    }
}
