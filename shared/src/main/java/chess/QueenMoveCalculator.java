package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMoveCalculator extends MoveCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();

        // Use inherited methods to add possible moves
        addVerticalAndHorizontalMoves(board, position, moves);
        addDiagonalMoves(board, position, moves);

        return moves;
    }
}