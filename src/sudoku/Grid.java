/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @author Orley
 */
public class Grid {

    public static int size = 9;
    public Field[][] fields = new Field[9][9];

    void init_rand(double fillRate) {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (Math.random() < fillRate) {
                    fields[r][c] = new Field((int) (Math.random() * 9));
                } else {
                    fields[r][c] = new Field(-1);
                }
            }
        }
    }

    void draw(javax.swing.JPanel jPanel1) {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                javax.swing.JButton btn = new javax.swing.JButton();
                btn.setText("" + fields[r][c].value);
                btn.setBounds(35, 35, 35, 35);
                int offsetR=0,offsetC=0;
                if(r%3==0) offsetR += 10;
                if(c%3==0) offsetC += 10;
                else offsetC=0;
                
                btn.move(r * 35 +offsetR, c * 35+offsetC);
                jPanel1.add(btn);
            }
        }
        jPanel1.repaint();
    }

}
