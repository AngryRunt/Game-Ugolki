public abstract class Piece {
    protected Position position;
    protected PieceColor color;

    public Piece(PieceColor color, Position position) {
        this.color = color;
        this.position = position;
    }

    public PieceColor getColor() {
        return color;
    }


    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Piece) {
            Piece piece = (Piece) obj;
            if (this.position.equals(piece.position)&&this.color.equals(piece.color)) {

                return true;
            }
        }
        return false;
    }

}