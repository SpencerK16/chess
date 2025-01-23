package chess;

import java.util.Collection;


/*
This class represents a single chess piece, with its corresponding type and team color. It contains the PieceType
enumeration that defines the different types of pieces. ChessPiece implements rules that define how a piece moves
independent of other chess rules such as check, stalemate, or checkmate.

pieceMoves: This method is similar to ChessGame.validMoves, except it does not honor whose turn it is or check if
the king is being attacked. This method does account for enemy and friendly pieces blocking movement paths. The
pieceMoves method will need to take into account the type of piece, and the location of other pieces on the board.
 */

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor teamColor;
    private PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        PieceMoveCalculator moves = switch (getPieceType()){
            case QUEEN -> new QueenMoveCalculator();
            case BISHOP  -> new BishopMoveCalculator();
            case KING -> new KingMoveCalculator();
            case ROOK -> new RookMoveCalculator();
            case PAWN -> new PawnMoveCalculator();
            case KNIGHT -> new KnightMoveCalculator();
        };
        return moves.pieceMoves(board, position);
    }
}
