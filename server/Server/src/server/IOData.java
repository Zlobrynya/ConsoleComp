/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nikita
 */
public class IOData {
    private Socket socket;
    private boolean bRun;
    private CheckID checkID;
    private String send;
    private BufferedReader in = null;
    private DataOutputStream out = null;
    
    private final String ID = "Id";
    private final String DISCONECT = "disc";
    private final String ALLOWED = "allowed";
    private final String DEBUG = "deb";
    
    
    public IOData(Socket socket){
        try {
            this.socket = socket;
            bRun = true;
            send = "";
            checkID = new CheckID();
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(IOData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean getbRun(){
        return bRun;
    }
    
    public void close(){
        try {
            in.close();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(IOData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        while(bRun){
            readData();
            sendData(send);
            send = "";
        }
    }
    
    public void readData(){
        try {
            String input;
            if ((input = in.readLine()) != null) {
                //System.out.println(input);
                String act = input.split(",")[0];
                String data = input.split(",")[1];
                chooseAct(act,data);
                //System.out.println(act + " " + data);
            }
        } catch (IOException ex) {
            Logger.getLogger(IOData.class.getName()).log(Level.SEVERE, null, ex);
            bRun = true;
        } 
    }
    
    private void sendData(String data){
        if (data.isEmpty())
            return;
        try {
            System.out.println(data);
            out.writeBytes(data+"\n");
            //out.flush();
        } catch (IOException ex) {
            Logger.getLogger(IOData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void chooseAct(String act,String data){
        if (act.contains(ID)){
            //временно
            //checkID.check(data);
            bRun = checkID.check(data);
            if (bRun){
                send = ALLOWED;
            }else {
                send = DISCONECT;
            }    
        }else if (act.contains(DISCONECT)){
            bRun = false;
        }else if (act.contains(DEBUG)){
            System.out.println(DEBUG + " " + data);
        }
    }
}