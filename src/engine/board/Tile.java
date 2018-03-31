package engine.board;

import engine.pieces.Piece;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile {

    private int tileCoordinate;
    private static final Map<Integer, EmptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();

    private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles() {

        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i <BoardUtils.NUM_TILES ; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return  emptyTileMap;
    }

    public static Tile createTile(int tileCoordinate, Piece piece){
        return piece != null ? new OcuppiedTile(tileCoordinate, piece) : EMPTY_TILES.get(tileCoordinate);
    }

    public Tile(int position){
        this.tileCoordinate=position;


    }
    public abstract boolean isOcuppied();
    public abstract Piece getPiece();




    public static final class EmptyTile extends Tile {


        public EmptyTile(int position) {
            super(position);
        }

        @Override
        public boolean isOcuppied() {

            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString(){

            return "-";
        }
    }

    public static final class OcuppiedTile extends Tile {

        private Piece pieceOnTile;

        public OcuppiedTile(int position, Piece pieceOnTile) {
            super(position);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isOcuppied() {
            return true;

        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }

        @Override
        public String toString(){
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :
                    getPiece().toString();
        }
    }


}
