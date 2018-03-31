package engine.player;

import com.google.common.collect.ImmutableList;
import engine.Alliance;
import engine.board.Board;
import engine.board.Move;
import engine.board.Tile;
import engine.pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player {


    public BlackPlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegasl, Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.isInCheck()){

            if (!this.board.getTile(5).isOcuppied() && !this.board.getTile(6).isOcuppied()){
                final Tile rookTile = this.board.getTile(7);

                if(rookTile.isOcuppied() && rookTile.getPiece().isFirstMove()){
                    if (Player.calculateAttacksOnTile(5, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(null);
                    }
                }
            }


            if (!this.board.getTile(1).isOcuppied() && !this.board.getTile(2).isOcuppied() &&
                    !this.board.getTile(3).isOcuppied()){

                final Tile rookTile = this.board.getTile(0);
                if (rookTile.isOcuppied() && rookTile.getPiece().isFirstMove()){

                        kingCastles.add(null);
                    }
                }
            }


        return ImmutableList.copyOf(kingCastles);
    }



}
