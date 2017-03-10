/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gamepackage;

import java.util.ArrayList;
import java.util.Random;

/** Game class
 *
 * @author v
 */
public class Game {
    private int[][] boardState;
    private int lastX, lastY;
    public int[][] winBoardState;
    private final int boardSize;
    private ArrayList<StoneGroup> whiteChains;
    private ArrayList<StoneGroup> blackChains;
    private int whiteInmates, blackInmates;
    private Stone lastRemoved; 
    private int stat = 0;
    
    public Game(int size){
        /** Set board state empty
         *  states: '-1' empty, '0' black, '1' white
         */
        stat = 0;
        lastRemoved = null;
        boardSize = size;
        whiteChains = new ArrayList();
        blackChains = new ArrayList();
        boardState = new int[boardSize][boardSize];
        winBoardState = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardState[i][j] = -1;
            }
        }     
        whiteInmates = 0;
        blackInmates = 0;
        resetWinBoard();
    }
    /** Check all rules
     * 
     * @param x x coordinate of new move
     * @param y y coordinate of new move
     * @param player player value 0 or 1
     * @return if move is valid
     */
    public boolean checkRules(int x, int y, int player) {
        if (ruleSize(x,y) == false) 
            return false;
        if (rulePlace(x,y) == false) {
            stat = 1;
            return false;
        }
        if (ruleSuicide(x,y,player) == false) {
            System.out.println("Suicide");
            stat = 3;
            return false;
        }
        if (ruleKo(x,y,player) == false) {
            stat = 2;
            return false;
        }
        
        return true;
    }
    
    public String ruleInfo() {
        String info = "OK";
        if (stat == 1)
            return "Bledny ruch";
        if (stat == 2)
            return "Ko";
        if (stat == 3)
            return "Nie rób tego amigo! \nMasz powód by żyć";
        
        return info;
    }
    /** Put new stone on board.
     * 
     * @param x 
     * @param y
     * @param player 
     */
    public void put(int x, int y, int player) {
        lastX = x;
        lastY = y;
        boardState[x][y] = player;
        addToChain(x,y,player);
        if (player == 0) {
            for (int i = 0; i < whiteChains.size(); i++) {
                whiteChains.get(i).updateBreaths();
                if (whiteChains.get(i).getBreaths() == 0) {
                    removeChain(whiteChains.get(i));
                    if(whiteChains.get(i).chain.size() == 1)
                        lastRemoved = whiteChains.get(i).chain.get(0);
                    whiteChains.remove(whiteChains.get(i));
                }
            }
            for (int i = 0; i < blackChains.size(); i++) {
                blackChains.get(i).updateBreaths();
                if (blackChains.get(i).getBreaths() == 0) {
                    removeChain(blackChains.get(i));
                    if(blackChains.get(i).chain.size() == 1)
                        lastRemoved = blackChains.get(i).chain.get(0);
                    blackChains.remove(blackChains.get(i));
                }
            }
        } else {
            for (int i = 0; i < blackChains.size(); i++) {
                blackChains.get(i).updateBreaths();
                if (blackChains.get(i).getBreaths() == 0) {
                    removeChain(blackChains.get(i));
                    if(blackChains.get(i).chain.size() == 1)
                        lastRemoved = blackChains.get(i).chain.get(0);
                    blackChains.remove(blackChains.get(i));
                }
            }
            for (int i = 0; i < whiteChains.size(); i++) {
                whiteChains.get(i).updateBreaths();
                if (whiteChains.get(i).getBreaths() == 0) {
                    removeChain(whiteChains.get(i));
                    if(whiteChains.get(i).chain.size() == 1)
                        lastRemoved = whiteChains.get(i).chain.get(0);
                    whiteChains.remove(whiteChains.get(i));
                }
            }
        }
//        writeBoard();
        System.out.println();
    }
    /** Check if x and y are on board.
     * 
     */
    private boolean ruleSize(int x, int y) {
        if (x > boardSize || y > boardSize) {
            return false;
        }
        return true;
    }
    /** Check if place is empty.
     * 
     */
    private boolean rulePlace(int x, int y) {
        if (boardState[x][y] != -1) {
            return false;
        }
        return true;
    }
    /** Check if you do suicide.
     * 
     */
    private boolean ruleSuicide(int x, int y, int player) {
        StoneGroup found;
        boardState[x][y] = player;
        boolean state = false;
        
        if (countBreaths(x,y) > 0) {
            boardState[x][y] = -1;
            return true;     
        }
        if (x == 0 && y == 0) {
            if (boardState[x+1][y] == player && boardState[x][y+1] == player)
                return true;
            if (boardState[x+1][y] != -1) {
                    if (boardState[x+1][y] == 0)
                        found = findGroup(new Stone(x+1,y,0),0);
                    else
                        found = findGroup(new Stone(x+1,y,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x][y+1] != -1) {
                    if (boardState[x][y+1] == 0)
                        found = findGroup(new Stone(x,y+1,0),0);
                    else
                        found = findGroup(new Stone(x,y+1,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
            } else if (x == 0 && y == boardSize-1) {
                if (boardState[x+1][y] == player && boardState[x][y-1] == player)
                    return true;
                if (boardState[x+1][y] != -1) {
                    if (boardState[x+1][y] == 0)
                        found = findGroup(new Stone(x+1,y,0),0);
                    else
                        found = findGroup(new Stone(x+1,y,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x][y-1] != -1) {
                    if (boardState[x][y-1] == 0)
                        found = findGroup(new Stone(x,y-1,0),0);
                    else
                        found = findGroup(new Stone(x,y-1,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
            } else if (x == boardSize-1 && y == 0) {
                if (boardState[x-1][y] == player && boardState[x][y+1] == player)
                    return true;
                if (boardState[x-1][y] != -1) {
                    if (boardState[x-1][y] == 0)
                        found = findGroup(new Stone(x-1,y,0),0);
                    else
                        found = findGroup(new Stone(x-1,y,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x][y+1] != -1) {
                    if (boardState[x][y+1] == 0)
                        found = findGroup(new Stone(x,y+1,0),0);
                    else
                        found = findGroup(new Stone(x,y+1,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
            } else if (x == boardSize-1 && y == boardSize-1) {
                if (boardState[x-1][y] == player && boardState[x][y-1] == player)
                    return true;
                if (boardState[x-1][y] != -1) {
                    if (boardState[x-1][y] == 0)
                        found = findGroup(new Stone(x-1,y,0),0);
                    else
                        found = findGroup(new Stone(x-1,y,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x][y-1] != -1) {
                    if (boardState[x][y-1] == 0)
                        found = findGroup(new Stone(x,y-1,0),0);
                    else
                        found = findGroup(new Stone(x,y-1,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
            } else if (x == 0) {
                if (boardState[x][y-1] == player && boardState[x][y+1] == player && boardState[x+1][y] == player)
                    return true;
                if (boardState[x+1][y] != -1) {
                    if (boardState[x+1][y] == 0)
                        found = findGroup(new Stone(x+1,y,0),0);
                    else
                        found = findGroup(new Stone(x+1,y,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x][y+1] != -1) {
                    if (boardState[x][y+1] == 0)
                        found = findGroup(new Stone(x,y+1,0),0);
                    else
                        found = findGroup(new Stone(x,y+1,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x][y-1] != -1) {
                    if (boardState[x][y-1] == 0)
                        found = findGroup(new Stone(x,y-1,0),0);
                    else
                        found = findGroup(new Stone(x,y-1,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
            } else if (y == 0) {
                if (boardState[x-1][y] == player && boardState[x][y+1] == player && boardState[x+1][y] == player)
                    return true;
                if (boardState[x+1][y] != -1) {
                    if (boardState[x+1][y] == 0)
                        found = findGroup(new Stone(x+1,y,0),0);
                    else
                        found = findGroup(new Stone(x+1,y,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x-1][y] != -1) {
                    if (boardState[x-1][y] == 0)
                        found = findGroup(new Stone(x-1,y,0),0);
                    else
                        found = findGroup(new Stone(x-1,y,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x][y+1] != -1) {
                    if (boardState[x][y+1] == 0)
                        found = findGroup(new Stone(x,y+1,0),0);
                    else
                        found = findGroup(new Stone(x,y+1,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
            } else if (y == boardSize-1) {
                if (boardState[x-1][y] == player && boardState[x][y-1] == player && boardState[x+1][y] == player)
                    return true;
                if (boardState[x+1][y] != -1) {
                    if (boardState[x+1][y] == 0)
                        found = findGroup(new Stone(x+1,y,0),0);
                    else
                        found = findGroup(new Stone(x+1,y,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x-1][y] != -1) {
                    if (boardState[x-1][y] == 0)
                        found = findGroup(new Stone(x-1,y,0),0);
                    else
                        found = findGroup(new Stone(x-1,y,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x][y-1] != -1) {
                    if (boardState[x][y-1] == 0)
                        found = findGroup(new Stone(x,y-1,0),0);
                    else
                        found = findGroup(new Stone(x,y-1,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                } 
            } else if (x == boardSize-1) {
                if (boardState[x][y-1] == player && boardState[x][y+1] == player && boardState[x-1][y] == player)
                    return true;
                if (boardState[x-1][y] != -1) {
                    if (boardState[x-1][y] == 0)
                        found = findGroup(new Stone(x-1,y,0),0);
                    else
                        found = findGroup(new Stone(x-1,y,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x][y+1] == 0) {
                    if (boardState[x][y+1] == 0)
                        found = findGroup(new Stone(x,y+1,0),0);
                    else
                        found = findGroup(new Stone(x,y+1,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x][y-1] != -1) {
                    if (boardState[x][y-1] == 0)
                        found = findGroup(new Stone(x,y-1,0),0);
                    else
                        found = findGroup(new Stone(x,y-1,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                } 
            } else {
                if (boardState[x][y-1] == player && boardState[x][y+1] == player && boardState[x+1][y] == player && boardState[x-1][y] == player)
                    return true;
                if (boardState[x+1][y] != -1) {
                    if (boardState[x+1][y] == 0)
                        found = findGroup(new Stone(x+1,y,0),0);
                    else
                        found = findGroup(new Stone(x+1,y,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    } else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x-1][y] != -1) {
                    if (boardState[x-1][y] == 0)
                        found = findGroup(new Stone(x-1,y,0),0);
                    else
                        found = findGroup(new Stone(x-1,y,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x][y-1] != -1) {
                    if (boardState[x][y-1] == 0)
                        found = findGroup(new Stone(x,y-1,0),0);
                    else
                        found = findGroup(new Stone(x,y-1,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
                if (boardState[x][y+1] != -1) {
                    if (boardState[x][y+1] == 0)
                        found = findGroup(new Stone(x,y+1,0),0);
                    else
                        found = findGroup(new Stone(x,y+1,1),1);
                    found.updateBreaths();
                    if (found.getBreaths() == 0) {
                        if (found.color != player) {
                            state = true;
                        }
                    }else {
                        if (found.color == player) {
                            state = true;
                        }
                    }
                }
            } 
        boardState[x][y] = -1;
        return state;
    }
    
    public boolean ruleKo(int x, int y, int player) {
        if (lastRemoved == null || countBreaths(x,y) > 0) {
            lastRemoved = null;
            return true;
        }
        if (x == lastRemoved.X && y == lastRemoved.Y)
            if (boardState[lastX][lastY] != -1)
                return false;
        
        return true;
    }
    /** Get actual number of breahts of the chain
     * 
     * @param x
     * @param y
     * @return 
     */
    public int countBreaths(int x, int y) {
        int xbreaths = 2, ybreaths = 2;
        if (x == 0 || x == boardSize-1)
            xbreaths -= 1;
        if (x != 0 && boardState[x-1][y] != -1) 
            xbreaths-= 1;
        if (x != boardSize-1 && boardState[x+1][y] != -1) 
            xbreaths-= 1;
        if (y == 0 || y == boardSize-1)
            ybreaths -= 1;
        if (y != 0 && boardState[x][y-1] != -1) 
            ybreaths-= 1;
        if (y != boardSize-1 && boardState[x][y+1] != -1) 
            ybreaths-= 1;
        
        return xbreaths+ybreaths;
    }
    /** Find group that stone belongs to
     * 
     * @param stone 
     * @param player
     * @return 
     */
    private StoneGroup findGroup(Stone stone, int player) {
        if (player == 0) {
            for (StoneGroup b : blackChains) {
                if (b.checkIfIn(stone) == true)
                    return b;
            }
        } else {
            for (StoneGroup w : whiteChains) {
                if (w.checkIfIn(stone) == true)
                    return w;
            }
        }
        System.out.println("Not found");
        return null;
    }

    /** If there is no black chains, add new one. In other case find closest
     *  group and add a stone or add a stone and connect some groups if possible.
     */
    private void addToChain(int x, int y, int player) {
        Stone stone = new Stone(x,y,player);
        StoneGroup newChain = new StoneGroup(x,y,player,this);
        
        if (player == 0) {
            blackChains.add(newChain);
            
            if (x == 0 && y == 0) {
                if (boardState[x+1][y] == 0) {
                    StoneGroup found = findGroup(new Stone(x+1,y,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
                if (boardState[x][y+1] == 0) {
                    StoneGroup found = findGroup(new Stone(x,y+1,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
            } else if (x == 0 && y == boardSize-1) {
                if (boardState[x+1][y] == 0) {
                    StoneGroup found = findGroup(new Stone(x+1,y,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
                if (boardState[x][y-1] == 0) {
                    StoneGroup found = findGroup(new Stone(x,y-1,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
            } else if (x == boardSize-1 && y == 0) {
                if (boardState[x-1][y] == 0) {
                    StoneGroup found = findGroup(new Stone(x-1,y,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
                if (boardState[x][y+1] == 0) {
                    StoneGroup found = findGroup(new Stone(x,y+1,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
            } else if (x == boardSize-1 && y == boardSize-1) {
                if (boardState[x-1][y] == 0) {
                    StoneGroup found = findGroup(new Stone(x-1,y,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
                if (boardState[x][y-1] == 0) {
                    StoneGroup found = findGroup(new Stone(x,y-1,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
            } else if (x == 0) {
                if (boardState[x+1][y] == 0) {
                    StoneGroup found = findGroup(new Stone(x+1,y,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
                if (boardState[x][y+1] == 0) {
                    StoneGroup found = findGroup(new Stone(x,y+1,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
                if (boardState[x][y-1] == 0) {
                    StoneGroup found = findGroup(new Stone(x,y-1,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
            } else if (y == 0) {
                if (boardState[x+1][y] == 0) {
                    StoneGroup found = findGroup(new Stone(x+1,y,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
                if (boardState[x-1][y] == 0) {
                    StoneGroup found = findGroup(new Stone(x-1,y,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
                if (boardState[x][y+1] == 0) {
                    StoneGroup found = findGroup(new Stone(x,y+1,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
            } else if (y == boardSize-1) {
                if (boardState[x+1][y] == 0) {
                    StoneGroup found = findGroup(new Stone(x+1,y,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
                if (boardState[x-1][y] == 0) {
                    StoneGroup found = findGroup(new Stone(x-1,y,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
                if (boardState[x][y-1] == 0) {
                    StoneGroup found = findGroup(new Stone(x,y-1,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                } 
            } else if (x == boardSize-1) {
                if (boardState[x-1][y] == 0) {
                    StoneGroup found = findGroup(new Stone(x-1,y,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
                if (boardState[x][y+1] == 0) {
                    StoneGroup found = findGroup(new Stone(x,y+1,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
                if (boardState[x][y-1] == 0) {
                    StoneGroup found = findGroup(new Stone(x,y-1,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                } 
            } else {
                if (boardState[x+1][y] == 0) {
                    StoneGroup found = findGroup(new Stone(x+1,y,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);              
                    }
                }
                if (boardState[x-1][y] == 0) {
                    StoneGroup found = findGroup(new Stone(x-1,y,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
                if (boardState[x][y-1] == 0) {
                    StoneGroup found = findGroup(new Stone(x,y-1,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
                if (boardState[x][y+1] == 0) {
                    StoneGroup found = findGroup(new Stone(x,y+1,0),0);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        blackChains.remove(found);
                    }
                }
            }   
        } else {
            whiteChains.add(newChain);
            
            if (x == 0 && y == 0) {
                if (boardState[x+1][y] == 1) {
                    StoneGroup found = findGroup(new Stone(x+1,y,1),1);
                    if (found == newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x][y+1] == 1) {
                    StoneGroup found = findGroup(new Stone(x,y+1,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
            } else if (x == 0 && y == boardSize-1) {
                if (boardState[x+1][y] == 1) {
                    StoneGroup found = findGroup(new Stone(x+1,y,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x][y-1] == 1) {
                    StoneGroup found = findGroup(new Stone(x,y-1,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
            } else if (x == boardSize-1 && y == 0) {
                if (boardState[x-1][y] == 1) {
                    StoneGroup found = findGroup(new Stone(x-1,y,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x][y+1] == 1) {
                    StoneGroup found = findGroup(new Stone(x,y+1,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
            } else if (x == boardSize-1 && y == boardSize-1) {
                if (boardState[x-1][y] == 1) {
                    StoneGroup found = findGroup(new Stone(x-1,y,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x][y-1] == 1) {
                    StoneGroup found = findGroup(new Stone(x,y-1,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
            } else if (x == 0) {
                if (boardState[x+1][y] == 1) {
                    StoneGroup found = findGroup(new Stone(x+1,y,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x][y+1] == 1) {
                    StoneGroup found = findGroup(new Stone(x,y+1,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x][y-1] == 1) {
                    StoneGroup found = findGroup(new Stone(x,y-1,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
            } else if (y == 0) {
                if (boardState[x+1][y] == 1) {
                    StoneGroup found = findGroup(new Stone(x+1,y,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x-1][y] == 1) {
                    StoneGroup found = findGroup(new Stone(x-1,y,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x][y+1] == 1) {
                    StoneGroup found = findGroup(new Stone(x,y+1,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
            } else if (y == boardSize-1) {
                if (boardState[x+1][y] == 1) {
                    StoneGroup found = findGroup(new Stone(x+1,y,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x-1][y] == 1) {
                    StoneGroup found = findGroup(new Stone(x-1,y,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x][y-1] == 1) {
                    StoneGroup found = findGroup(new Stone(x,y-1,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                } 
            } else if (x == boardSize-1) {
                if (boardState[x-1][y] == 1) {
                    StoneGroup found = findGroup(new Stone(x-1,y,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x][y+1] == 1) {
                    StoneGroup found = findGroup(new Stone(x,y+1,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x][y-1] == 1) {
                    StoneGroup found = findGroup(new Stone(x,y-1,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                } 
            } else {
                if (boardState[x+1][y] == 1) {
                    StoneGroup found = findGroup(new Stone(x+1,y,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x-1][y] == 1) {
                    StoneGroup found = findGroup(new Stone(x-1,y,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x][y-1] == 1) {
                    StoneGroup found = findGroup(new Stone(x,y-1,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
                if (boardState[x][y+1] == 1) {
                    StoneGroup found = findGroup(new Stone(x,y+1,1),1);
                    if (found != newChain) {
                        newChain = joinGroup(newChain, found);
                        whiteChains.remove(found);
                    }
                }
            }           
        }
        
    }
    /** Remove all stones belonging to chain from board and count inmates
     * 
     * @param chain 
     */
    private void removeChain(StoneGroup chain) {
        for (int i = 0; i < chain.chain.size(); i++) {
            Stone s = chain.chain.get(i);
            boardState[s.X][s.Y] = -1;
            if (s.color == 0) {
                blackInmates++;
            } else {
                whiteInmates++;
            }
        }
    }
    
    public int getBlackInmates() {
        return blackInmates;
    }
    
    public int getWhiteInmates() {
        return whiteInmates;
    }
    
    private StoneGroup joinGroup(StoneGroup chain, StoneGroup chain1) {
        for (Stone s : chain1.chain) {
            chain.addToGroup(s);
        }
        return chain;
    }
    
    public boolean checkIfExists(int x, int y) {
        if (boardState[x][y] == -1)
            return false;
        return true;
    }
    
    /** Reset winBoard.
     *  
     * 
     */
    public void resetWinBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                winBoardState[i][j] = -1;
            }
        }
    }
    /** Put player on board counting score.
     *  0 - black, 1 - white
     */
    public void putWinBoard(int x, int y, int player) {
        if (winBoardState[x][y] == -1) {
            if (boardState[x][y] == 1)
                winBoardState[x][y] = 0;
            else if (boardState[x][y] == 0)
                winBoardState[x][y] = 1;
            else
                winBoardState[x][y] = player;
        } else {
            winBoardState[x][y] = -1;
        }
        for (int i = 0; i < boardSize; i++) {
            System.out.println();
            for (int j = 0; j < boardSize; j++) {
                System.out.print(winBoardState[i][j] + " | ");
            }
        }  
    }
    /** Count breahts of x,y place on winBoard
     * 
     * @param x
     * @param y
     * @return 
     */
    public int countWinBreaths(int x, int y) {
        int xbreaths = 2, ybreaths = 2;
        if (x == 0 || x == boardSize-1)
            xbreaths -= 1;
        if (x != 0 && (winBoardState[x-1][y] != -1 || boardState[x-1][y] != -1)) 
            xbreaths-= 1;
        if (x != boardSize-1 && (winBoardState[x+1][y] != -1 || boardState[x+1][y] != -1)) 
            xbreaths-= 1;
        if (y == 0 || y == boardSize-1)
            ybreaths -= 1;
        if (y != 0 && (winBoardState[x][y-1] != -1 || boardState[x][y-1] != -1)) 
            ybreaths-= 1;
        if (y != boardSize-1 && (winBoardState[x][y+1] != -1 || boardState[x][y+1] != -1)) 
            ybreaths-= 1;
        
        return xbreaths+ybreaths;
    }
    /** Suggest territory.
     *  First fill place where the stone is with -2 for white and -3 for black.
     *  Then make two suggestions and delete places suggested once.
     * 
     */
    public void suggestTerritory() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (boardState[i][j] != -1) {
                    winBoardState[i][j] = boardState[i][j]-3;
                }
            }
        }
        int c = 0, lp = -1;
        for (int i = 0; i < boardSize; i++) {
            c = 0;
            lp = -1;
            for (int j = 0; j < boardSize; j++) {
                if (winBoardState[i][j] == -3) {
                    for (int k = c; k <= j; k++) {
                        if (boardState[i][k] == -1)
                            winBoardState[i][k] += 5;
                    }
                    c = j;
                    lp = 0;
                } else if (winBoardState[i][j] == -2) {
                    for (int k = c; k <= j; k++) {
                        if (boardState[i][k] == -1)
                            winBoardState[i][k] += 6;
                    }
                    c = j;
                    lp = 1;
                } else if (j == boardSize-1) {
                    if (lp == 0) {
                        for (int k = c; k <= j; k++) {
                            if (boardState[i][k] == -1)
                                winBoardState[i][k] += 5;
                        }
                    } else if (lp == 1) {
                        for (int k = c; k <= j; k++) {
                            if (boardState[i][k] == -1)
                                winBoardState[i][k] += 6;
                        }
                    }
                }
            }
        }
        
        for (int i = 0; i < boardSize; i++) {
            c = 0;
            lp = 0;
            for (int j = 0; j < boardSize; j++) {
                if (winBoardState[j][i] == -3) {
                    for (int k = c; k <= j; k++) {
                        if (boardState[k][i] == -1)
                            winBoardState[k][i] += 5;
                    }
                    c = j;
                    lp = 0;
                } else if (winBoardState[j][i] == -2) {
                    for (int k = c; k <= j; k++) {
                        if (boardState[k][i] == -1)
                            winBoardState[k][i] += 6;
                    }
                    c = j;
                    lp = 1;
                } else if (j == boardSize-1) {
                    if (lp == 0) {
                        for (int k = c; k <= j; k++) {
                            if (boardState[k][i] == -1)
                                winBoardState[k][i] += 5;
                        }
                    } else if (lp == 1) {
                        for (int k = c; k <= j; k++) {
                            if (boardState[k][i] == -1)
                                winBoardState[k][i] += 6;
                        }
                    }
                }
            }
        }
        
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (winBoardState[i][j] == 9 && boardState[i][j] == -1) {
                   winBoardState[i][j] = 0;
                } else if (winBoardState[i][j] == 15 && boardState[i][j] == -1) {
                   winBoardState[i][j] = 0;
                } else if (winBoardState[i][j] == 11 && boardState[i][j] == -1) {
                    winBoardState[i][j] = 1;
                } else if (winBoardState[i][j] == 16 && boardState[i][j] == -1) {
                    winBoardState[i][j] = 1;
                } else if (winBoardState[i][j] == -3) {
                    
                } else if (winBoardState[i][j] == -2) {
                    
                }else {
                    winBoardState[i][j] = -1;
                }
            }
        }
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (countWinBreaths(i,j) > 0) {
                    winBoardState[i][j] = -1;
                }
            }
        }
        int[][] temp = winBoardState;
        while (temp != winBoardState) {
            temp = winBoardState;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (countWinBreaths(i,j) > 0) {
                    winBoardState[i][j] = -1;
                }
            }
        }
        }
           
        System.out.println();
        for (int i = 0; i < boardSize; i++) {
            System.out.println();
            for (int j = 0; j < boardSize; j++) {
                System.out.print(winBoardState[i][j] + " | ");
            }
        }
    }
    
    /** Count 1 values on winBoard
     * 
     */
    public double countWhiteScore() {
        double score = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (winBoardState[i][j] == 1)
                    score++;
            }
        }    
        return score+blackInmates+6.5;
    }
    /** Count 0 values on winBoard
     * 
     */
    public double countBlackScore() {
        double score = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (winBoardState[i][j] == 0)
                    score++;
            }
        }    
        return score+whiteInmates;
    }
    
    private void writeBoard() {
        for (int i = 0; i < boardSize; i++) {
            System.out.println();
            for (int j = 0; j < boardSize; j++) {
                System.out.print(boardState[i][j] + " | ");
            }
        }   
    }
}
