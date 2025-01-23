package chess;

/*
This class stores all the uncaptured pieces in a Game. It needs to support adding and removing pieces for
testing, as well as a resetBoard() method that sets the standard Chess starting configuration.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private Map<ChessPosition,ChessPiece> board;

    public ChessBoard() {
        board = new HashMap<>();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
       return board.get(position);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Clear my board
        board.clear();

        // Add the powerful pieces
        ChessPiece.PieceType[] pieceOrder = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};
        for (int i = 1; i <= 8; i++) {
            addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, pieceOrder[i-1]));
            addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, pieceOrder[i-1]));
        }

        //Add the pawns to the board
        for (int j = 1; j <= 8; j++){
            addPiece(new ChessPosition(2, j), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, j), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(board);
    }
}
