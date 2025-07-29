package interfaces;

import pieces.Position;
import types.EState;
import java.awt.image.BufferedImage;

public interface IGraphicsData {

    void reset(EState state, Position to);

    void update();

    boolean isAnimationFinished();

    int getCurrentNumFrame();

    int getTotalFrames();

    double getFramesPerSec();

    boolean isLoop();

    BufferedImage getCurrentFrame();
}
