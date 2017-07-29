/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nikita
 */
public class Server {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // TODO code application logic here
        int port = 6666;       
        try {
            ServerSocket ss = new ServerSocket(port); // создаем сокет сервера и привязываем его к вышеуказанному порту
            System.out.println("Waiting for a client...");
            Socket socket = ss.accept();
            InputData inputData = new InputData(socket);

            while(!inputData.getbExit()){
                 // заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером
                System.out.println("Got a client :) ... Finally, someone saw me through all the cover!");
                System.out.println();
                
                inputData.readData(socket);
                
                
                System.out.println("Client disconect");
                System.out.println();
            }
            socket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
}
