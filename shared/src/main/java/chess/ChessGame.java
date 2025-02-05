package chess;

import java.util.Collection;
import java.util.HashSet;
//I have added the test files, and I'm starting this project
/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamColor;

    public ChessGame() {
        this.board = new ChessBoard();
        this.teamColor = TeamColor.WHITE;
        board.resetBoard();

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null){
            return null;
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();
        for (ChessMove move : moves) {
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(startPosition, null);

            if(!isInCheck(teamColor)) {
                validMoves.add(move);
            }
            //This undoes the piece but I think that it would be a lot better if I were to simply make a copy of the board with the possible moves and throw out what is invalid
            board.addPiece(startPosition, piece);
            board.addPiece(move.getEndPosition(), null);
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (validMoves(move.getStartPosition()).contains(move)) {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
            board.addPiece(move.getStartPosition(), null);
            // Switch turns
            teamColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;  // Updated to use teamColor
        } else {
            throw new InvalidMoveException("Invalid move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Find the king position
        ChessPosition kingPosition = null;
        for (ChessPosition position : board.getPositions()) {
            ChessPiece piece = board.getPiece(position);
            if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                kingPosition = position;
                break;
            }
        }
        // Check if any enemy piece can move to the king's position
        for (ChessPosition position : board.getPositions()) {
            ChessPiece piece = board.getPiece(position);
            if (piece != null && piece.getTeamColor() != teamColor) {
                Collection<ChessMove> moves = piece.pieceMoves(board, position);
                for (ChessMove move : moves) {
                    if (move.getEndPosition().equals(kingPosition)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        for (ChessPosition position : board.getPositions()) {
            ChessPiece piece = board.getPiece(position);
            if (piece != null && piece.getTeamColor() == teamColor) {
                Collection<ChessMove> moves = validMoves(position);
                if (!moves.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        for (ChessPosition position : board.getPositions()) {
            ChessPiece piece = board.getPiece(position);
            if (piece != null && piece.getTeamColor() == teamColor) {
                Collection<ChessMove> moves = validMoves(position);
                if (!moves.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
