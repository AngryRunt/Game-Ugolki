public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object obj) {
            if (obj instanceof Position) {
            Position p = (Position) obj;
            if (this.row == p.row && this.column == p.column) {
                return true;
            }
        }
        return false;
    }
}
