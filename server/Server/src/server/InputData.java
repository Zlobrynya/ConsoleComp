/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nikita
 */
public class InputData {
    private Socket socket;
    private boolean bExit;
    
    private final String ID = "Id";
    private final String DISCONECT = "disc";
    
    
    
    public InputData(Socket socket){
        this.socket = socket;
        bExit = false;
    }
    
    public boolean getbExit(){
        return bExit;
    }
    
    public void readData(Socket socket){
        try {
            BufferedReader in = null;
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input;
            
            while ((input = in.readLine()) != null) {
                System.out.println(input);
                String act = input.split(",")[0];
                String data = input.split(",")[1];
                chooseAct(act,data);
                System.out.println(act + " " + data);

            }

            in.close();
        } catch (IOException ex) {
            Logger.getLogger(InputData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void chooseAct(String act,String data){
        if (act.contains(ID)){
            //временно
            bExit = true;
        }else if (act.contains(DISCONECT)){
            bExit = true;
        }
    }
}
