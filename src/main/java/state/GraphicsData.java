package state;

import interfaces.IGraphicsData;
import pieces.Position;
import types.EState;
import java.awt.image.BufferedImage;

public class GraphicsData implements IGraphicsData {
    private BufferedImage[] frames = null;
    private final int totalFrames;
    private int currentFrame;
    private final double framesPerSec;
    private final boolean isLoop;
    private long lastFrameTimeNanos;
    private final long seconde = 1_000_000_000;

    public GraphicsData(BufferedImage[] frames, double framesPerSec, boolean isLoop) {
        this.frames = frames;
        this.totalFrames = frames.length;
        this.framesPerSec = framesPerSec;
        this.isLoop = isLoop;
        this.currentFrame = 0;
        this.lastFrameTimeNanos = System.nanoTime();
    }

    @Override
    public void reset(EState state, Position to) {
        // Reset only when switching to a new state
        this.currentFrame = 0;
        this.lastFrameTimeNanos = System.nanoTime();
    }

    @Override
    public void update() {
        long now = System.nanoTime();
        double elapsedSec = (now - lastFrameTimeNanos) / seconde;

        if (elapsedSec >= 1.0 / framesPerSec) {
            currentFrame++;
            lastFrameTimeNanos = now;

            if (currentFrame >= totalFrames) {
                currentFrame = isLoop ? 0 : totalFrames - 1;
            }
        }
    }

    @Override
    public boolean isAnimationFinished() {
        return !isLoop && currentFrame >= totalFrames - 1;
    }

    @Override
    public int getCurrentNumFrame() {
        return currentFrame;
    }

    @Override
    public int getTotalFrames() {
        return totalFrames;
    }

    @Override
    public double getFramesPerSec() {
        return framesPerSec;
    }

    @Override
    public boolean isLoop() {
        return isLoop;
    }

    @Override
    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }
}
