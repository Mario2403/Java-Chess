package engine.pieces;

import engine.Alliance;
import engine.board.Board;
import engine.board.Move;

import java.util.Collection;

public abstract class Piece {


    protected int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    protected final PieceType pieceType;
    private final int cachedHashCode;


    @Override
    public boolean equals(Object other){
        if(this==other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece= (Piece) other;
        return piecePosition==otherPiece.piecePosition && pieceType == otherPiece.getPieceType() &&
               pieceAlliance==otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove;

    }

    @Override
    public int hashCode(){
        return cachedHashCode;
    }

    public int getPiecePosition() {
        return piecePosition;
    }

    public boolean isFirstMove() {

        return isFirstMove;
    }

    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }

    public PieceType getPieceType() {
        return pieceType;
    }


    public Piece(PieceType pieceType, int piecePosition, Alliance pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;

        this.pieceType = pieceType;
        this.isFirstMove=false;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return  result;
    }

    public abstract Collection<Move> calculateLegalMoves(Board board);

    public abstract Piece movePiece(Move move);

    public enum PieceType{

        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;
        PieceType(final String pieceName){
            this.pieceName=pieceName;
        }

        @Override
        public String toString(){
            return this.pieceName;

        }

        public abstract boolean isKing();

        public abstract boolean isRook();
    }
}
