package interfaces;

import java.util.List;

import pieces.Position;


public interface IPlayer {
    
    public List<IPiece> getPieces();
    
    public int getId();
    
    String getName();

    public IPlayerCursor getCursor();

    public Position getPendingFrom();

    public void setPendingFrom(Position pending);

    public boolean isFailed();

    public void markPieceCaptured(IPiece p);

    public ICommand handleSelection(IBoard board);

    int getScore();
    
    void setScore(int score);

}
