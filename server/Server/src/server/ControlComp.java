/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Nikita
 */
public class ControlComp {
    private final String VOLUME = "vol";
    
    
    public void chooseControl(String act,String data){
        if (act.contains(VOLUME)){
            controlVolume(data);
        }
    }
    
    
    private void controlVolume(String data){
        if (data.contains("+")){
            
        }else if (data.contains("0")){
            
        }else if (data.contains("-")){
            
        }
    }
}
