package chess;

import java.util.Collection;
import java.util.List;

public class OfficialPiecesMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> calculateMoves(ChessPiece chessPiece, ChessBoard board, ChessPosition position) {
        return List.of();
    }
}
