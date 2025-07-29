package interfaces;

import pieces.Position;
import types.EPieceType;
import types.EState;
import java.awt.geom.Point2D;
import java.util.Map;

import move.Moves;

public interface IPiece {

    int getPlayer();

    String getId();

    EPieceType getType();

    void setState(EState newStateName);

    IState getCurrentState();

    void update();

    void move(Position to);

    void jump();

    boolean isCaptured();

    void markCaptured();

    int getRow();

    int getCol();

    EState getCurrentStateName();

    Point2D.Double getCurrentPixelPosition();

    Moves getMoves();

    Map<EState, IState> getStates();

    boolean canMoveOver();
}
