/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.serverpackage;

import com.mycompany.gamepackage.Game;
import java.io.*;
import java.net.*;

/**     
 *      DO SERWERA "IN- "
 *      Z SERWERA "OUT- "
 *  IN- START19, START13, START9
 *  OUT- START19;WHITE, START13;BLACK ...
 *  IN- B:'X';'Y' np. B:12;18     B DLA BLACK, może to być łatwy sposób kontroli
 *  OUT- B:OK albo B:ERR    do czarnych
 *  OUT- B:12;18    do białych
 *  IN- W:'X';'Y' np. W:12;18     W DLA WHITE, może to być łatwy sposób kontroli
 *  OUT- W:OK albo W:ERR    do białych
 *  OUT- B:12;18   do czarnych
 * 
 * 
 * 
 * 
 * 
 * 
 * @author v
 */
public class ServOneThread extends Thread {
    private Socket[] players;
    private BufferedReader[] in;
    private PrintWriter[] out;
    private Game game;
        /** Setting inputs, outputs, players and starting a new thread
         * 
         */
    public ServOneThread(Socket sock, Socket sock2, int boardSize) throws IOException {
        players = new Socket[2];
        in = new BufferedReader[2];
        out = new PrintWriter[2];
        
        players[0] = sock;
        players[1] = sock2;

        in[0] = new BufferedReader(new InputStreamReader(players[0].getInputStream()));
        out[0] = new PrintWriter(new BufferedWriter(new OutputStreamWriter(players[0].getOutputStream())), true);
        /** Setting players color BLACK*/        
        out[0].println("COLORBLACK");
                
        in[1] = new BufferedReader(new InputStreamReader(players[1].getInputStream()));
        out[1] = new PrintWriter(new BufferedWriter(new OutputStreamWriter(players[1].getOutputStream())), true);
        /** Setting players color WHITE*/ 
        out[1].println("COLORWHITE");
        
        game = new Game(boardSize);
        System.out.println("Thread started");
        start();        
    }
    
