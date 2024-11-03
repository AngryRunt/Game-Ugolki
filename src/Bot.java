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

    private void FixePosition(Position pos) {
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

    public BotMove GetMove(){
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


                    checkers.add(new BotChecker(piece.position, new ArrayList<>(positions)));
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
                    penalty += C.getPenalty();
                }
            }
            for (Position move : checker.getLegalMoves()) {
                int p = -(penalty /*+ referee.getPenalty(move)*/);
                if (p > maxcost) {
                    maxcost = p;
                    startposition = checker.getPosition();
                    endposition = move;

                }
            }
        }
        FixePosition(endposition);
        System.out.println("Move penalty: " + String.valueOf(maxcost));
        return new BotMove(startposition, endposition);
    }

    public static class BotMove {
        private Position startpos;
        private Position endpos;
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



    private static class BotChecker extends Checker{
        private List<Position> legalMoves = new ArrayList<>();
        private int penalty;
        public BotChecker(Position position, List<Position> moves) {
            super(PieceColor.BLACK, position);
            this.legalMoves = moves;
            this.penalty = referee.getPenalty(position);
        }

        public List<Position> getLegalMoves() {
            return legalMoves;
        }

        public int getPenalty() {
            return penalty;
        }

        public Position getmove(int index) {
            return legalMoves.get(index);
        }

    }

    private static class Referee {
        private int [][] cost = new int[8][8];
        private int [][] penalty = new int[8][8];
        public Referee() {
            int mincost = 200;
            int maxpenalty = 100;
            for (int k = 7; k >= 0; k--) {
                for (int i = 7; i >= 7 - k; i--) {
                    for (int j = 0; j <= k; j++) {
                        cost[i][j] = mincost;
                    }
                }
                mincost+=100;

                maxpenalty-=10;
            }
            for (int i = 7; i >= 0; i--) {
                for (int j = 0; j <= 7; j++) {
                    penalty[i][j] = (8 - i) * (j + 1 );
                }
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 5; j < 8; j++) {
                    penalty[i][j] = 1024;
                }

            }
        }

        public int getCost(Position position) {
            return cost[position.getRow()][position.getColumn()];
        }

        public int getPenalty(Position position) {
            return penalty[position.getRow()][position.getColumn()];
        }

        public void print() {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    System.out.print(cost[i][j] + " \t");
                }
                System.out.println();
            }
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
