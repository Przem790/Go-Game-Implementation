/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.mycompany.client;

import com.mycompany.brogogame.GameBoard;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 *
 * @author v
 */
public class Client implements ActionListener {

    public JCheckBox botinvolve;
    public GameBoard gameinstance;
    public JFrame frame,frame2;
    public JButton button1,button2,button3,button4;
    private PrintWriter out;
    private Socket socket;
    private int boardclienttype=-1;

    public Client()throws IOException{
        InetAddress addr = InetAddress.getByName(null);
        System.out.println();
        socket = new Socket(addr, 8080);

	    frame = new JFrame("TestClient");
        button1=new JButton("19x19 Board");          /**ten klient będzie miał 4 buttony gdzie wybieramy plansze albo wychodzimy **/
        button2=new JButton("13x13 Board");
        button3=new JButton("9x9 Board");
        button4=new JButton("exit");
	    botinvolve=new JCheckBox("Against Bot");
        frame.setLayout(new GridLayout(2,3));
        frame.setBounds(300,300,400,200);
	    frame.setVisible(true);
	    frame.setResizable(false);
        frame.add(button1);
        frame.add(button2);
        frame.add(button3);
        frame.add(button4);
        frame.add(botinvolve);
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
		
	    frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we) {
     		System.exit(0);
		}
	});


        try {
            
            System.out.println("Socket = " + socket);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                String str;
                while (true) {
                    str = in.readLine();

               if(!str.startsWith("OK")&&gameinstance!=null&&!str.equals("EXIT")&&!str.equals("EXITENEMY")){
                   System.out.println("OTRZYMANA WIADOMOść:"+str);
                   gameinstance.panel.interpretation(str);
                }
                else if(str.equals("EXIT")){
                   socket.close();
                   System.exit(0);
               }
               else if(str.equals("EXITENEMY")){
                   socket.close();
                   JFrame frame3 = new JFrame("VICTORY");
                   JLabel wygrana = new JLabel(" WYGRANA! Przeciwnik się poddał");
                   frame3.setSize(250,150);
                   wygrana.setLayout(new GridLayout(1,1));
                   frame3.add(wygrana);
                   frame3.setVisible(true);
                   //frame3.setResizable(false);
                   frame3.addWindowListener(new WindowAdapter(){
                       @Override
                       public void windowClosing(WindowEvent we) {
                           System.exit(0);
                       }
                   });

               }

                if(gameinstance==null&&boardclienttype!=-1)
                    gameinstance=new GameBoard(str,boardclienttype,out);
                    frame2.setVisible(false);
                }
        } finally {
            System.out.println("closed");
            socket.close();
        }
     }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object listener = evt.getSource();

        if(listener==button1) {
            if(botinvolve.isSelected()) {
                out.println("BOTSTART19");
                boardclienttype=0;
                waitframe();
            }else{
            out.println("START19");
            boardclienttype=0;
            waitframe();
            }
        }
        else if(listener==button2){
            if(botinvolve.isSelected()){
                out.println("BOTSTART13");
                boardclienttype=1;
                waitframe();
            }else{
            out.println("START13");
            boardclienttype=1;
            waitframe();

            }
        }
        else if(listener==button3) {
            if (botinvolve.isSelected()) {
                out.println("BOTSTART9");
                boardclienttype = 2;
                waitframe();
            } else {
                out.println("START9");
                boardclienttype = 2;
                waitframe();
            }
        }
        else if(listener==button4){
            out.println("ClientQuit");
            System.exit(0);
        }
    }


    public void waitframe(){
        frame.setVisible(false);
        frame2=new JFrame("GoGame");
        JLabel label = new JLabel("Please Wait");
        frame2.setBounds(300,300,100,100);
        frame2.setLayout(new FlowLayout());
        frame2.add(label);
        frame2.setVisible(true);
    }
    public static void main(String[] args) throws IOException {
        new Client();
    }
}
