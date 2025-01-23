package chess;

import java.util.Collection;
import java.util.HashSet;


public class KnightMoveCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        //Call the L shape moves finder
        return moves;
    }

    //Make a L shape moves finder

    private boolean addMoveIfValid(ChessBoard board, ChessPosition start, ChessPosition end, Collection<ChessMove> moves) {
        ChessPiece pieceAtEnd = board.getPiece(end);
        if (pieceAtEnd != null) {
            if (pieceAtEnd.getTeamColor() != board.getPiece(start).getTeamColor()) {
                moves.add(new ChessMove(start, end, null)); // Capture move
            }
            return false; // Blocked by a piece
        }
        moves.add(new ChessMove(start, end, null)); // Normal move
        return true;
    }
}

/*  KNIGHT: Knights move in an L shape, moving 2 squares in one direction and 1 square in the other direction.
    Knights are the only piece that can ignore pieces in the in-between squares (they can "jump" over other pieces).
    They can move to squares occupied by an enemy piece and capture the enemy piece, or to unoccupied squares. */