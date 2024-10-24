import java.util.List;
import java.util.ArrayList;

public class ChessGame {
    private ChessBoard board;
    private boolean whiteTurn = true; // White starts the game

    public ChessGame() {
        this.board = new ChessBoard();
    }

    public ChessBoard getBoard() {
        return this.board;
    }

    public void resetGame() {
        this.board = new ChessBoard();
        this.whiteTurn = true;
        counter.ResetCounter();
    }

    public PieceColor getCurrentPlayerColor() {
        return whiteTurn ? PieceColor.WHITE : PieceColor.BLACK;
    }

    private Position selectedPosition;

    public boolean isPieceSelected() {
        return selectedPosition != null;
    }

    public boolean handleSquareSelection(int row, int col) {
        if (selectedPosition == null) {
            Piece selectedPiece = board.getPiece(row, col);
            if (selectedPiece != null
                    && selectedPiece.getColor() == (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
                selectedPosition = new Position(row, col);
                return false;
            }
        } else {
            boolean moveMade = makeMove(selectedPosition, new Position(row, col));
            selectedPosition = null;
            return moveMade;
        }
        return false;
    }

    public boolean makeMove(Position start, Position end) {
        Piece movingPiece = board.getPiece(start.getRow(), start.getColumn());
        if (movingPiece == null || movingPiece.getColor() != (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
            return false;
        }
// проверяем, находится ли end в списке разрешенных ходов
        if (legalMoves.contains(end)) {
            counter.UpCounter(movingPiece.color);
            board.movePiece(start, end);
            whiteTurn = !whiteTurn;
            return true;
        }
        return false;
    }

    public boolean isInCheck(PieceColor kingColor) {
        Position kingPosition = findKingPosition(kingColor);
        for (int row = 0; row < board.getBoard().length; row++) {
            for (int col = 0; col < board.getBoard()[row].length; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor() != kingColor) {
                    if (piece.isValidMove(kingPosition, board.getBoard())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Position findKingPosition(PieceColor color) {
        for (int row = 0; row < board.getBoard().length; row++) {
            for (int col = 0; col < board.getBoard()[row].length; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece instanceof King && piece.getColor() == color) {
                    return new Position(row, col);
                }
            }
        }
        throw new RuntimeException("King not found, which should never happen.");
    }

    public boolean isCheckmate(PieceColor color) {
        System.out.println("WHITEMOVE" + String.valueOf(counter.getcounter(PieceColor.WHITE)));
        System.out.println("BLACKEMOVE" + String.valueOf(counter.getcounter(PieceColor.BLACK)));
        if (counter.getcounter(color) == 12){
            int i = 0, j = 0;

            if (color == PieceColor.WHITE) {
                i = 5;
                j = 0;

            }
            else{
                i = 0;
                j = 5;
            }
            int endI = i + 3, endJ = j + 3;
            int count = 0;
            for (int r = i; r < endI; r++) {
                for (int c = j; c < endJ; c++) {
                    if (board.getPiece(r, c) != null) {
                        count++;
                    }
                }
            }

            return count == 0 ? true : false;
        }

        int i = 0, j = 0;

        if (color == PieceColor.WHITE) {
           i = 0;
           j = 5;

        }
        else{
            i = 5;
            j = 0;
        }
        int endI = i + 3, endJ = j + 3;
        int count = 0;
        for (int r = i; r < endI; r++) {
            for (int c = j; c < endJ; c++) {
                if ((board.getPiece(r, c) != null) &&
                        (board.getPiece(r, c).getColor() == color)) {
                    count++;
                }
            }
        }

        /*if (!isInCheck(kingColor)) {
            return false;
        }

        Position kingPosition = findKingPosition(kingColor);
        King king = (King) board.getPiece(kingPosition.getRow(), kingPosition.getColumn());

        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {
                if (rowOffset == 0 && colOffset == 0) {
                    continue;
                }
                Position newPosition = new Position(kingPosition.getRow() + rowOffset,
                        kingPosition.getColumn() + colOffset);

                if (isPositionOnBoard(newPosition) && king.isValidMove(newPosition, board.getBoard())
                        && !wouldBeInCheckAfterMove(kingColor, kingPosition, newPosition)) {
                    return false;
                }
            }
        }*/

        return count == 9 ? true : false;
    }

    private boolean isPositionOnBoard(Position position) {
        return position.getRow() >= 0 && position.getRow() < board.getBoard().length &&
                position.getColumn() >= 0 && position.getColumn() < board.getBoard()[0].length;
    }

    private boolean wouldBeInCheckAfterMove(PieceColor kingColor, Position from, Position to) {
        Piece temp = board.getPiece(to.getRow(), to.getColumn());
        board.setPiece(to.getRow(), to.getColumn(), board.getPiece(from.getRow(), from.getColumn()));
        board.setPiece(from.getRow(), from.getColumn(), null);

        boolean inCheck = isInCheck(kingColor);

        board.setPiece(from.getRow(), from.getColumn(), board.getPiece(to.getRow(), to.getColumn()));
        board.setPiece(to.getRow(), to.getColumn(), temp);

        return inCheck;
    }

    public List<Position> getLegalMovesForPieceAt(Position position) {
        Piece selectedPiece = board.getPiece(position.getRow(), position.getColumn());
        if (selectedPiece == null)
            return new ArrayList<>();

                /*switch (selectedPiece.getClass().getSimpleName()) {
            case "Pawn":
                addPawnMoves(position, selectedPiece.getColor(), legalMoves);
                break;
            case "Rook":
                addLineMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } }, legalMoves);
                break;
            case "Knight":
                addSingleMoves(position, new int[][] { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, { 1, 2 }, { -1, 2 },
                        { 1, -2 }, { -1, -2 } }, legalMoves);
                break;
            case "Bishop":
                addLineMoves(position, new int[][] { { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } }, legalMoves);
                break;
            case "Queen":
                addLineMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { -1, -1 },
                        { 1, -1 }, { -1, 1 } }, legalMoves);
                break;
            case "King":
                addSingleMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { -1, -1 },
                        { 1, -1 }, { -1, 1 } }, legalMoves);
                break;
        }*/


// список хранит разрешенные ходы
        // очищается лениво перед новым заполнением (как посуда перед едой =))
        if (legalMoves == null){
            legalMoves = new ArrayList<>();
        }
        legalMoves.clear();

        addSingleMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } }, legalMoves);
        return legalMoves;
    }

    private void addLineMoves(Position position, int[][] directions, List<Position> legalMoves) {
        for (int[] d : directions) {
            Position newPos = new Position(position.getRow() + d[0], position.getColumn() + d[1]);
            while (isPositionOnBoard(newPos)) {
                if (board.getPiece(newPos.getRow(), newPos.getColumn()) == null) {
                    legalMoves.add(new Position(newPos.getRow(), newPos.getColumn()));
                    newPos = new Position(newPos.getRow() + d[0], newPos.getColumn() + d[1]);
                } else {
                    if (board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != board
                            .getPiece(position.getRow(), position.getColumn()).getColor()) {
                        legalMoves.add(newPos);
                    }
                    break;
                }
            }
        }
    }

    private List<Position> legalMoves = null;
    private void addSingleMoves(Position position, int[][] moves, List<Position> legalMoves, final boolean isRecursiveCall) {
        //перебираем все смещения
        for (int[] move : moves) {
            // получаем новую позицию по смещении
            Position newPos = new Position(position.getRow() + move[0], position.getColumn() + move[1]);
            // проверяем, находится ли позиция на доске
            if (isPositionOnBoard(newPos)) {
                // проверяем, есть ли рядом шашка
                if (board.getPiece(newPos.getRow(), newPos.getColumn()) != null) {
                    // получаем позицию через шашку
                    newPos = new Position(position.getRow() + move[0] * 2, position.getColumn() + move[1] * 2);
                    // проверяем, что позиция свободна и находится на доске
                    if (isPositionOnBoard(newPos) && (board.getPiece(newPos.getRow(), newPos.getColumn()) == null)) {

                         /*
                         * если рядом с новой позицией есть шашка, то мы можем прыгнуть и через нее
                         * тогда необходимо рекурсивно получить все возможные прыжки через одну
                         * для этого проверяем, что мы не вернудись в предыдущую позицию
                         */
                        if (!legalMoves.contains(newPos)) {
                            // добавляем новую позицию в список разрешенных
                            legalMoves.add(newPos);
                            // проверяем врзможность нового прыжка вокруг новой позиции
                            addSingleMoves(newPos, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } }, legalMoves, true);
                        }
                    }
                    //если вызов был рекурсивным, в список разрещеннвх могут попасть все клетки вокруг
                    //при том, что после прыжка разрешен только прыжок
                    // поэтому проверяем, что функция была вызвана не рекурсивно
                } else if (!isRecursiveCall) {
                    legalMoves.add(newPos);
                }
            }
        }
    }

