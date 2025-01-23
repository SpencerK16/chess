package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMoveCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        addVerticalAndHorizontalMoves(board, position, moves);
        addDiagonalMoves(board, position, moves);
        return moves;
    }

    private void addVerticalAndHorizontalMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves) {

    }

    private void addDiagonalMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves) {

    }

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

/*    KING: Kings may move 1 square in any direction (including diagonal) to either a position
    occupied by an enemy piece (capturing the enemy piece), or to an unoccupied position. A player
    is not allowed to make any move that would allow the opponent to capture their King. If your King
    is in danger of being captured on your turn, you must make a move that removes your King from immediate danger.*/
