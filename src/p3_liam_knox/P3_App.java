/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p3_liam_knox;

import javax.swing.*;

/**
 *
 * @author lknox
 */
public class P3_App extends JFrame{
    P3_App() {//creates the JFrame and sets size, location,name... and adds  JPanel
        super("Minesweeper");
        
        P3_Canvas canvas = new P3_Canvas();
         add(canvas);
        setSize(1400,1035); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
    }
}
