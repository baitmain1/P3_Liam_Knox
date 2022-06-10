/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p3_liam_knox;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class P3_Canvas extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener {

    //variables
    Timer timer;
    int flagsPlaced = 0, count = 0, mouseX, mouseY;
    int[][] layout = new int[10][10], bombs = new int[12][2];
    boolean kaboom = false;
    boolean[] edge = new boolean[9];
    boolean[][] reveal = new boolean[10][10], flags = new boolean[10][10];
    Font font = new Font("Arial", Font.BOLD, 70), restartFont = new Font("Arial", Font.BOLD, 40), timerFont = new Font("Arial", Font.BOLD, 25);
    Color num4 = new Color(28, 0, 85), num5 = new Color(146, 18, 0), lightGreen = new Color(196, 251, 124), darkGreen = new Color(115, 194, 1);
    Color[] numColors = {Color.BLUE, Color.GREEN, Color.RED, num4, num5, Color.CYAN, Color.BLACK, Color.GRAY};
    Integer highScoreTime;
    File bestTime;
    Scanner fileScr;
    FileWriter newTime;

    public P3_Canvas() {
        try {//creates a file and scanner for the file sets fastest completion time
            bestTime = new File("P3_BestTime.txt");

            bestTime.createNewFile();

            fileScr = new Scanner(bestTime);

            if (fileScr.hasNext()) {
                highScoreTime = Integer.parseInt(fileScr.nextLine());
                fileScr.close();
            }
        } catch (Exception e) {
        }
        timer = new Timer(1000, this);//sets timer and inputs
        this.addKeyListener(this);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.setFocusable(true);

        setup();//setup resets all necessary components and places bombs and numbers
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        super.paintComponent(g2D);//resets screen

        g2D.setFont(restartFont);//play again and exit "buttons"
        g2D.setColor(Color.GRAY);
        Rectangle2D.Double exitButton = new Rectangle2D.Double(1235, 60, 110, 47);
        g2D.fill(exitButton);

        g2D.setColor(Color.BLACK);
        g2D.drawString("Exit", 1252, 98);

        g2D.setPaint(Color.BLACK);//set parameters for the timer and the highScoreTime
        g2D.setFont(timerFont);
        g2D.drawString("Timer: " + count, 50, 100);

        if (highScoreTime != null) {
            g2D.drawString("Best Time: " + highScoreTime, 20, 150);
        } else {
            g2D.drawString("Best Time: ", 20, 150);
        }

        //light green squares parameters
        int x = 300;
        int y = 0;

        for (int k = 0; k < 2; k++) {//creates green squares in a checker pattern
            for (int j = 0; j < 5; j++) {
                for (int i = 0; i < 5; i++) {
                    if (reveal[(x - 200) / 100][y / 100]) {//colour squares grey if they have been opened
                        g2D.setPaint(Color.LIGHT_GRAY);
                    } else {
                        g2D.setPaint(lightGreen);
                    }

                    Rectangle2D.Double square = new Rectangle2D.Double(x, y, 100, 100);
                    g2D.fill(square);

                    if (flags[(x - 200) / 100][y / 100] == true) {//draw flags if they have beeb placed
                        g2D.setPaint(Color.RED);
                        GeneralPath drawFlag = new GeneralPath();
                        drawFlag.moveTo(x + 40, y + 5);
                        drawFlag.lineTo(x + 80, y + 30);
                        drawFlag.lineTo(x + 40, y + 55);
                        g2D.fill(drawFlag);

                        g2D.setPaint(Color.BLACK);
                        Rectangle2D.Double flagPole = new Rectangle2D.Double(x + 37, y + 5, 3, 80);
                        g2D.fill(flagPole);
                    }

                    if (reveal[(x - 200) / 100][y / 100] && layout[(x - 200) / 100][y / 100] > 0) {//draw numbers on opened squares
                        g2D.setFont(font);
                        g2D.setPaint(numColors[layout[(x - 200) / 100][y / 100] - 1]);
                        g2D.drawString(layout[(x - 200) / 100][y / 100] + "", x + 20, y + 60);
                    }
                    x += 200;
                }
                y += 200;
                x -= 1000;
            }
            x = 200;
            y = 100;

        }

        //darker green square parameters
        x = 200;
        y = 0;

        for (int k = 0; k < 2; k++) {//dark green squares in checker pattern opposite to light green
            for (int j = 0; j < 5; j++) {
                for (int i = 0; i < 5; i++) {

                    if (reveal[(x - 200) / 100][y / 100]) {//grey squares for opened squares
                        g2D.setPaint(Color.LIGHT_GRAY);
                    } else {
                        g2D.setPaint(darkGreen);
                    }

                    Rectangle2D.Double square = new Rectangle2D.Double(x, y, 100, 100);
                    g2D.fill(square);

                    if (flags[(x - 200) / 100][y / 100] == true) {//flag drawing
                        g2D.setPaint(Color.RED);
                        GeneralPath drawFlag = new GeneralPath();
                        drawFlag.moveTo(x + 40, y + 5);
                        drawFlag.lineTo(x + 80, y + 30);
                        drawFlag.lineTo(x + 40, y + 55);
                        g2D.fill(drawFlag);

                        g2D.setPaint(Color.BLACK);
                        Rectangle2D.Double flagPole = new Rectangle2D.Double(x + 37, y + 5, 3, 80);
                        g2D.fill(flagPole);
                    }

                    if (reveal[(x - 200) / 100][y / 100] && layout[(x - 200) / 100][y / 100] > 0) {//number placing
                        g2D.setFont(font);
                        g2D.setPaint(numColors[layout[(x - 200) / 100][y / 100] - 1]);
                        g2D.drawString(layout[(x - 200) / 100][y / 100] + "", x + 20, y + 60);
                    }
                    x += 200;
                }
                y += 200;
                x -= 1000;
            }
            x = 300;
            y = 100;
        }

        int numOpened = 0;//check if all non mines have been opened
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (reveal[j][i] == true) {
                    numOpened++;
                }

            }

        }

        if (kaboom || numOpened == 88) {//if you hit a mine or win the game 
            timer.stop();//stop timer
            if (kaboom) {//draw the bombs
                for (int i = 0; i < 12; i++) {
                    bombs[i][0] = (bombs[i][0] * 100) + 200;
                    bombs[i][1] *= 100;
                    g2D.setPaint(Color.LIGHT_GRAY);
                    Rectangle2D.Double square = new Rectangle2D.Double(bombs[i][0], bombs[i][1], 100, 100);
                    g2D.fill(square);
                    g2D.setPaint(Color.BLACK);
                    Ellipse2D.Double visualBomb = new Ellipse2D.Double(bombs[i][0], bombs[i][1], 100, 100);
                    g2D.fill(visualBomb);

                }
                g2D.setFont(font);//tell user they lost
                g2D.setColor(Color.BLACK);
                g2D.drawString("You Lost!", 500, 100);
            } else {
                if (highScoreTime == null || count < highScoreTime) {//set highScore if necessary
                    highScoreTime = count;

                }

                kaboom = true;//use kaboom variable as a end of game variable
                g2D.setFont(font);//tell user they won
                g2D.setColor(Color.BLACK);
                g2D.drawString("You Won!", 510, 100);

            }

            g2D.setFont(restartFont);//play again and exit "buttons"
            g2D.setColor(Color.GRAY);
            Rectangle2D.Double restartButton = new Rectangle2D.Double(552, 146, 235, 47);
            g2D.fill(restartButton);

            g2D.setColor(Color.BLACK);
            g2D.drawString("Play Again?", 558, 180);

        }

    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (this.mouseX > 199 && this.mouseX < 1201 && kaboom == false) {
            if (e.getKeyCode() == KeyEvent.VK_D) {//if they want to dig
                int x = (this.mouseX - 200) / 100;//get x and y values
                int y = this.mouseY / 100;
                int[] xAndY = {x, y};

                digSquare(xAndY);//dig with those values

            } else if (e.getKeyCode() == KeyEvent.VK_F) {//if they want to set a flag
                int x = (this.mouseX - 200) / 100;
                int y = this.mouseY / 100;
                int[] xAndY = {x, y};

                if (reveal[x][y] == false) {//don't let them flag an opened square
                    flag(xAndY);
                }

            }
        }

    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent e) {//if the game is over
        if (kaboom && this.mouseX > 552 && this.mouseX < 787 && this.mouseY > 146 && this.mouseY < 193) {
            setup();//restart the game
            kaboom = false;
            repaint();
        } else if (this.mouseX > 1235 && this.mouseX < 1345 && this.mouseY > 60 && this.mouseY < 107) {
            try {//set file to best score
                newTime = new FileWriter(bestTime, false);
                newTime.write(highScoreTime + "");
                newTime.close();
            } catch (Exception ex) {
            }
            System.exit(0);//end the program
        }

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {//get x and y coordinates
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    public void digSquare(int[] xAndY) {//dig square x and y, and all around x and y, then use new x and y to dig until digging is surrounded by numbered base layer
        boolean[] edge = new boolean[8];
        if (flags[xAndY[0]][xAndY[1]] == false) {
            reveal[xAndY[0]][xAndY[1]] = true;

            if (layout[xAndY[0]][xAndY[1]] < 0) {
                kaboom = true;
            }

            if (layout[xAndY[0]][xAndY[1]] == 0) {
                for (int i = 0; i < 8; i++) {
                    edge[i] = true;
                }

                if (xAndY[0] > 8) {
                    edge[2] = false;
                    edge[4] = false;
                    edge[7] = false;
                }
                if (xAndY[0] < 1) {
                    edge[0] = false;
                    edge[3] = false;
                    edge[5] = false;
                }
                if (xAndY[1] > 8) {
                    edge[5] = false;
                    edge[6] = false;
                    edge[7] = false;
                }
                if (xAndY[1] < 1) {
                    edge[0] = false;
                    edge[1] = false;
                    edge[2] = false;
                }

                int[] arrayCopy = {xAndY[0], xAndY[1]};

                if (edge[0]) {
                    arrayCopy[0] = xAndY[0] - 1;
                    arrayCopy[1] = xAndY[1] - 1;
                    if (reveal[arrayCopy[0]][arrayCopy[1]] == false) {
                        digSquare(arrayCopy);
                    }
                }

                if (edge[1]) {
                    arrayCopy[1] = xAndY[1] - 1;
                    arrayCopy[0] = xAndY[0];
                    if (reveal[arrayCopy[0]][arrayCopy[1]] == false) {
                        digSquare(arrayCopy);
                    }
                }

                if (edge[2]) {
                    arrayCopy[0] = xAndY[0] + 1;
                    arrayCopy[1] = xAndY[1] - 1;
                    if (reveal[arrayCopy[0]][arrayCopy[1]] == false) {
                        digSquare(arrayCopy);
                    }
                }

                if (edge[3]) {
                    arrayCopy[0] = xAndY[0] - 1;
                    arrayCopy[1] = xAndY[1];
                    if (reveal[arrayCopy[0]][arrayCopy[1]] == false) {
                        digSquare(arrayCopy);
                    }
                }

                if (edge[4]) {
                    arrayCopy[0] = xAndY[0] + 1;
                    arrayCopy[1] = xAndY[1];
                    if (reveal[arrayCopy[0]][arrayCopy[1]] == false) {
                        digSquare(arrayCopy);
                    }
                }

                if (edge[5]) {
                    arrayCopy[0] = xAndY[0] - 1;
                    arrayCopy[1] = xAndY[1] + 1;
                    if (reveal[arrayCopy[0]][arrayCopy[1]] == false) {
                        digSquare(arrayCopy);
                    }
                }

                if (edge[6]) {
                    arrayCopy[1] = xAndY[1] + 1;
                    arrayCopy[0] = xAndY[0];
                    if (reveal[arrayCopy[0]][arrayCopy[1]] == false) {
                        digSquare(arrayCopy);
                    }
                }

                if (edge[7]) {
                    arrayCopy[0] = xAndY[0] + 1;
                    arrayCopy[1] = xAndY[1] + 1;
                    if (reveal[arrayCopy[0]][arrayCopy[1]] == false) {
                        digSquare(arrayCopy);
                    }
                }
            }
            repaint();
        }
    }

    public void flag(int[] xAndY) {//makes a limit to # of flags, and remove a flag if you try to flag a flagged square
        if (flags[xAndY[0]][xAndY[1]] == false) {
            if (flagsPlaced < 12) {
                flagsPlaced++;
                flags[xAndY[0]][xAndY[1]] = true;
            }
        } else {
            flagsPlaced--;
            flags[xAndY[0]][xAndY[1]] = false;
        }
        repaint();
    }

    public void setup() {
        timer.start();
        count = 0;
        flagsPlaced = 0;
        for (int i = 0; i < 10; i++) {//resets necessart variables
            for (int j = 0; j < 10; j++) {
                layout[i][j] = 0;
                reveal[i][j] = false;
                flags[i][j] = false;
            }

        }

        for (int i = 0; i < 12; i++) {//create new random bombs that don't overlap
            do {
                bombs[i][0] = (int) (Math.random() * 10);
                bombs[i][1] = (int) (Math.random() * 10);

                if (layout[bombs[i][0]][bombs[i][1]] < 0) {
                } else {
                    layout[bombs[i][0]][bombs[i][1]] -= 9;
                    break;
                }

            } while (true);

        }

        for (int i = 0; i < 12; i++) {//add 1 to values all around the bombs, but make sure not to add 1 to non existing places

            for (int j = 0; j < 9; j++) {
                edge[j] = true;
            }
            if (bombs[i][0] > 8) {
                edge[2] = false;
                edge[4] = false;
                edge[7] = false;
            }

            if (bombs[i][0] < 1) {
                edge[0] = false;
                edge[3] = false;
                edge[5] = false;
            }

            if (bombs[i][1] < 1) {
                edge[0] = false;
                edge[1] = false;
                edge[2] = false;
            }

            if (bombs[i][1] > 8) {
                edge[5] = false;
                edge[6] = false;
                edge[7] = false;
            }

            if (edge[0]) {
                layout[bombs[i][0] - 1][bombs[i][1] - 1]++;
            }
            if (edge[1]) {
                layout[bombs[i][0]][bombs[i][1] - 1]++;
            }
            if (edge[2]) {
                layout[bombs[i][0] + 1][bombs[i][1] - 1]++;
            }
            if (edge[3]) {
                layout[bombs[i][0] - 1][bombs[i][1]]++;
            }
            if (edge[4]) {
                layout[bombs[i][0] + 1][bombs[i][1]]++;
            }
            if (edge[5]) {
                layout[bombs[i][0] - 1][bombs[i][1] + 1]++;
            }
            if (edge[6]) {
                layout[bombs[i][0]][bombs[i][1] + 1]++;
            }
            if (edge[7]) {
                layout[bombs[i][0] + 1][bombs[i][1] + 1]++;
            }

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.count++;//adds 1 to the timer every second
        repaint();
    }

}
