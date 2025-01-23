package chess;

public class PawnMoveCalculator {
}

/*  PAWN: Pawns normally may move forward one square if that square is unoccupied, though if it is the first time that
    pawn is being moved, it may be moved forward 2 squares (provided both squares are unoccupied). Pawns cannot capture
    forward, but instead capture forward diagonally (1 square forward and 1 square sideways). They may only move
    diagonally like this if capturing an enemy piece. When a pawn moves to the end of the board (row 8 for white
    and row 1 for black), they get promoted and are replaced with the player's choice of Rook, Knight, Bishop,
    or Queen (they cannot stay a Pawn or become King). */