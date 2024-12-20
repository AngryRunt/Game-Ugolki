import java.util.List;
import java.util.ArrayList;

public class CornersGame {
    private ChessBoard board;
    private boolean whiteTurn = true; // White starts the game
    private Bot bot;
    private String WinString = "Winner is ";

    public String getWinString() {
        return WinString;
    }
    public CornersGame() {
        this.board  =   new ChessBoard();
        this.bot    =   new Bot(this);
    }

    public ChessBoard getBoard() {
        return this.board;
    }

    public void resetGame() {
        this.board = new ChessBoard();
        this.whiteTurn = true;
        counter.ResetCounter();
        this.WinString =   "Winner is ";
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

    boolean makeBotMove() {
        Bot.BotMove botMove = bot.GetMove();
        if (botMove == null) {
            return false;
        }
        return makeMove(botMove.getStartpos(), botMove.getEndpos());
    }

    public boolean makeMove(Position start, Position end) {
        Piece movingPiece = board.getPiece(start.getRow(), start.getColumn());
        if (movingPiece == null || movingPiece.getColor() != (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
            return false;
        }
// проверяем, находится ли end в списке разрешенных ходов
        if (getLegalMovesForPieceAt(start).contains(end)) {
            counter.UpCounter(movingPiece.color);
            if(movingPiece instanceof Checker) {
                ((Checker) movingPiece).AddPrevPosition(end);
            }
            board.movePiece(start, end);
            whiteTurn = !whiteTurn;
            return true;
        }
        return false;
    }


    public boolean isGameOver(PieceColor color) {

        if (counter.getcounter(color) == 27){
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
                    if ((board.getPiece(r, c) != null) && (board.getPiece(r, c).getColor() == color)) {
                        count++;
                    }
                }
            }


           if (count != 0) {
               this.WinString = "Lost game";
               return true;

           }

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


if (count == 9) {
    updateWinnerStringToWin(color);
    return true;

}
return false;
    }

    private void updateWinnerStringToWin(PieceColor color) {
        if (color == PieceColor.WHITE) {
            this.WinString += "white";

        }

        else {
            this.WinString += "black";
        }
    }


    private boolean isPositionOnBoard(Position position) {
        return position.getRow() >= 0 && position.getRow() < board.getBoard().length &&
                position.getColumn() >= 0 && position.getColumn() < board.getBoard()[0].length;
    }

    public List<Position> getLegalMovesForPieceAt(Position position) {
        Piece selectedPiece = board.getPiece(position.getRow(), position.getColumn());
        if (selectedPiece == null)
            return new ArrayList<>();

        // список хранит разрешенные ходы
        // очищается лениво перед новым заполнением (как посуда перед едой =))
        if (legalMoves == null){
            legalMoves = new ArrayList<>();
        }
        legalMoves.clear();

        addSingleMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } }, legalMoves);
        return legalMoves;
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

                    if (whiteTurn) {
                        if ((move[0] == -1 && move[1] == 0) || (move[0] == 0 && move[1] == 1)) {
                            legalMoves.add(newPos);
                        }
                    }
                    else {
                        if ((move[0] == 1 && move[1] == 0) || (move[0] == 0 && move[1] == -1)) {
                            legalMoves.add(newPos);
                        }
                    }
                }
            }
        }
    }

    private void addSingleMoves(Position position, int[][] moves, List<Position> legalMoves) {
        addSingleMoves(position, moves, legalMoves, false);
    }

    private final MoveCounter counter = new MoveCounter();
    private static class MoveCounter{
        private int WhiteCounter = 0;
        private int BlackCounter = 0;
        private MoveCounter(){}
                public int getcounter(PieceColor color) {
                    return switch (color) {
                        case WHITE -> WhiteCounter;
                        case BLACK -> BlackCounter;
                        default -> -1;
                    };
                }

                public void ResetCounter() {
                    WhiteCounter = 0;
                    BlackCounter = 0;
                }

                 public void UpCounter(PieceColor color){
                        switch(color) {
                            case WHITE:
                                WhiteCounter++;
                                break;
                            case BLACK:
                                BlackCounter++;
                                break;
                            }

                    }
             }
}