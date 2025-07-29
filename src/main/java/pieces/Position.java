package pieces;

public class Position {
    int row;
    int col;

    public Position(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }

    public int getCol() { return col; }

    public int differenceX(Position other){
        return row - other.row;
    }

    public int differenceY(Position other){
        return col - other.col;
    }

    @Override
    public boolean equals(Object obj){
        return obj instanceof Position && ((Position)obj).row== row && ((Position)obj).col == col;
    }

    public Position add(int x, int y){
        return new Position(row + x, col + y);
    }

    @Override
    public String toString() {
        return "row: " + row + ", col: " + col;
    }

    public void reduceOneRow(){
        row--;
    }

    public void reduceOneCol(){
        col--;
    }

    public void addOneRow(){
        row++;
    }

    public void addOneCol(){
        col++;
    }

    public Position copy(){
        return new Position(getRow(), getCol());
    }
}

