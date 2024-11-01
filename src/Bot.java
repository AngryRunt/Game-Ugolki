import java.util.ArrayList;
import java.util.List;

public class Bot {
private static Referee referee = new Referee();
private List<BotChecker>checkers = new ArrayList<>();
private CornersGame game;


    public Bot(CornersGame game) {
        this.game = game;
    }

    public BotMove GetMove(){
        checkers.clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = game.getBoard().getPiece(i, j);
                if ((piece != null) && piece.color == PieceColor.BLACK) {
                    List<Position>positions = game.getLegalMovesForPieceAt(piece.position);
                    List<LegalMove>moves = new ArrayList<>();
                    for (Position position : positions) {
                        moves.add(new LegalMove(position));
                    }
                    checkers.add(new BotChecker(piece.position, moves ));
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
            for (LegalMove move : checker.getLegalMoves()) {
                if (move.getCost() - penalty > maxcost) {
                    maxcost = move.getCost() - penalty;
                    startposition = checker.getPosition();
                    endposition = move.getMove();

                }
            }
        }
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

    private static class LegalMove {
        private final Position move;
        private final int cost;
        public LegalMove(Position move) {
            this.cost = referee.getCost(move);
            this.move = move;
        }

        public int getCost() {
            return cost;
        }

        public Position getMove() {
            return move;
        }
    }

    private static class BotChecker extends Checker{
        private List<LegalMove> legalMoves = new ArrayList<>();
        private int penalty;
        public BotChecker(Position position, List<LegalMove> moves) {
            super(PieceColor.BLACK, position);
            this.legalMoves = moves;
            this.penalty = referee.getPenalty(position);
        }

        public List<LegalMove> getLegalMoves() {
            return legalMoves;
        }

        public int getPenalty() {
            return penalty;
        }

        public LegalMove getmove(int index) {
            return legalMoves.get(index);
        }

    }

    private static class Referee {
        private int [][] cost = new int[8][8];
        private int [][] penalty = new int[8][8];
        public Referee() {
            int mincost = 800;
            int maxpenalty = 100;
            for (int k = 7; k >= 0; k--){
                for (int i = 7; i >= 7 - k; i--){
                    for (int j = 0; j <= k; j++){
                        cost[i][j] = mincost;
                    }
                }
                mincost+=100;
                for (int i = 7; i >= 7 - k; i--){
                    for (int j = 0; j <= k; j++){
                        penalty[i][j] = maxpenalty;
                    }
                }
                maxpenalty-=10;
            }
        }

        public int getCost(Position position) {
            return cost[position.getRow()][position.getColumn()];
        }

        public int getPenalty(Position position) {
            return penalty[position.getRow()][position.getColumn()];
        }

        public void print() {
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    System.out.print(cost[i][j] + " \t");
                }
                System.out.println();
            }
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
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
