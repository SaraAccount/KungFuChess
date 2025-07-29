package interfaces;

import pieces.Position;
import types.EState;

public interface IPhysicsData {

    double getSpeedMetersPerSec();

    void setSpeedMetersPerSec(double speedMetersPerSec);

    EState getNextStateWhenFinished();

    void setNextStateWhenFinished(EState nextStateWhenFinished);

    void reset(EState state, Position startPos, Position to, double tileSize, long startTimeNanos);

    void update();

    boolean isMovementFinished();

    double getCurrentX();

    double getCurrentY();
}
