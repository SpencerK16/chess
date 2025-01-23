package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoveCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        //Call the pawn moves finder
        return moves;
    }

    //Make the pawn moves finder

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

/*  PAWN: Pawns normally may move forward one square if that square is unoccupied, though if it is the first time that
    pawn is being moved, it may be moved forward 2 squares (provided both squares are unoccupied). Pawns cannot capture
    forward, but instead capture forward diagonally (1 square forward and 1 square sideways). They may only move
    diagonally like this if capturing an enemy piece. When a pawn moves to the end of the board (row 8 for white
    and row 1 for black), they get promoted and are replaced with the player's choice of Rook, Knight, Bishop,
    or Queen (they cannot stay a Pawn or become King). */