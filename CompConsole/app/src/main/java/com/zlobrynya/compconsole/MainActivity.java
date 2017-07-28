package com.zlobrynya.compconsole;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    String serIpAddress = "192.168.0.50";
    int port = 6666;
    String pseudoID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pseudoID = " " + Build.BOARD.length()%10 + Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10;

        Log.i("Build", String.valueOf(pseudoID));

        setContentView(R.layout.activity_main);
        SenderThread senderThread = new SenderThread();
        senderThread.execute();
    }



    class SenderThread extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                // ip адрес сервера
                InetAddress ipAddress = InetAddress.getByName(serIpAddress);
                // Создаем сокет
                Socket socket = new Socket(ipAddress, port);
                // Получаем потоки ввод/вывода
               /* OutputStream outputStream = socket.getOutputStream();
                DataOutputStream out = new DataOutputStream(outputStream);
                Log.i("BuildByte", String.valueOf(pseudoID));
                out.writeInt(256);*/

                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                out.print(pseudoID);
                out.close();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            return null;
        }
    }
}
