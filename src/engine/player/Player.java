package engine.player;

import engine.Alliance;
import engine.board.Board;
import engine.board.Move;
import engine.pieces.King;
import engine.pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;




    public Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves){
        this.board=board;
        this.playerKing=establishKing();
        this.legalMoves=legalMoves;
        this.isInCheck=!Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {

    final List<Move> attackMoves = new ArrayList<>();
    for(Move move : moves){
    if(piecePosition== move.getDestinationCoordinate()){
        attackMoves.add(move);
            }

        }
        return Collections.unmodifiableList(attackMoves);
    }

    public King getPlayerKing() {
        return playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return legalMoves;
    }

    private King establishKing(){

        for (Piece piece : getActivePieces()){

                if(piece.getPieceType().isKing()){
                    return (King) piece;
                }
        }
        throw new RuntimeException("Whithout King,  you shouldn't be here");

    }
    //TODO Implement this methods

    public boolean isMoveLegal(Move move){
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck(){
        return this.isInCheck;
    }

    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves();
    }

    public boolean isInStaleMate(){
        return this.isInCheck && !hasEscapeMoves();
    }



    protected boolean hasEscapeMoves(){

        for(Move move : this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()){
                return true;
            }

        }
        return false;


    }



    public boolean isCastled(){
        return false;
    }

    public MoveTransition makeMove(final Move move){
        if(!isMoveLegal(move)){
            return new MoveTransition(this.board, move,  MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks =
                Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());

        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegasl, Collection<Move> opponentsLegals);
}
