public class ChessBoard {
    private final Piece[][] board;

    public ChessBoard() {
        this.board = new Piece[8][8]; // Chessboard is 8x8
        setupPieces();
    }

    public Piece[][] getBoard() {
        return board;
    }

    public Piece getPiece(int row, int column) {
        return board[row][column];
    }

    public void setPiece(int row, int column, Piece piece) {
        board[row][column] = piece;
        if (piece != null) {
            piece.setPosition(new Position(row, column));
        }
    }

    private void setupPieces() {

        // Place checkers
        for (int i = 0; i < 3; i++) {
            for (int j = 5; j < 8; j++) {
                board[i][j] = new Checker(PieceColor.BLACK, new Position(i, j));
            }
        }
        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = new Checker(PieceColor.WHITE, new Position(i, j));
            }
        }

    }

    public void movePiece(Position start, Position end) {
        if (board[start.getRow()][start.getColumn()] != null) {

            board[end.getRow()][end.getColumn()] = board[start.getRow()][start.getColumn()];
            board[end.getRow()][end.getColumn()].setPosition(end);
            board[start.getRow()][start.getColumn()] = null;
        }
    }
}