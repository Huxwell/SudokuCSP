/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @authhor Orley
 */
public class Field {
    Grid grid;
    int value;
    public int[] domain = {1,2,3,4,5,6,7,8,9};
    public int col, row;

    public Field(int col, int row, Grid grid) {
        this.value = -1;
        this.col = col;
        this.row = row;
    }

    public int[] computeDomain() {
        return domain;
    }
}
