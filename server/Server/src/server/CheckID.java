/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author Nikita
 */
public class CheckID {
    private File file;
    
    public CheckID(){
        //String path = Paths.get("").toAbsolutePath().toString()+"Id";
        file = new File("Id");
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(CheckID.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean check(String id){
        boolean check = false;
        String hashCodeId = id.hashCode() + "";
        System.out.println(hashCodeId);
        try {
            //Считывание с файла строки
            Stream<String> lines = Files.lines(file.toPath());
            if (lines.count() > 0){
                //Создаем заново stream, из за того, что он может создаваться только один раз
                lines = Files.lines(file.toPath());
                //Проверяем строки на соответвие hash коду id.
                String buf = lines.filter(hashCodeId::equals).findAny().orElse("");
                if (!buf.isEmpty()){
                    check = true;
                } else {
                    return connectionRequest(id);
                }
            }else{
                return connectionRequest(id);
            }
            lines.close();
        } catch (IOException ex) {
            Logger.getLogger(CheckID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return check;
    }
    
    //Запрос на разрешение подключиния устройства к компу.
    //@return true если подключение разрешено, false если запрящено.
    private boolean connectionRequest(String id){
        System.out.println("Неизвестное устройство пытается подключиться.");
        System.out.println("Индификатор устройства: " + id);
        System.out.println("Разрешаете подключиться устройству?(y/n)");
       
        Scanner in = new Scanner(System.in);
        String confirmation = in.next();
        if (confirmation.equals("y")){
            System.out.println("Подключение разрешено.");
            writeFile(id);
            return true;
        }else{
            System.out.println("Подключение запрещено.");
            return false;
        }
    }
    
    //запись нового индификатора устройства в файл.
    private void writeFile(String id){
        FileWriter writer = null;
        try { 
            String hashCodeId = id.hashCode() + "";
            writer = new FileWriter(file);
            writer.write(hashCodeId);
            writer.write('\n');
        } catch (IOException ex) {
            Logger.getLogger(CheckID.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException ex) {
                Logger.getLogger(CheckID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
