package state;

import interfaces.*;
import pieces.Position;
import types.EState;
import java.awt.*;
import java.awt.geom.Point2D;

public  class State implements IState {

    private EState name = null;
    private IPhysicsData physics = null;
    private IGraphicsData graphics = null;
    private Position startPos;
    private Position targetPos;
    private long startTimeNanos;
    private final double tileSize;


    @SuppressWarnings("OverridableMethodCallInConstructor")
    public State(EState name, Position startPos, Position targetPos,
                 double tileSize, IPhysicsData physics, IGraphicsData graphics) {
        this.name = name;
        this.startPos = startPos;
        this.targetPos = targetPos;
        this.tileSize = tileSize;
        this.physics = physics;
        this.graphics = graphics;
        reset(EState.IDLE, startPos, null);
    }
   
    @Override
    public void reset(EState state, Position from, Position to) {
        if (from != null && to != null) {
            this.startPos = new Position(from.getRow(), from.getCol());
            this.targetPos = new Position(to.getRow(), to.getCol());
        }

        this.startTimeNanos = System.nanoTime();

        if (graphics != null) graphics.reset(state, startPos);
        if (physics != null) physics.reset(state, startPos, targetPos, tileSize, startTimeNanos);
    }

    @Override
    public void update() {
        if (graphics != null) graphics.update();
        if (physics != null) physics.update();

        if (isActionFinished()) {
            startPos = targetPos;
        }
    }

    @Override
    public boolean isActionFinished() {
        switch (name) {
            case EState.MOVE -> {
                return physics.isMovementFinished();
            }
            case EState.JUMP -> {
                boolean finished = graphics != null && graphics.isAnimationFinished();
                if (finished) {
                    System.out.println("Jump animation finished, transitioning to: " + physics.getNextStateWhenFinished());
                }
                return finished;
            }
            case EState.SHORT_REST, EState.LONG_REST -> {
                boolean restFinished = graphics != null && graphics.isAnimationFinished();
                if (restFinished) {
                    System.out.println(name + " animation finished");
                }
                return restFinished;
            }
            default -> {
                if (physics != null)
                    return physics.isMovementFinished();
                else if (graphics != null)
                    return graphics.isAnimationFinished();
                else
                    return true;
            }
        }
    }

    @Override
    public int getStartCol() {
        return startPos.getCol();
    }

    @Override
    public int getStartRow() {
        return startPos.getRow();
    }

    @Override
    public Point2D.Double getCurrentPosition() {
        return new Point2D.Double(physics.getCurrentX(), physics.getCurrentY());
    }

    @Override
    public Point getBoardPosition() {
        return new Point((int) (physics.getCurrentX() / tileSize), (int) (physics.getCurrentY() / tileSize));
    }

    @Override
    public int getCurrentRow() {
        return (int) (physics.getCurrentY() / tileSize);
    }

    @Override
    public int getCurrentCol() {
        return (int) (physics.getCurrentX() / tileSize);
    }

    @Override
    public int getTargetRow() {
        return targetPos.getRow();
    }

    @Override
    public int getTargetCol() {
        return targetPos.getCol();
    }

    @Override
    public IPhysicsData getPhysics() {
        return physics;
    }

    @Override
    public IGraphicsData getGraphics() {
        return graphics;
    }
}
