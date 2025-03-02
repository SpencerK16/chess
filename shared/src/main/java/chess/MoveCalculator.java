package chess;

import java.util.Collection;

public abstract class MoveCalculator {

    protected void addVerticalAndHorizontalMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves) {
        int[] directions = {-1, 1};

        // Vertical moves (up and down)
        for (int direction : directions) {
            int row = position.getRow();
            while (true) {
                row += direction;
                if (row < 1 || row > 8) {
                    break;
                }
                ChessPosition newPosition = new ChessPosition(row, position.getColumn());
                if (!addMoveIfValid(board, position, newPosition, moves)) {
                    break;
                }
            }
        }

        // Horizontal moves (left and right)
        for (int direction : directions) {
            int col = position.getColumn();
            while (true) {
                col += direction;
                if (col < 1 || col > 8) {
                    break;
                }
                ChessPosition newPosition = new ChessPosition(position.getRow(), col);
                if (!addMoveIfValid(board, position, newPosition, moves)) {
                    break;
                }
            }
        }
    }

    protected void addDiagonalMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves) {
        int[] directions = {-1, 1};

        for (int rowDirection : directions) {
            for (int colDirection : directions) {
                int row = position.getRow();
                int col = position.getColumn();
                while (true) {
                    row += rowDirection;
                    col += colDirection;
                    if (row < 1 || row > 8 || col < 1 || col > 8){
                        break;
                    }
                    ChessPosition newPosition = new ChessPosition(row, col);
                    if (!addMoveIfValid(board, position, newPosition, moves)){
                        break;
                    }
                }
            }
        }
    }

    protected boolean addMoveIfValid(ChessBoard board, ChessPosition start, ChessPosition end, Collection<ChessMove> moves) {
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

    protected void addValidMoves(ChessBoard board, ChessPosition start, ChessPosition end, Collection<ChessMove> moves) {
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