    @Override
    public void run() {
        String[] str = new String[2];
        int turn = 0; 
        int passes = 0;
        
        try {
            BlackState black = new BlackState();
            WhiteState white = new WhiteState();
            while (!players[0].isClosed() && !players[1].isClosed()) {
                str[turn] = in[turn].readLine();
                System.out.println(str[turn]);
                if (str[turn] != null) {
                    Interpreter interpreter = new Interpreter();

                int x = 0, y = 0;
                int k = 0;
                game.resetWinBoard();
                /** If player exits */ 
                if (str[turn].equals("EXIT")) {
                    if (turn == 0) 
                        out[1].println("EXITENEMY");
                    else
                        out[0].println("EXITENEMY");
                    throw new IOException("Player disconnected");
                }
                /** What type of action it is. */
                if (interpreter.getCommandType(str[turn]).equals("MOVE")) {
                    x = interpreter.getX(str[turn]);
                    y = interpreter.getY(str[turn]);
                    passes = 0;
                }
                /** If player passes */
                if (str[turn].equals("PASS")) {
                    k++;
                    passes++;
                    if (passes == 1) {
                        if (turn == 0) 
                            out[1].println("PASS");
                        else
                            out[0].println("PASS");
                    } else {
                        passes = 0;
                        if (turn == 0) {
                            out[0].println("BOTHPASSED1");
                            out[1].println("BOTHPASSED2");
                            game.resetWinBoard();
                            game.suggestTerritory();
                            int select = 0;
                            while (select == 0) {
                                str[0] = in[0].readLine();
                                if (str[0].equals("CONFIRM")) {
                                    out[1].println("CONFIRM");
                                    select = 1;
                                } else if (str[0].equals("CONTINUE")) {
                                    out[0].println("CONTINUE1");
                                    out[1].println("CONTINUE2");
                                    select = 2;
                                } else if (str[0].equals("EXIT")) {
                                    out[1].println("EXITENEMY");
                                    throw new IOException("Player disconnected");
                                } else {
                                    x = interpreter.getX(str[0]);
                                    y = interpreter.getY(str[0]);
                                    game.putWinBoard(x, y, 0);
                                    out[1].println(str[0]);
                                    
                                }
                            }
                            if (select != 2)
                                select = 0;
                            while (select == 0) {
                                str[1] = in[1].readLine();
                                System.out.println(str[1]);
                                if (str[1].equals("CONFIRM")) {
                                    out[0].println("ACCEPT");
                                    str[0] = in[0].readLine();
                                    if (str[0].equals("CONTINUE")) {
                                        out[1].println("CONTINUE1");
                                        out[0].println("CONTINUE2");
                                        select = 2;
                                    } else if (str[0].equals("EXIT")) {
                                    out[1].println("EXITENEMY");
                                    throw new IOException("Player disconnected");
                                    } else {
                                        double bs = game.countBlackScore();
                                        double ws = game.countWhiteScore();
                                        System.out.println("B = "+bs);
                                        System.out.println("W = "+ws);
                                        if (bs > ws) {
                                            out[0].println("EXITWINNER;"+bs+";"+ws);
                                            out[1].println("EXITLOSER;"+bs+";"+ws);
                                        } else if (bs < ws) {
                                            out[1].println("EXITWINNER;"+bs+";"+ws);
                                            out[0].println("EXITLOSER;"+bs+";"+ws);
                                        } else {
                                            out[0].println("EXITDRAW;"+bs);
                                            out[1].println("EXITDRAW;"+bs);
                                        }
                                        //players[0].close();
                                        //players[1].close();
                                        //System.exit(0);
                                    }
                                } else if (str[1].equals("CONTINUE")) {
                                    out[1].println("CONTINUE1");
                                    out[0].println("CONTINUE2");
                                    k++;
                                    select = 1;
                                } else if (str[1].equals("EXIT")) {
                                    out[0].println("EXITENEMY");
                                    throw new IOException("Player disconnected");
                                } else {
                                    x = interpreter.getX(str[1]);
                                    y = interpreter.getY(str[1]);
                                    game.putWinBoard(x, y, 1);
                                    out[0].println(str[1]);
                                    
                                }
                            }
                        } else {
                            out[1].println("BOTHPASSED1");
                            out[0].println("BOTHPASSED2");
                            game.resetWinBoard();
                            game.suggestTerritory();
                            int select = 0;
                            while (select == 0) {
                                str[1] = in[1].readLine();
                                if (str[1].equals("CONFIRM")) {
                                    out[0].println("CONFIRM");
                                    select = 1;
                                } else if (str[1].equals("CONTINUE")) {
                                    out[1].println("CONTINUE1");
                                    out[0].println("CONTINUE2");
                                    select = 2;
                                } else if (str[1].equals("EXIT")) {
                                    out[0].println("EXITENEMY");
                                    throw new IOException("Player disconnected");
                                } else {
                                    x = interpreter.getX(str[1]);
                                    y = interpreter.getY(str[1]);
                                    game.putWinBoard(x, y, 1);
                                    out[0].println(str[1]);
                                }
                            }
                            if (select != 2)
                                select = 0;
                            while (select == 0) {
                                str[0] = in[0].readLine();
                                System.out.println(str[1]);
                                if (str[0].equals("CONFIRM")) {
                                    out[1].println("ACCEPT");
                                    str[1] = in[1].readLine();
                                    if (str[1].equals("CONTINUE")) {
                                        out[1].println("CONTINUE1");
                                        out[0].println("CONTINUE2");
                                        select = 2;
                                    } else if (str[0].equals("EXIT")) {
                                        out[1].println("EXITENEMY");
                                        throw new IOException("Player disconnected");
                                    } else {
                                        double bs = game.countBlackScore();
                                        double ws = game.countWhiteScore();
                                        System.out.println("B = "+bs);
                                        System.out.println("W = "+ws);
                                        if (bs > ws) {
                                            out[0].println("EXITWINNER;"+bs+";"+ws);
                                            out[1].println("EXITLOSER;"+bs+";"+ws);
                                        } else if (bs < ws) {
                                            out[1].println("EXITWINNER;"+bs+";"+ws);
                                            out[0].println("EXITLOSER;"+bs+";"+ws);
                                        } else {
                                            out[0].println("EXIT DRAW");
                                            out[1].println("EXIT DRAW");
                                        }
                                        //players[0].close();
                                        //players[1].close();
                                        //System.exit(0);
                                    }
                                } else if (str[0].equals("CONTINUE")) {
                                    out[0].println("CONTINUE1");
                                    out[1].println("CONTINUE2");
                                    k++;
                                    select = 1;
                                } else if (str[0].equals("EXIT")) {
                                    out[1].println("EXITENEMY");
                                    throw new IOException("Player disconnected");
                                } else {
                                    x = interpreter.getX(str[0]);
                                    y = interpreter.getY(str[0]);
                                    game.putWinBoard(x, y, 0);
                                    out[1].println(str[0]);
                                    
                                }
                            }
                        }
                    }
                }
                /** Black turn, check if move is OK. */
                if (str[turn].startsWith("BD:")) {
                    k += black.putStone(x, y, game, out[0], out[1]);
                }
                /** White turn, check if move is OK. */
                if (str[turn].startsWith("WD:")) {
                    k += white.putStone(x, y, game, out[0], out[1]);
                }
                System.out.println("Kolej = " + turn);
                turn = turn + k;
                /** Next turn */
                turn = turn%2;
                }
            } 
            System.out.println("Player disconnected");
        } catch (IOException ex) {
            System.err.println("IO Exception");
        } finally {
            try {
                for (Socket s : players)
                    s.close();
            } catch(IOException ex) {
                System. out.println("Socket still active!");
            }
        }
    }
}