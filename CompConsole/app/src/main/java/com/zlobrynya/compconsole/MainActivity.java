package com.zlobrynya.compconsole;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zlobrynya.compconsole.fragment.StartFragment;
import com.zlobrynya.compconsole.fragment.VideoFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MainActivity extends AppCompatActivity {
    //Очередь для передачи сообщений в поток.
    public static BlockingQueue blockingQueue;

    public static final String ALLOWED = "allowed";
    public static final String DEBUG = "deb";
    public static final String DISC = "disc";
    public static final String KEY = "key";
    public static final String CONNECT = "con";

    //Code http://www.kbdedit.com/manual/low_level_vk_list.html
    public static final String KEY_PAUSE_CODE = "0xB3";
    public static final String KEY_VOLUME_MUTE_CODE = "0xAD";
    public static final String KEY_VOLUME_UP_CODE = "0xAF";
    public static final String KEY_VOLUME_DOWN_CODE = "0xAE";
    public static final String KEY_LEFT_CODE = "0x25";
    public static final String KEY_RIGHT_CODE = "0x27";
    public static final String KEY_MEDIA_NEXT_CODE = "0xB0";
    public static final String KEY_MEDIA_PREV_CODE = "0xB1";

    private SenderThread senderThread;
    private FragmentManager myFragmentManager;
    private boolean firstStart = true;

    private final static String TAG_1 = "FRAGMENT_START";
    private final static String TAG_2 = "FRAGMENT_VIDEO";
    //final static String TAG_3 = "FRAGMENT_3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String pseudoID = " " + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10;

        Log.i("Build", String.valueOf(pseudoID));

        blockingQueue = new ArrayBlockingQueue(100);

        setContentView(R.layout.activity_main);
        String serIpAddress = "192.168.0.50";
        int port = 13;
        Log.i("DEBUUG","Create");

      // VideoFragment videoFragment = new VideoFragment();
       /* StartFragment startFragment = new StartFragment();
        FragmentTransaction fragmentTransaction = myFragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.container, startFragment,
                TAG_1);
        fragmentTransaction.commit();*/


        senderThread = new SenderThread(pseudoID, serIpAddress, port,blockingQueue);
        senderThread.execute();
       // myFragmentManager = getSupportFragmentManager();

        VideoFragment videoFragment = new VideoFragment();
        //StartFragment startFragment = new StartFragment();
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.container, videoFragment,TAG_2);
        fTrans.commit();
    }

    @Override
    protected void onStop() {
        try {
            Log.i("DEBUUG","STOP");
            if (senderThread != null)
                blockingQueue.put(MainActivity.DISC+", ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!firstStart){
            try {
                if (senderThread != null) {
                    Log.i("DEBUUG","START");
                    blockingQueue.put(MainActivity.CONNECT);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        firstStart = false;
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
      /*  if (event == ALLOWED){
            VideoFragment videoFragment = new VideoFragment();
            FragmentTransaction fragmentTransaction = myFragmentManager
                    .beginTransaction();
            fragmentTransaction.replace(R.id.container, videoFragment,
                    TAG_1);
            fragmentTransaction.commit();
        }*/
    }
}
