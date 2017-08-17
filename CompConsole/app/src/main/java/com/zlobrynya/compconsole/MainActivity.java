package com.zlobrynya.compconsole;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zlobrynya.compconsole.fragment.VideoFragment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MainActivity extends AppCompatActivity {
    //Очередь для передачи сообщений в поток.
    public static BlockingQueue blockingQueue;
    private SenderThread senderThread;
    private int i;

    private VideoFragment videoFragment;
    private FragmentTransaction fTrans;

    public static final String ALLOWED = "allowed";
    public static final String DEBUG = "deb";
    public static final String VOLUME = "volume";


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
        int port = 6666;
        senderThread = new SenderThread(pseudoID, serIpAddress, port,blockingQueue);
        senderThread.execute();

        videoFragment = new VideoFragment();
        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.fragment,videoFragment);
        i = 0;
    }

    public void clickButton(View view) {
        i++;
        try {
            blockingQueue.put(DEBUG+","+String.valueOf(i));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        senderThread.cancel(false);
        super.onDestroy();
    }

}
