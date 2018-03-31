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

public class Rook extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-8, -1, 1, 8};



    public Rook(int piecePosition, Alliance pieceAlliance) {
        super(PieceType.ROOK, piecePosition, pieceAlliance);
    }


    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (int cadidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES) {

            int candidateDestinationCoordinate=this.piecePosition;
            while (BoardUtils.isValidCoordinate(candidateDestinationCoordinate)){

                if (isFirstColumnExclusion(candidateDestinationCoordinate, cadidateCoordinateOffset) ||
                        isEightColumnExclusion(candidateDestinationCoordinate, cadidateCoordinateOffset)){

                    break;
                }
                candidateDestinationCoordinate+=cadidateCoordinateOffset;

                if (BoardUtils.isValidCoordinate(candidateDestinationCoordinate)){
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if(!candidateDestinationTile.isOcuppied()){
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }else{
                        final Piece pieceAtDestination= candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        if (this.pieceAlliance!=pieceAlliance){
                            legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }

        return Collections.unmodifiableList(legalMoves);
    }

    private boolean isFirstColumnExclusion(int currentPosition, int candidatePosition){

        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidatePosition == -1);
    }

    private boolean isEightColumnExclusion(int currentPosition, int candidatePosition){

        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidatePosition == 1);
    }

    @Override
    public Rook movePiece(Move move) {
        return new Rook(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance() );
    }


    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }

}
