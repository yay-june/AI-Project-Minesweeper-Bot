import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.lang.Math;

public class proj extends JPanel implements KeyListener {

    int xframe = 0;
    int yframe = 0;
    int xbound = 30;
    int ybound = 30;

    int xbombs, ybombs, bombs, bombsflagged;

    boolean loss = false;

    int boardwidth = 10;
    int boardheight = 10;
    int bombcount = 8;

    int brickborderwidth = 2;
    Color brickShade = new Color(60, 60, 60);
    Color brickBorderShade = new Color(90, 90, 90);
    Color brickShadeNumber = new Color(75, 75, 75);
    Color brickShadeBomb = new Color (200, 30, 30);

    int[][] mainboard;
    int[][] playerboard;
    int[][] clearedboard;
    int[][] flagboard;

    public proj(int x, int y) {

        xframe = x;
        yframe = y;

        generateBoard(boardwidth, boardheight, bombcount);
        setFocusable(true);
		setFocusTraversalKeysEnabled(true);
        addKeyListener(this);
    }

    public void generateBoard(int w, int h, int bombCount) {

        bombs = bombCount;

        if (bombs > w * h) {
            bombs = (w * h) - 1;
        }

        int[][] newboard = new int[w][h];
        int[][] bombpos = new int[bombs][2];
        Random random = new Random();
        System.out.println();

        for (int a = 0; a < bombs; a++) {
            bombpos[a][0] = Math.abs(random.nextInt()) % w;
            bombpos[a][1] = Math.abs(random.nextInt()) % h;
            System.out.println("{" + bombpos[a][0] + "," + bombpos[a][1] + "}");
        }

        System.out.println();

        for (int a = 0; a < bombs; a++) {
            boolean dupbomb = false;
            for (int b = 0; b < bombs; b++) {
                if ((bombpos[a][0] == bombpos[b][0] && bombpos[a][1] == bombpos[b][1]) && a != b) {
                    dupbomb = true;
                    System.out.println("Old Coord: {" + bombpos[a][0] + "," + bombpos[a][1] + "}");
                    break;
                }
            }

            while (dupbomb == true) {

                bombpos[a][0] = Math.abs(random.nextInt()) % w;
                bombpos[a][1] = Math.abs(random.nextInt()) % h;
                dupbomb = false;

                System.out.println("New Coord: {" + bombpos[a][0] + "," + bombpos[a][1] + "}");

                for (int b = 0; b < bombs; b++) {
                    if ((bombpos[a][0] == bombpos[b][0] && bombpos[a][1] == bombpos[b][1]) && a != b) {
                        dupbomb = true;
                        System.out.println("Old Coord: {" + bombpos[a][0] + "," + bombpos[a][1] + "}");
                        break;
                    }
                }
            }
        }

        System.out.println("\nNew Coords");

        for (int i = 0; i < bombs; i++) {
            System.out.println("{" + bombpos[i][0] + "," + bombpos[i][1] + "}");
        }

        xbombs = w;
        ybombs = h;

        for (int i = bombs; i < bombs; i++) {
            newboard[bombpos[i][0]][bombpos[i][1]] = 10;
        }

        for (int a = 0; a < h; a++) {
            for (int b = 0; b < w; b++) {
                for (int i = 0; i < bombs; i++) {
                    if (b == bombpos[i][0] && a == bombpos[i][1]) {
                            newboard[b][a] = 9;
                            break;
                    }
                }
            }
        }

        int surroundbombs = 0;

        for (int a = 0; a < h; a++) {
            for (int b = 0; b < w; b++) {
                surroundbombs = 0;
                if (newboard[b][a] != 9) {
                    for (int c = -1; c < 2; c++) {
                        for (int d = -1; d < 2; d++) {
                            if (b + c >= 0 && a + d >= 0 && b + c < w && a + d < h) {
                                if (newboard[b + c][a + d] == 9) {
                                    surroundbombs++;
                                }
                            }
                        }
                    }
                    newboard[b][a] = surroundbombs;
                }
                System.out.print(newboard[b][a] + "   ");
            }
            System.out.print("\n\n");
        }

        mainboard = newboard;
        playerboard = new int[w][h];
        clearedboard = new int[w][h];
        flagboard = new int[w][h];
        bombsflagged = 0;

        loss = false;

        for (int a = 0; a < w; a++) {
            for (int b = 0; b < h; b++) {
                playerboard[a][b] = -1;
            }
        }
    }

