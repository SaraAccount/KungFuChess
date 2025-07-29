package interfaces;

import java.awt.Graphics;

import pieces.Position;
public interface IPlayerCursor {
 
    void moveUp();

    void moveDown();

    void moveLeft();

    void moveRight();

    void draw(Graphics g, int panelWidth, int panelHeight);

    int getRow();

    int getCol();

    Position getPosition();
}
