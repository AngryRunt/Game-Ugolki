import java.util.ArrayList;
import java.util.List;

public class Bot {
    private static class Move{
        private final Position move;
        private final int cost;

        public Move(Position move, int cost) {
            this.cost = cost;
            this.move = move;
        }
    }
    private static class BotChecker extends Checker{
        private List<Move> moves = new ArrayList<>();


        public BotChecker(Position position, List<Move> moves){
            super(PieceColor.BLACK, position);
            this.moves = moves;
        }

    }
    private static class Referee{
        private int [][] cost = new int[8][8];
        private int [][] penalty = new int[8][8];
        public Referee(){
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
        public int getCost(Position position){
            return cost[position.getRow()][position.getColumn()];
        }
        public int getPenalty(Position position){
            return penalty[position.getRow()][position.getColumn()];
        }
        public void print(){
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
