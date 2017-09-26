package eminem.ren.okhttp3_network_tool.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import eminem.ren.okhttp3_network_tool.R;
import eminem.ren.okhttp3_network_tool.download.DownloadManager;
import eminem.ren.okhttp3_network_tool.event.DownloadProgressEvent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_start, btn_pause, btn_continue, btn_cancel;
    private TextView tv, tv2, tv3, tv4;
    //private static String url = "http://dlied5.myapp.com/myapp/1104466820/sgame/2017_com.tencent.tmgp.sgame_h100_1.22.1.5.apk";
    //private String url = "http://firmware.birdytone.com.cn/download/OTA/Zipp_Mini/Zipp_02_554_FULL_20170227.zip";
    private String url = "http://gdown.baidu.com/data/wisegame/fc163d814078183f/QQyinle_696.apk";
    private Timer downloadTimer;
    private Timer speedTimer;
    private int time;
    private boolean pauseFlag;
    private Handler mHandler = null;
    private DecimalFormat showFloatFormat = new DecimalFormat("0.00");
    private long downloadCurrentFinishTotal = 0;
    private long downloadCurrentFinishTotal_temp = 0;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        btn_start = (Button) findViewById(R.id.button);
        btn_start.setOnClickListener(this);
        btn_pause = (Button) findViewById(R.id.button2);
        btn_pause.setOnClickListener(this);
        btn_continue = (Button) findViewById(R.id.button3);
        btn_continue.setOnClickListener(this);
        btn_cancel = (Button) findViewById(R.id.button4);
        btn_cancel.setOnClickListener(this);
        tv = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        tv4 = (TextView) findViewById(R.id.textView4);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        if (!pauseFlag) {
                            tv2.setText("Time    :   " + time + "  s");
                            time++;
                        }
                        break;
                    case 2:
                        updateSpeed();
                        break;
                }

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                DownloadManager.getInstance().add("1", "libratone.apk", url, getAppFilePath());
                DownloadManager.getInstance().download(url);
                downloadTimer = new Timer();
                downloadTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(1);
                    }
                }, 0, 1000);

                speedTimer = new Timer();
                speedTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(2);
                    }
                }, 0, 1000);

                break;
            case R.id.button2:
                pauseFlag = true;
                DownloadManager.getInstance().pause(url);
                break;
            case R.id.button3:
                pauseFlag = false;
                DownloadManager.getInstance().download(url);
                break;
            case R.id.button4:
                DownloadManager.getInstance().cancel(url);
                cancelTimer();
                tv.setText("Progress    :   " + 0 + "  %");
                tv2.setText("Time    :   " + time + "  s");
                tv3.setText("Size    :   ");
                break;
        }
    }

    private void cancelTimer() {
        downloadTimer.cancel();
        pauseFlag = false;
        time = 0;
        downloadCurrentFinishTotal = 0;
        downloadCurrentFinishTotal_temp = 0;
    }

    public String getAppFilePath() {
        if (!TextUtils.isEmpty(getLibratoneStorageDirectory())) {
            File file = new File(getLibratoneStorageDirectory() + File.separator + "downloadDir");
            if (!file.exists()) file.mkdirs();
            return file.getPath();
        }
        return "";
    }

    public String getLibratoneStorageDirectory() {
        //'   /storage/emulated/0/Android/data/com.libratone/files/log'
        File externalFile = getExternalFilesDir(null);  // external storage
        if (externalFile != null) {
            return externalFile.getPath();
        } else {
            //      /data/user/0/com.libratone/files
            final File internalFile = getFilesDir(); // internal storage
            if (internalFile != null) {
                return internalFile.getPath();
            }
        }
        return "";
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DownloadProgressEvent event) {
        if (event.getProgress() == 100) {
            cancelTimer();
        }
        tv.setText("Progress    :   " + event.getProgress() + "  %");
        tv3.setText("Size    :   " + showSize(event.getCurrentSize()) + "   /   " + showSize(event.getTotal()));
        downloadCurrentFinishTotal_temp = event.getCurrentSize();
    }

    private void updateSpeed() {
        long totalSpeed = downloadCurrentFinishTotal_temp - downloadCurrentFinishTotal;
        downloadCurrentFinishTotal = downloadCurrentFinishTotal_temp;
        tv4.setText("Size    :   " + showSpeed(totalSpeed));
    }

    private String showSpeed(long speed) {
        String speedString;
        if (speed >= 1048576d) {
            speedString = showFloatFormat.format(speed / 1048576d) + "MB/s";
        } else {
            speedString = showFloatFormat.format(speed / 1024d) + "KB/s";
        }
        return speedString;
    }

    private String showSize(long speed) {
        String speedString;
        if (speed >= 1048576d) {
            speedString = showFloatFormat.format(speed / 1048576d) + "MB";
        } else {
            speedString = showFloatFormat.format(speed / 1024d) + "KB";
        }
        return speedString;
    }
}
