package com.zlobrynya.compconsole;


import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Delayed;


/**
 * Created by Nikita on 14.08.2017.
 */

public class SenderThread extends AsyncTask<Void, Void, Void>{

   private BlockingQueue blockingQueue;

    private Socket socket;
    private boolean bStop;
    private boolean bSendData;
    private BufferedReader in;
    private DataOutputStream out;

    private String pseudoID;
    private String serIpAddress;
    private int port;




    public SenderThread(String pseudoID,String serIpAddress, int port,BlockingQueue blockingQueue){
        this.pseudoID = pseudoID;
        this.serIpAddress = serIpAddress;
        this.port = port;
        this.blockingQueue = blockingQueue;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
           /* mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    // process incoming messages here
                    //Log.e("Thread", "#"+number + ": "+msg.getData().getString("KEY"));
                }
            };*/

            bStop = false;
            bSendData = true;
            if (socket == null){
                createSoket();
            }
            // Получаем потоки ввод/вывода
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.writeBytes("id,"+pseudoID+"\n");
            dataExchange();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        } finally {

        }
        return null;
    }

    private void dataExchange(){
        while (!bStop){
            try {
                if (bSendData){
                    String data = (String) blockingQueue.poll();
                    if (data != null)
                        out.writeBytes(data+"\n");
                }
                //Log.i("DEBUUG","---");
                //Проверка ответ серва
                if (in.ready()){
                    String str = in.readLine();
                    Log.i("DEBUUG",str);
                    if (str.contains(MainActivity.DISC))
                        bStop = true;
                    else if (str.contains(MainActivity.ALLOWED))
                        bSendData = true;
                }
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                bStop = true;
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
            Log.i("ppp","async close");
            out.close();
            in.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onPostExecute(result);
    }
}
