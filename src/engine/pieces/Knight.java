package engine.pieces;

import engine.Alliance;
import engine.board.Board;
import engine.board.BoardUtils;
import engine.board.Move;
import engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static engine.board.Move.*;

public class Knight extends Piece {

    private final int[] CANDIDATE_MOVE_COORDINATES={-17, -15, -10, -6, 6, 10, 15, 17};


    public Knight(int piecePosition, Alliance alliance) {
        super(PieceType.KNIGHT, piecePosition, alliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

     final List<Move> legalMoves= new ArrayList<>();

        for (int currentCoordinateOffset: CANDIDATE_MOVE_COORDINATES) {

            int candidateDestinationCoordinate=  this.piecePosition + currentCoordinateOffset;

            if(BoardUtils.isValidCoordinate(candidateDestinationCoordinate)){

                if(isFirstColumnExclusion(this.piecePosition, currentCoordinateOffset) ||
                   isSecondColumnExclusion(this.piecePosition, currentCoordinateOffset) ||
                   isSeventhColumnExclusion(this.piecePosition, currentCoordinateOffset) ||
                   isEighthColumnExclusion( this.piecePosition,currentCoordinateOffset)){

                    continue;
                }

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if(!candidateDestinationTile.isOcuppied()){

                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }else{

                    final Piece pieceAtDestination= candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if (this.pieceAlliance!=pieceAlliance){
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
    return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance() );
    }

    private boolean isFirstColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -17) || (candidateOffset== -10) ||
                (candidateOffset== 6) || (candidateOffset == 15));
    }

    private boolean isSecondColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && ( (candidateOffset==-10) || (candidateOffset==6));
    }

    private boolean isSeventhColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && ((candidateOffset==10) || (candidateOffset==-6));
    }

    private boolean isEighthColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && ((candidateOffset == 17) || (candidateOffset== 10) ||
                (candidateOffset== -6) || (candidateOffset == -15));
    }


    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }

}
