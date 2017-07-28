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
    private static boolean bExit;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        bExit = false;
        // TODO code application logic here
        int port = 6666;       
        try {
            ServerSocket ss = new ServerSocket(port); // создаем сокет сервера и привязываем его к вышеуказанному порту
            System.out.println("Waiting for a client...");
            
            while(!bExit){
                Socket socket = ss.accept(); // заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером
                System.out.println("Got a client :) ... Finally, someone saw me through all the cover!");
                System.out.println();
                
                readData(socket);
                
                socket.close();
                System.out.println("Client disconect");
                System.out.println();
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static void readData(Socket socket){
        try {
            BufferedReader in = null;
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input;

            while ((input = in.readLine()) != null) {
                    System.out.println(input);
            }
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
