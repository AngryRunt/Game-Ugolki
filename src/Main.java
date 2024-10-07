public class Main {
    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        Piece onepiece = new Piece() {
            @Override
            public boolean isValidMove(Position newPosition, Piece[][] board) {
                return false;
            }
        }


        }
    }
