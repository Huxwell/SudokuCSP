/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Orley
 */
public class Grid {

    public static int steps = 0;
    private javax.swing.JPanel jPanel;
    int offsetR = 0, offsetC = 0;
    public static int size = 9;
    public Field[][] fields = new Field[9][9];

    void init(javax.swing.JPanel jPanel) {
        this.jPanel = jPanel;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                fields[r][c] = new Field(c, r, this);
            }
        }
    }

    void fill_rand(double fillRate) {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (Math.random() < fillRate) {
                    //fields[r][c].value = (int) (Math.random() * 9);
                    try_to_fill((int) (Math.random() * 9) + 1, r, c);
                    System.out.println("r=" + r + " c=" + c + " val=" + fields[r][c].value);
                }

            }
        }
    }

    void fill_string(String line) {
        int count = 0;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                fields[r][c].value = line.charAt(count)!='0'?Character.getNumericValue(line.charAt(count)):-1;
                count++;
            }
        }
    }

    void fill_in_order() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                boolean valid = false;
                if (fields[r][c].value == -1) {
                    for (int v = 1; v <= 9 && valid != true; v++) {
                        valid = try_to_fill(v, r, c);
                        System.out.println("IN ORDER r=" + r + " c=" + c + " val=" + fields[r][c].value);
                    }
                }

            }
        }
    }

    boolean try_to_fill(int v, int r, int c) {
        if (!ifRowOk(r, v)) {
            System.out.println("row " + r + " already has value " + v);
            return false;
        }
        if (!ifColOk(c, v)) {
            System.out.println("col " + c + " already has value " + v);
            return false;
        }
        if (!ifSectorOk(r, c, v)) {
            return false;
        }
        fields[r][c].value = v;
        return true;
    }

    public void solve(int row, int col) throws Exception {
        steps++;
        // Throw an exception to stop the process if the puzzle is solved
        if (row > 8) {
            throw new Exception("Solution found");
        }

        // If the cell is not empty, continue with the next cell
        if (fields[row][col].value != -1) {
            next(row, col);
        } else {
            // Find a valid number for the empty cell
            for (int num = 1; num < 10; num++) {
                if (ifRowOk(row, num) && ifColOk(col, num) && ifSectorOk(row, col, num)) {

                    fields[row][col].value = num;
                    System.out.println("r=" + row + " c=" + col + " val=" + fields[row][col].value);
                    //draw(jPanel);
                    //updateView();

                    // Let the observer see it
                    //Thread.sleep(1000);
                    // Delegate work on the next cell to a recursive call
                    next(row, col);
                }
            }
            System.out.println("r=" + row + " c=" + col + " cannot be filled, we go back");
            // No valid number was found, clean up and return to caller
            fields[row][col].value = -1;
            //updateView();
        }
    }

    public void solve(int row, int col, boolean[][][] domain) throws Exception {
        steps++;
        if (row > 8) {
            throw new Exception("Solution found");
        }
        if (fields[row][col].value != -1) {
            next(row, col, domain.clone());
        } else {
            for (int num = 1; num < 10; num++) {
                if (domain[row][col][num] == true && ifSectorOk(row, col, num)) {
                    fields[row][col].value = num;
                    System.out.println("r=" + row + " c=" + col + " val=" + fields[row][col].value);
                    boolean[][][] nDomain = copy3d(domain);
                    cropDomainsAtCol(col, num, nDomain);
                    cropDomainsAtRow(row, num, nDomain);
                    next(row, col, nDomain);
                }
            }
            System.out.println("r=" + row + " c=" + col + " cannot be filled, we go back");
            fields[row][col].value = -1;
        }
    }

    public boolean[][][] copy3d(boolean[][][] oldA) {
        boolean[][][] newA = new boolean[Grid.size][Grid.size][10];
        for (int r = 0; r < Grid.size; r++) {
            for (int c = 0; c < Grid.size; c++) {
                for (int v = 0; v < 10; v++) {
                    newA[r][c][v] = oldA[r][c][v];
                }
            }
        }
        return newA;
    }

    public void next(int row, int col, boolean[][][] domain) throws Exception {
        if (col < 8) {
            solve(row, col + 1, domain);
        } else {
            solve(row + 1, 0, domain);
        }
    }

    public void next(int row, int col) throws Exception {
        if (col < 8) {
            solve(row, col + 1);
        } else {
            solve(row + 1, 0);
        }
    }

    void draw(javax.swing.JPanel jPanel1) {
        jPanel1.removeAll();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                javax.swing.JButton btn = new javax.swing.JButton();
                String text = fields[r][c].value == -1 ? "" : "" + fields[r][c].value;
                btn.setText(text);
                btn.setBounds(50, 50, 50, 50);
                btn.move(r * 50, c * 50);
                jPanel1.add(btn);
            }
        }
        jPanel1.repaint();
        System.out.println("no of steps: " + steps);
    }

    boolean ifRowOk(int r, int v) {
        for (Field f : fields[r]) {
            if (f.value == v) {
                //System.out.println("row " + r + " already has value " + v);
                return false;
            }
        }
        return true;
    }

    boolean ifColOk(int c, int v) {
        for (int r = 0; r < size; r++) {
            if (fields[r][c].value == v) {
                //System.out.println("col " + c + " already has value " + v);
                return false;
            }
        }
        return true;
    }

    protected boolean ifSectorOk(int row, int col, int num) {
        row = (row / 3) * 3;
        col = (col / 3) * 3;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (fields[row + r][col + c].value == num) {
                    return false;
                }
            }
        }
        return true;
    }

    void cropDomainsAtCol(int c, int v, boolean domain[][][]) {
        System.out.println("croped " + v + " at col " + c);
        for (int r = 0; r < Grid.size; r++) {
            domain[r][c][v] = false;
        }
    }

    void cropDomainsAtRow(int r, int v, boolean domain[][][]) {
        System.out.println("croped " + v + " at row " + r);
        for (int c = 0; c < Grid.size; c++) {
            domain[r][c][v] = false;
        }
    }

    Field[] getRow(int r) {
        return fields[r];
    }

    Field[] getCol(int c) {
        Field[] col = new Field[size];
        for (int r = 0; r < size; r++) {
            col[r] = fields[r][c];
        }
        return col;
    }
}
