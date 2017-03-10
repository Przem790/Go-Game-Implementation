/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.serverpackage;

/** Class to interprete string from com.mycompany.client
 *  Types: MOVE, SURRENDER, NOMOVE, TERRITORY ?????????
 *  
 * @author v
 */
public class Interpreter {
    
    public Interpreter() {
        
    }
    
    String getCommandType(String command) {      
        if (command.startsWith("BD:") || command.startsWith("WD:")) {
            return "MOVE";
        }
        /** TODO: implement rest of commands
        *
        */
        
        return "ERROR";
    }
    
    int getX(String command) {
        int x = 0;
        command = command.substring(3,command.length());
        String[] temp  = command.split(";");
        x = Integer.parseInt(temp[0]);
        return x;
    }
    
    int getY(String command) {
        int y = 0;
        command = command.substring(3,command.length());
        String[] temp  = command.split(";");
        y = Integer.parseInt(temp[1]);
        return y;
    }
    
}
