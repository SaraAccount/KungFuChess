package player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import interfaces.IPlayerCursor;
import pieces.Position;

public class PlayerCursor implements IPlayerCursor {
    private Position pos = null;
    private final Color color;
    public final int ROWS = 8;
    public final int COLS = 8;

    public PlayerCursor(Position pos, Color color) {
        this.pos = pos;
        this.color = color;
    }

    @Override
    public void moveUp() {
        if (pos.getRow() > 0) {
            pos.reduceOneRow();
        }         
    }

    @Override
    public void moveDown() {
        if (pos.getRow() < ROWS-1){
            pos.addOneRow();
        }
    }

    @Override
    public void moveLeft() {
        if (pos.getCol() > 0){
            pos.reduceOneCol();
        }
    }

    @Override
    public void moveRight() {
        if (pos.getCol() < COLS-1){
            pos.addOneCol();
        }
    }

    @Override
    public void draw(Graphics g, int panelWidth, int panelHeight) {
        int squareWidth = panelWidth / ROWS;
        int squareHeight = panelHeight / COLS;

        int x = pos.getCol() * squareWidth;
        int y = pos.getRow() * squareHeight;

        Graphics2D g2d = (Graphics2D) g; 

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(5));  // Line width
        g2d.drawRect(x, y, squareWidth, squareHeight);
    }

    @Override
    public int getRow() {
        return pos.getRow();
    }

    @Override
    public int getCol() {
        return pos.getCol();
    }
    
    @Override
    public Position getPosition(){
        return pos;
    }
}
