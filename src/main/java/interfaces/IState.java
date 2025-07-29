package interfaces;

import pieces.Position;
import types.EState;
import java.awt.*;
import java.awt.geom.Point2D;

public interface IState {

    void reset(EState state, Position from, Position to);

    void update();

    boolean isActionFinished();

    int getStartCol();

    int getStartRow();

    Point2D.Double getCurrentPosition();

    Point getBoardPosition();

    int getCurrentRow();

    int getCurrentCol();

    int getTargetRow();

    int getTargetCol();

    IPhysicsData getPhysics();

    IGraphicsData getGraphics();
}
