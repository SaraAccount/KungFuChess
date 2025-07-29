package interfaces;

import board.BoardConfig;
import pieces.Position;

public interface IBoard {

    void placePiece(IPiece piece);

    boolean hasPiece(int row, int col);

    IPiece getPiece(int row, int col);

    IPiece getPiece(Position pos);

    int getPlayerOf(int row);

    int getPlayerOf(Position pos);

    int getPlayerOf(IPiece piece);

    void move(Position from, Position to);

    void updateAll();

    boolean isInBounds(int r, int c);

    boolean isInBounds(Position p);
    
    boolean isMoveLegal(Position from, Position to);
    
    boolean isPathClear(Position from, Position to);

    boolean isJumpLegal(IPiece p);

    void jump(IPiece p);

    IPlayer[] getPlayers();
    
    int getROWS();

    int getCOLS();

    BoardConfig getBoardConfig();
}
