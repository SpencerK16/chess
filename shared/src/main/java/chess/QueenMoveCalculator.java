package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMoveCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();

        //Look for Possible moves
        addVerticalAndHorizontalMoves(board, position, moves);
        addDiagonalMoves(board, position, moves);

        return moves;
    }

    private void addVerticalAndHorizontalMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves) {
        int[] directions = {-1, 1};

        // Vertical moves (up and down)
        for (int direction : directions) {
            int row = position.getRow();
            while (true) {
                row += direction;
                if (row < 1 || row > 8) break;
                ChessPosition newPosition = new ChessPosition(row, position.getColumn());
                if (!addMoveIfValid(board, position, newPosition, moves)) break;
            }
        }

        // Horizontal moves (left and right)
        for (int direction : directions) {
            int col = position.getColumn();
            while (true) {
                col += direction;
                if (col < 1 || col > 8) break;
                ChessPosition newPosition = new ChessPosition(position.getRow(), col);
                if (!addMoveIfValid(board, position, newPosition, moves)) break;
            }
        }
    }

    private void addDiagonalMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves) {
        int[] directions = {-1, 1};

        for (int rowDirection : directions) {
            for (int colDirection : directions) {
                int row = position.getRow();
                int col = position.getColumn();
                while (true) {
                    row += rowDirection;
                    col += colDirection;
                    if (row < 1 || row > 8 || col < 1 || col > 8) break;
                    ChessPosition newPosition = new ChessPosition(row, col);
                    if (!addMoveIfValid(board, position, newPosition, moves)) break;
                }
            }
        }
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

/*  QUEEN: Queens are the most powerful piece and may move in straight lines and diagonals as far as there is
    open space. If there is an enemy piece at the end of the line, they may move to that position and capture the
    enemy piece. (In simpler terms, Queens can take all moves a Rook or Bishop could take from the Queen's position). */