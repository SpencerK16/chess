package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoveCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        //Create a pawn variable
        ChessPiece pawn = board.getPiece(position);
        int direction = (pawn.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
        addForwardMoves(board, position, direction, moves);// Add normal forward move
        addCaptureMoves(board, position, direction, moves);// Add diagonal capture moves

        return moves;
    }

    private void addForwardMoves(ChessBoard board, ChessPosition position, int direction, Collection<ChessMove> moves) {
        int startRow = position.getRow();
        int startCol = position.getColumn();

        // One square forward
        int newRow = startRow + direction;
        if (newRow >= 1 && newRow <= 8) {
            ChessPosition newPosition = new ChessPosition(newRow, startCol);
            if (board.getPiece(newPosition) == null) {
                if (isPromotionRow(newRow, direction)) {
                    addPromotionMoves(position, newPosition, moves); // Promotion move
                } else {
                    moves.add(new ChessMove(position, newPosition, null)); // Normal move
                }
                // Two squares forward (only if on the starting row)
                if ((direction == 1 && startRow == 2) || (direction == -1 && startRow == 7)) {
                    newRow = startRow + 2 * direction;
                    newPosition = new ChessPosition(newRow, startCol);
                    if (board.getPiece(newPosition) == null) {
                        moves.add(new ChessMove(position, newPosition, null)); // Double move
                    }
                }
            }
        }
    }

    private void addCaptureMoves(ChessBoard board, ChessPosition position, int direction, Collection<ChessMove> moves) {
        int startRow = position.getRow();
        int startCol = position.getColumn();
        int[] captureDirections = {-1, 1};

        for (int captureDirection : captureDirections) {
            int newRow = startRow + direction;
            int newCol = startCol + captureDirection;
            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece pieceAtEnd = board.getPiece(newPosition);
                if (pieceAtEnd != null && pieceAtEnd.getTeamColor() != board.getPiece(position).getTeamColor()) {
                    if (isPromotionRow(newRow, direction)) {
                        addPromotionMoves(position, newPosition, moves); // Promotion move on capture
                    } else {
                        moves.add(new ChessMove(position, newPosition, null)); // Capture move
                    }
                }
            }
        }
    }

    private boolean isPromotionRow(int row, int direction) {
        return (direction == 1 && row == 8) || (direction == -1 && row == 1);
    }

    private void addPromotionMoves(ChessPosition startPosition, ChessPosition endPosition, Collection<ChessMove> moves) {
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
    }
}

/*  PAWN: Pawns normally may move forward one square if that square is unoccupied, though if it is the first time that
    pawn is being moved, it may be moved forward 2 squares (provided both squares are unoccupied). Pawns cannot capture
    forward, but instead capture forward diagonally (1 square forward and 1 square sideways). They may only move
    diagonally like this if capturing an enemy piece. When a pawn moves to the end of the board (row 8 for white
    and row 1 for black), they get promoted and are replaced with the player's choice of Rook, Knight, Bishop,
    or Queen (they cannot stay a Pawn or become King). */