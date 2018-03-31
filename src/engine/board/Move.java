package engine.board;

import engine.board.Board.Builder;
import engine.pieces.Pawn;
import engine.pieces.Piece;
import engine.pieces.Rook;

public abstract class Move {

    protected Board board;
    protected Piece movedPiece;
    protected int destinationCoordinate;
    public static final Move NULL_MOVE = new NullMove();


    public Move(Board board, Piece movedPiece, int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result= prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }
    @Override
    public boolean equals(Object other){
        if (this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move)other;
        return getDestinationCoordinate()== otherMove.getDestinationCoordinate() &&
               getMovedPiece()== otherMove.getMovedPiece();
    }

    public int getDestinationCoordinate() {
        return destinationCoordinate;
    }

    public int getCurrentCoordinate(){
        return this.movedPiece.getPiecePosition();
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public boolean isAttack(){
        return false;
    }

    public boolean isCastlingMove(){
        return false;
    }

    public Piece getAttackedPiece(){
        return null;
    }

    public Board execute() {

        final Builder builder = new Builder();
        for (Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    public static final class MajorMove extends Move {

        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            return null;
        }
    }

    public static class AttackMove extends Move {

        private Piece attackedPiece;

        public AttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public boolean isAttack(){
            return true;
        }

        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }

        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(Object other){

            if (this == other){
                return true;
            }
            if(!(other instanceof AttackMove)){
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove)other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());

        }

    }

    public static final class PawnMove extends Move {

        public PawnMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }


    public static class PawnAttackMove extends AttackMove {


        public PawnAttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {


        public PawnEnPassantAttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnJump extends Move {


        public PawnJump(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();

            for (Piece piece : this.board.currentPlayer().getActivePieces()){
                if (!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getAlliance());
            return builder.build();
        }
    }

    static abstract class CastleMove extends Move {

       protected final Rook castledRook;
       protected final int castledRookStart;
       protected final int castledRookDestination;

        public CastleMove(Board board, Piece movedPiece, int destinationCoordinate, Rook castledRook,
                          int castledRookStart, int castledRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castledRook= castledRook;
            this.castledRookDestination=castledRookDestination;
            this.castledRookStart=castledRookStart;
        }

        public Rook getCastledRook() {
            return castledRook;
        }

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public Board execute(){

            final Builder builder = new Builder();
            for (Piece piece : this.board.currentPlayer().getActivePieces()){
                if (!this.movedPiece.equals(piece) && !this.castledRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castledRookDestination, this.castledRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }


    public static final class KingSideCastleMove extends CastleMove {


        public KingSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate, Rook castledRook,
                                  int castledRookStart, int castledRookDestination) {
            super(board, movedPiece, destinationCoordinate, castledRook,
             castledRookStart,  castledRookDestination);
        }

        @Override
        public String toString(){
            return "0-0";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {


        public QueenSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate, Rook castledRook,
                                   int castledRookStart, int castledRookDestination) {
            super(board, movedPiece, destinationCoordinate, castledRook,
                    castledRookStart,  castledRookDestination);
        }

        @Override
        public String toString(){
            return "0-0-0";
        }
    }

    public static final class NullMove extends Move {


        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute(){
            throw new RuntimeException("cannot execute a Null Move");
        }
    }


    public static class MoveFactory{

        private MoveFactory(){
            throw new RuntimeException("Not instantiable");
        }

        public static Move createMove(Board board, int currentCoordinate, int destinationCoordinate ){

            for (Move move : board.getAllLegalMoves()){
                if (move.getCurrentCoordinate()==currentCoordinate &&
                    move.getDestinationCoordinate()==destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

}





