/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.brogogame;

import javax.swing.*;
import java.io.PrintWriter;
import java.lang.*;



/**
 * klasa która tworzy okno oraz tworzy obiekt-panel
 * @author v
 */
public class GameBoard extends JFrame {
    JFrame okno;
    private int colornumber,boardtype;
    public GoBoardGame panel;
    private PrintWriter out;

    public GameBoard(String str, int boardtype, PrintWriter out){
        this.out=out;
        this.boardtype = boardtype;
        if(str.contentEquals("COLORWHITE"))
            colornumber=1;                                      //białe-1 , czarne-0
        else
            colornumber=0;

        okno = new JFrame("Go Game");                           //tworze okno GoGame

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);         //dodaje obsługę X - wyjście

        panel = new GoBoardGame(colornumber,boardtype,out);     //dodaje panel do okna
        okno.getContentPane().add(panel);
        if(boardtype==0||boardtype==1)
        okno.setSize(817,645);                                  //dla board 19x19 i 13x13
        else
        okno.setSize(705,534);                                  // dla board 9x9
        okno.setVisible(true);                                  //ustawiam na widoczny i bez możliwości zmiany rozmiaru
        okno.setResizable(false);
    }
}
