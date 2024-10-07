class ChessBoard {
    private Piece[][] board;

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
        board[0][0] = new Checker(PieceColor.BLACK, new Position(0, 0));
    }
    }
        public void movePiece(Position start, Position end) {
            if (board[start.getRow()][start.getColumn()] != null &&
            board[start.getRow()][start.getColumn()].isValidMove(end, board)) {

        board[end.getRow()][end.getColumn()] = board[start.getRow()][start.getColumn()];
        board[end.getRow()][end.getColumn()].setPosition(end);
        board[start.getRow()][start.getColumn()] = null;
    }
}