    public void makeMove() {
        System.out.println("\nMaking Move...");
        boolean makemove = false;
        int surroundbombs = 0;

        for (int a = 0; a < ybombs; a++) {
            for (int b = 0; b < xbombs; b++) {
                if (clearedboard[b][a] == 0) {
                    surroundbombs = 0;
                    for (int c = -1; c < 2; c++) {
                        for (int d = -1; d < 2; d++) {
                            if (b + c >= 0 && a + d >= 0 && b + c < xbombs && a + d < ybombs) {
                                if (flagboard[b + c][a + d] == 1) {
                                    surroundbombs++;
                                }
                            }
                        }
                    }

                    if (playerboard[b][a] == surroundbombs) {
                        for (int c = -1; c < 2; c++) {
                            for (int d = -1; d < 2; d++) {
                                if (b + c >= 0 && a + d >= 0 && b + c < xbombs && a + d < ybombs) {
                                    if (flagboard[b + c][a + d] == 0) {
                                        playerboard[b + c][a + d] = mainboard[b + c][a + d];
                                    }
                                }
                            }
                        }

                        clearedboard[b][a] = 1;
                        System.out.println("Cleared Square: {" + b + "," + a + "}");
                        makemove = true;
                    }
                }
            }
        }

        boolean makemove2 = false;

        if (!makemove) {
            int surroundnum = 0;
            for (int a = 0; a < ybombs; a++) {
                for (int b = 0; b < xbombs; b++) {
                    if (playerboard[b][a] >= 1 && playerboard[b][a] <= 8) {
                        surroundnum = 0;
                        for (int c = -1; c < 2; c++) {
                            for (int d = -1; d < 2; d++) {
                                if (b + c >= 0 && a + d >= 0 && b + c < xbombs && a + d < ybombs) {
                                    if (!(c == 0 && d == 0)) {
                                        if ((playerboard[b + c][a + d] >= 0 && playerboard[b + c][a + d] <= 8)) {
                                            surroundnum++;
                                        }
                                    }
                                }
                                else {
                                    surroundnum++;
                                }
                            }
                        }

                        System.out.println("Coord {" + b + "," + a + "} has " + surroundnum + " numbers around it");
                        System.out.println("        " + playerboard[b][a] + " | " + (8 - surroundnum));

                        if (playerboard[b][a] == (8 - surroundnum) && clearedboard[b][a] == 0) {
                            for (int c = -1; c < 2; c++) {
                                for (int d = -1; d < 2; d++) {
                                    if (b + c >= 0 && a + d >= 0 && b + c < xbombs && a + d < ybombs) {
                                        if (playerboard[b + c][a + d] == -1) {
                                            if (flagboard[b + c][a + d] == 0) {
                                                flagboard[b + c][a + d] = 1;
                                                bombsflagged++;
                                            }
                                            System.out.println("        Flagged Bomb {" + ( b + c) + "," + (a + d) + "}");
                                            makemove2 = true;
                                        }
                                    }
                                }
                            }
                            clearedboard[b][a] = 1;
                        }
                    }
                }
            }
        }

        if (!makemove2 && !makemove) {
            Random random = new Random();
            int x, y;
            do {
                x = Math.abs(random.nextInt()) % xbombs;
                y = Math.abs(random.nextInt()) % ybombs;
            }
            while(playerboard[x][y] >= 0 || flagboard[x][y] == 1);
            System.out.println("Selecting Random Coordinate: {" + x + "," + y + "}");
            playerboard[x][y] = mainboard[x][y];
        }

        for (int a = 0; a < ybombs; a++) {
            for (int b = 0; b < xbombs; b++) {
                if (playerboard[b][a] == 9) {
                    loss = true;
                }
            }
        }
    }

    public void paint(Graphics g) {
        if (bombsflagged >= bombs) {
            System.out.println(bombsflagged + " | " + bombs);
            g.setColor(new Color(35, 35, 200));
        }
        else if (loss) {
            g.setColor(new Color(200, 35, 35));
        }
        else {
            g.setColor(new Color(35, 35, 35));
        }

        g.fillRect(0, 0, xframe, yframe);
        g.setColor(new Color(0, 255, 0));

        for (int a = 0; a < xbombs; a++) {
            for (int b = 0; b < ybombs; b++) {
                int posA = (a * ((xframe - (xbound * 2)) / xbombs)) + xbound;
                int posB = (b * ((yframe - (ybound * 2)) / ybombs)) + ybound;
                int modA = (((xframe - (xbound * 2)) / xbombs));
                int modB = (((yframe - (ybound * 2)) / ybombs));

                g.setColor(brickBorderShade);
                g.fillRect(posA, posB, modA, modB);

                if (playerboard[a][b] == 0) {
                    g.setColor(brickShadeNumber);
                    g.fillRect(posA + brickborderwidth, posB + brickborderwidth, modA - brickborderwidth, modB - brickborderwidth);
                }
                else if (playerboard[a][b] == 9) {
                    g.setColor(brickShadeBomb);
                    g.fillRect(posA + brickborderwidth, posB + brickborderwidth, modA - brickborderwidth, modB - brickborderwidth);
                }
                else if (playerboard[a][b] >= 0) {
                    g.setColor(brickShadeNumber);
                    g.fillRect(posA + brickborderwidth, posB + brickborderwidth, modA - brickborderwidth, modB - brickborderwidth);
                    g.setColor(new Color(0, 255, 255));
                    g.drawString(Integer.toString(playerboard[a][b]), posA, posB + modB);
                }
                else {
                    g.setColor(brickShade);
                    g.fillRect(posA + brickborderwidth, posB + brickborderwidth, modA - brickborderwidth, modB - brickborderwidth);
                }

                if (flagboard[a][b] == 1) {
                    g.setColor(new Color(255, 0, 0));
                    g.drawString("F", posA, posB + modB);
                }
            }
        }
        repaint();
    }

    @Override
	public void keyTyped(KeyEvent e) {
	}

    @Override
	public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_X) {
			generateBoard(boardwidth, boardheight, bombcount);
		}

        if (e.getKeyCode() == KeyEvent.VK_C) {
            if (bombsflagged < bombs && loss == false) {
                makeMove();
            }
		}
	}

    @Override
	public void keyReleased(KeyEvent e) {
	}
}
