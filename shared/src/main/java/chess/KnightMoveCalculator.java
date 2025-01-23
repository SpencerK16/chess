package chess;

import java.util.Collection;
import java.util.HashSet;


public class KnightMoveCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        addKnightMoves(board, position, moves);
        return moves;
    }

    private void addKnightMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves){
        int[][] directions = {
                {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
                {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] direction : directions) {
            int newRow = position.getRow() + direction[0];
            int newCol = position.getColumn() + direction[1];
            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                addMoveIfValid(board, position, newPosition, moves);
            }
        }
    }

    private void addMoveIfValid(ChessBoard board, ChessPosition start, ChessPosition end, Collection<ChessMove> moves) {
        ChessPiece pieceAtEnd = board.getPiece(end);
        if (pieceAtEnd != null) {
            if (pieceAtEnd.getTeamColor() != board.getPiece(start).getTeamColor()) {
                moves.add(new ChessMove(start, end, null)); // Capture move
            }
            return; //Blocked by piece
        }
        moves.add(new ChessMove(start, end, null)); // Normal move
    }
}

/*  KNIGHT: Knights move in an L shape, moving 2 squares in one direction and 1 square in the other direction.
    Knights are the only piece that can ignore pieces in the in-between squares (they can "jump" over other pieces).
    They can move to squares occupied by an enemy piece and capture the enemy piece, or to unoccupied squares. */