package pieces;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import interfaces.IPiece;

public class PieceRenderer {
 
    public static void draw(Graphics graphics, IPiece picece, int squareWidth, int squareHeight) {
        BufferedImage frame = picece.getCurrentState().getGraphics().getCurrentFrame();

        Point2D.Double pos = picece.getCurrentPixelPosition();
        int pixelX = (int) (pos.x * squareWidth / 64.0);
        int pixelY = (int) (pos.y * squareHeight / 64.0);

        graphics.drawImage(frame, pixelX, pixelY, squareWidth, squareHeight, null);
    }

}
