/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p3_liam_knox;

public class P3_Dig extends P3_Canvas{
    public P3_Dig(int[] xAndY) {
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
           
        }
        
    }
    
    
    
    
    
}
