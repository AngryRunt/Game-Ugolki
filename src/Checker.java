import java.util.ArrayList;
import java.util.List;

public class Checker extends Piece {
    public Checker(PieceColor color, Position position) {
        super(color, position);
    }
    protected List<Position> PrevPosition = new ArrayList<Position>();
    public void AddPrevPosition(Position p) {
        PrevPosition.add(p);
    }
    public boolean isPrevPosition(Position p) {
        return PrevPosition.contains(p);
    }

}