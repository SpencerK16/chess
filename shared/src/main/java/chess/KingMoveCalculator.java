package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMoveCalculator extends MoveCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        // Add all possible one-square moves in all directions
        addKingMoves(board, position, moves);
        return moves;
    }

    private void addKingMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves) {
        int[] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colDirections = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < rowDirections.length; i++) {
            int newRow = position.getRow() + rowDirections[i];
            int newCol = position.getColumn() + colDirections[i];
            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                addValidMoves(board, position, newPosition, moves);
            }
        }
    }
}


/*    KING: Kings may move 1 square in any direction (including diagonal) to either a position
    occupied by an enemy piece (capturing the enemy piece), or to an unoccupied position. A player
    is not allowed to make any move that would allow the opponent to capture their King. If your King
    is in danger of being captured on your turn, you must make a move that removes your King from immediate danger.*/
