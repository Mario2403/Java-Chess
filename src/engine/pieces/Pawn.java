package engine.pieces;

import engine.Alliance;
import engine.board.Board;
import engine.board.BoardUtils;
import engine.board.Move;
import engine.board.Move.MajorMove;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Pawn extends Piece {

    private static final int[] CANDIDATE_MOVE_COORDINATES={8, 16, 7, 9};

    public Pawn(int piecePosition, Alliance pieceAlliance) {
        super(PieceType.PAWN, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){

            int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);

            if (!BoardUtils.isValidCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            if (currentCandidateOffset==8 && !board.getTile(candidateDestinationCoordinate).isOcuppied()){

                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));

            }else if(currentCandidateOffset==16 && this.isFirstMove && (BoardUtils.SECOND_ROW[this.piecePosition] &&
                     this.getPieceAlliance().isBlack()) || (BoardUtils.SEVENTH_ROW[this.piecePosition] &&
                     this.getPieceAlliance().isWhite())){

                int behindCandidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * 8);

                if (!board.getTile(behindCandidateDestinationCoordinate).isOcuppied() &&
                    !board.getTile(candidateDestinationCoordinate).isOcuppied()){

                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));

                }

            }else if(currentCandidateOffset== 7 &&
                    !((BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                    (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))){

                if (board.getTile(candidateDestinationCoordinate).isOcuppied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();

                    if (pieceOnCandidate.getPieceAlliance() !=this.pieceAlliance){

                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }

            }else if(currentCandidateOffset==9 &&
                    !((BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                    (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))){

                if (board.getTile(candidateDestinationCoordinate).isOcuppied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();

                    if (pieceOnCandidate.getPieceAlliance() !=this.pieceAlliance){

                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }

            }


        }



        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance() );
    }


    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
}
