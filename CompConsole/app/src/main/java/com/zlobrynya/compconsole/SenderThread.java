package com.zlobrynya.compconsole;


import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

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
    private boolean bDisk;
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
            bStop = false;
            bSendData = true;

            Log.i("DEBUUG","POST");
            connect();
            dataExchange();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        } finally {

        }
        return null;
    }

    public void connect(){
        try {
            if (socket == null) {
                    createSoket();
            }
            if (socket.isClosed())
                createSoket();
            // Получаем потоки ввод/вывода
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.writeBytes("Id,"+pseudoID+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataExchange();
    }


    private void dataExchange(){
        while (true){
            try {
                if (!bStop){
                    if (bSendData){
                        String data = (String) blockingQueue.poll();
                        if (data != null){
                            out.writeBytes(data+"\n");
                            if (data.contains(MainActivity.DISC+", "))
                                bStop = true;
                        }
                    }
                    //Log.i("DEBUUG","---");
                    //Проверка ответ серва
                    if (in.ready()){
                        String str = in.readLine();
                        Log.i("DEBUUG",str);
                        if (str.contains(MainActivity.ALLOWED)) {
                            bSendData = true;
                            EventBus.getDefault().post(str);
                        }
                    }
                    if (bStop){
                        socket.close();
                    }
                }else{
                    String data = (String) blockingQueue.poll();
                    if (data != null){
                        Log.i("Connect",data);
                        if (data.contains(MainActivity.CONNECT)){
                            Log.i("Connect","true");
                            bStop = false;
                            connect();
                        }
                    }
                }
                Thread.sleep(300);
                Log.i("While", String.valueOf(bStop));

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                break;
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
