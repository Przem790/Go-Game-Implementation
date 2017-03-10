/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.serverpackage;

import java.net.Socket;

/**
 *
 * @author v
 */
public class Party {
    private Socket[] players;
    private int boardType;
    
    public Party(Socket sock1, Socket sock2, int boardType) {
        players = new Socket[2];
        players[0] = sock1;
        players[1] = sock2;
        this.boardType = boardType;
    }
    
    public int getType() {
        return boardType;
    }
}