    private void addSingleMoves(Position position, int[][] moves, List<Position> legalMoves) {
        addSingleMoves(position, moves, legalMoves, false);
    }


    private void addPawnMoves(Position position, PieceColor color, List<Position> legalMoves) {
        int direction = color == PieceColor.WHITE ? -1 : 1;
        Position newPos = new Position(position.getRow() + direction, position.getColumn());
        if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) == null) {
            legalMoves.add(newPos);
        }

        if ((color == PieceColor.WHITE && position.getRow() == 6)
                || (color == PieceColor.BLACK && position.getRow() == 1)) {
            newPos = new Position(position.getRow() + 2 * direction, position.getColumn());
            Position intermediatePos = new Position(position.getRow() + direction, position.getColumn());
            if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) == null
                    && board.getPiece(intermediatePos.getRow(), intermediatePos.getColumn()) == null) {
                legalMoves.add(newPos);
            }
        }

        int[] captureCols = { position.getColumn() - 1, position.getColumn() + 1 };
        for (int col : captureCols) {
            newPos = new Position(position.getRow() + direction, col);
            if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) != null &&
                    board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != color) {
                legalMoves.add(newPos);
            }
        }
    }
    private MoveCounter counter = new MoveCounter();
    private static class MoveCounter{
        private int WhiteCounter = 0;
        private int BlackCounter = 0;
        private MoveCounter(){};
                public int getcounter(PieceColor color) {
                    switch (color) {
                        case WHITE:
                            return WhiteCounter;

                        case BLACK:
                            return BlackCounter;
                        default:
                            return -1;
                    }
                }
                public void ResetCounter(){
                    WhiteCounter = 0;
                    BlackCounter = 0;
                }
                 public void UpCounter(PieceColor color){
                        switch(color){
                            case WHITE:
                                WhiteCounter++;
                                break;
                            case BLACK:
                                BlackCounter++;
                                break;
                            default: return;
                        }
                    }
             }
}