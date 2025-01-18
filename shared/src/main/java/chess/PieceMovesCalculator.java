package chess;

public interface PieceMovesCalculator {
}
 /* HOW EACH PIECE CAN MOVE:

    KING: Kings may move 1 square in any direction (including diagonal) to either a position
    occupied by an enemy piece (capturing the enemy piece), or to an unoccupied position. A player
    is not allowed to make any move that would allow the opponent to capture their King. If your King
    is in danger of being captured on your turn, you must make a move that removes your King from immediate danger.

    QUEEN: Queens are the most powerful piece and may move in straight lines and diagonals as far as there is
    open space. If there is an enemy piece at the end of the line, they may move to that position and capture the
    enemy piece. (In simpler terms, Queens can take all moves a Rook or Bishop could take from the Queen's position).

    BISHOP: Bishops move in diagonal lines as far as there is open space. If there is an enemy piece at the end of
    the diagonal, the bishop may move to that position and capture the enemy piece.

    KNIGHT: Knights move in an L shape, moving 2 squares in one direction and 1 square in the other direction.
    Knights are the only piece that can ignore pieces in the in-between squares (they can "jump" over other pieces).
    They can move to squares occupied by an enemy piece and capture the enemy piece, or to unoccupied squares.

    ROOK: Rooks may move in straight lines as far as there is open space. If there is an enemy piece at the end of
    the line, rooks may move to that position and capture the enemy piece.

    PAWN: Pawns normally may move forward one square if that square is unoccupied, though if it is the first time that
    pawn is being moved, it may be moved forward 2 squares (provided both squares are unoccupied). Pawns cannot capture
    forward, but instead capture forward diagonally (1 square forward and 1 square sideways). They may only move
    diagonally like this if capturing an enemy piece. When a pawn moves to the end of the board (row 8 for white
    and row 1 for black), they get promoted and are replaced with the player's choice of Rook, Knight, Bishop,
    or Queen (they cannot stay a Pawn or become King).

  */