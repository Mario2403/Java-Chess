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


public class WhitePlayer extends Player{



    public WhitePlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }
    public Player getOpponent(){
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegasl, Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()) {

            if (!this.board.getTile(61).isOcuppied() && !this.board.getTile(62).isOcuppied()) {
                final Tile rookTile = this.board.getTile(63);

                if (rookTile.isOcuppied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(null);
                    }
                }
            }


            if (!this.board.getTile(5).isOcuppied() && !this.board.getTile(6).isOcuppied() &&
                    !this.board.getTile(7).isOcuppied()) {

                final Tile rookTile = this.board.getTile(56);
                if (rookTile.isOcuppied() && rookTile.getPiece().isFirstMove()) {

                    kingCastles.add(null);
                }
            }
        }


        return ImmutableList.copyOf(kingCastles);
    }
}
