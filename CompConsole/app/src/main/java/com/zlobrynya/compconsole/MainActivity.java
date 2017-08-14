package com.zlobrynya.compconsole;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private String serIpAddress = "192.168.0.50";
    private int port = 6666;
    private String pseudoID;
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



    private class SenderThread extends AsyncTask<Void, Void, Void>
    {
        private Socket socket;
        private boolean bStop;
        private boolean bSendData;
        private BufferedReader in;

        private final String ALLOWED = "allowed";
        private final String DEBUG = "deb,";
        private final String DISCONECT = "disc";

        @Override
        protected Void doInBackground(Void... params) {
            try {
                bStop = false;
                bSendData = false;
                if (socket == null){
                    createSoket();
                }
                // Получаем потоки ввод/вывода
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.writeBytes("Id,"+pseudoID+"\n");
                run(out,in);
                out.close();
                in.close();
                Log.i("DEBUG","Stop");
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            } finally {
                if (socket != null)
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return null;
        }

        private void run(DataOutputStream out,BufferedReader in){
            while (!bStop){
                try {
                    if (bSendData)
                        out.writeBytes(DEBUG + "mess\n");
                    //Проверка ответ серва
                    if (in.ready()){
                        String str = in.readLine();
                        Log.i("Str",str);
                        if (str.contains(DEBUG))
                            bStop = true;
                        else if (str.contains(ALLOWED))
                            bSendData = true;
                    }
                    Thread.sleep(500);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void createSoket(){
            try {
                // ip адрес сервера
                InetAddress ipAddress = InetAddress.getByName(serIpAddress);
                // Создаем сокет
                socket = new Socket(ipAddress, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
}
