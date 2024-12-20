import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bot {
private static Referee referee = new Referee();
private List<BotChecker>checkers = new ArrayList<>();
private CornersGame game;
private HashMap<Position, Boolean> FixedCheckers = new HashMap<>();


    public Bot(CornersGame game) {
        this.game = game;
        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                this.FixedCheckers.put(new Position(i, j), false);
            }
        }
    }
    private boolean FixOnePosition(Position pos, Position refPos) {

        if ((pos.equals(refPos)) && (FixedCheckers.get(refPos) != null && !FixedCheckers.get(pos))) {
            FixedCheckers.put(pos, Boolean.TRUE);
            return true;
        }
        if (FixedCheckers.get(refPos) != null &&
                !FixedCheckers.get(refPos)) {
            return false;
        }
        return true;
    }
    private boolean FixTwoPosition(Position pos, Position refPos, Position refPos2) {

        if ((pos.equals(refPos)) && (FixedCheckers.get(refPos) != null && !FixedCheckers.get(pos))) {
            FixedCheckers.put(pos, Boolean.TRUE);
            return true;
        }
        if ((pos.equals(refPos2)) && (FixedCheckers.get(refPos2) != null && !FixedCheckers.get(pos))) {
            FixedCheckers.put(pos, Boolean.TRUE);
            return true;
        }
        if ((FixedCheckers.get(refPos) != null && !FixedCheckers.get(refPos)) || (FixedCheckers.get(refPos2) != null && !FixedCheckers.get(refPos2))) {
            return false;
        }
        return true;
    }

    private void FixePosition(Position pos) { // запрещает переставлять шашку внутри дома, если она заняля крайнюю позицию
        //для одной шашки
        Position refPos = new Position(7,0);
        if (!FixOnePosition(pos, refPos)) {
            return;
        }
        // для двух шашек
        refPos = new Position(7,1);
        Position refPos2 = new Position(6,0);
        if (!FixTwoPosition(pos, refPos, refPos2)) {
            return;
        }

        //для одной шашки
        refPos = new Position(6,1);
        if (!FixOnePosition(pos, refPos)) {
            return;
        }
        // для двух шашек
        refPos = new Position(5,0);
        refPos2 = new Position(7,2);
        if (!FixTwoPosition(pos, refPos, refPos2)) {
            return;
        }
        // для двух шашек
        refPos = new Position(5,1);
        refPos2 = new Position(6,2);
        if (!FixTwoPosition(pos, refPos, refPos2)) {
            return;
        }
        //для одной шашки
        refPos = new Position(5,2);
        if (!FixOnePosition(pos, refPos)) {
            return;
        }

    }

    public BotMove GetMove(){ //генерирует ходы для бота
        checkers.clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = game.getBoard().getPiece(i, j);
                if ((piece != null) && piece.color == PieceColor.BLACK) {
                    if ((FixedCheckers.get(piece.getPosition()) != null) &&
                            (FixedCheckers.get(piece.getPosition()))) {
                        continue;

                    }
                    List<Position>positions = game.getLegalMovesForPieceAt(piece.position);
                    if (positions.isEmpty()) {
                        continue;
                    }


                    checkers.add(new BotChecker(piece, new ArrayList<>(positions)));
                }
            }
        }
        Position startposition = null;
        Position endposition = null;
        int maxcost = -10000;
        for (BotChecker checker : checkers) {
            int penalty = 0;
            for (BotChecker C : checkers) {
                if (!checker.equals(C)){
                    penalty += (int) (C.getPenalty() + 0 + (Math.random() * 5));;
                }
            }
            for (Position move : checker.getLegalMoves()) {
                int movep = (int)((referee.getPenalty(move) + Math.random() * 5));
                int p = -(penalty + 0);
                if (referee.getPenalty(checker.getPosition()) == 1024){
                    p = 0;
                }

                if (referee.getPenalty(move) == 0){
                    p = 0;
                }

                if (checker.isPrevPosition(move)){
                    p-=1000;
                }

                if (IsMoveBack(checker.getPosition(), move)){
                    p-=1000;
                }

                if (p > maxcost) {
                    maxcost = p;
                    startposition = checker.getPosition();
                    endposition = move;

                }
            }
        }
        FixePosition(endposition);
        System.out.println(maxcost);
        return new BotMove(startposition, endposition);
    }
    private static boolean IsMoveBack (Position startposition, Position endposition) {
        if (endposition.getRow() < startposition.getRow()) {
            return true;
        }
        if (startposition.getColumn() > endposition.getColumn()) {
            return true;
        }
        return false;
    }

    public static class BotMove { //инкапсулирует для бота ход, состоящий из начального и конечного положения
        private final Position startpos;
        private final Position endpos;
        public BotMove(Position startpos, Position endpos) {
            this.startpos = startpos;
            this.endpos = endpos;
        }

        public Position getStartpos() {
            return startpos;
        }

        public Position getEndpos() {
            return endpos;
        }
    }



    private static class BotChecker extends Checker{ //инкапсулирует в шашку её легальные ходы
        private final List<Position> legalMoves;
        private final int penalty;
        public BotChecker(Piece p, List<Position> moves) {
            super(PieceColor.BLACK, p.getPosition());
            if (p instanceof Checker){
                this.PrevPosition = ((Checker)p).PrevPosition;
            }
            this.legalMoves = moves;
            this.penalty = referee.getPenalty(position);
        }

        public List<Position> getLegalMoves() {
            return legalMoves;
        }

        public int getPenalty() {
            return penalty;
        }

    }

    private static class Referee {
        private int [][] penalty = new int[8][8];
        public Referee() {

            for (int i = 7; i >= 0; i--) {
                for (int j = 0; j <= 7; j++) {
                    penalty[i][j] = (8 - i) * (j + 1) + (int) (0 + (Math.random() * 5));
                }
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 5; j < 8; j++) {
                    penalty[i][j] = 1024;
                }

            }

        }




        public int getPenalty(Position position) {
            return penalty[position.getRow()][position.getColumn()];
        }

        public void print() {

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    System.out.print(penalty[i][j] + "\t");
                }
                System.out.println();
            }

        }

    }

    public static void main(String[] args) {
        Referee referee = new Referee();
        referee.print();
    }
}
