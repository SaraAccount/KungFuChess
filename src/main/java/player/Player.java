package player;

import java.util.ArrayList;
import java.util.List;

import board.BoardConfig;
import command.JumpCommand;
import command.MoveCommand;
import game.LoadPieces;
import interfaces.IBoard;
import interfaces.ICommand;
import interfaces.IPiece;
import interfaces.IPlayer;
import interfaces.IPlayerCursor;
import pieces.PiecesFactory;
import pieces.Position;
import types.EPieceType;
import utils.LogUtils;


public class Player implements IPlayer{

    private final int id;
    private String name = null;
    private final IPlayerCursor cursor;
    private Position pending;
    private static int countPlayers = 0;
    private final List<IPiece> pieces;
    private int score;
    private boolean isFailed;

    public Player(String name ,IPlayerCursor pc, BoardConfig bc){
        id = countPlayers++;
        this.cursor = pc;
        pending=null;
        isFailed = false;
        this.name = name;
        pieces = new ArrayList<>();
        score = 0;

        for(int i:BoardConfig.rowsOfPlayer.get(id)){
            for(int j = 0; j < 8; j++) {
                IPiece p = PiecesFactory.createPieceByCode(EPieceType.valueOf(LoadPieces.board[i][j].charAt(0) + ""), id, new Position(i, j), bc);
                this.pieces.add(p);
                // score += p.getType().getScore();
            }
        }
    }

    @Override
    public List<IPiece> getPieces() {
        return pieces;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IPlayerCursor getCursor() {
        return cursor;
    }

    @Override
    public Position getPendingFrom() {
        return pending;
    }

    @Override
    public void setPendingFrom(Position pending) {
        this.pending = pending == null? null : pending.copy();
    }

    @Override
    public boolean isFailed(){
        return isFailed;
    }

    @Override
    public void markPieceCaptured(IPiece p){
        p.markCaptured();
        // score -= p.getType().getScore();
        if(p.getType() == EPieceType.K)
            isFailed = true;
    }

    @Override
    public ICommand handleSelection(IBoard board){
        Position previous = getPendingFrom();
        Position selected = getCursor().getPosition();

        if (previous == null) {
            if(board.getPlayerOf(board.getPiece(selected)) != id)
                return null; // invalid 

            if (board.hasPiece(getCursor().getRow(), getCursor().getCol()) &&
                 board.getPiece(getCursor().getPosition()).getCurrentStateName().isCanAction())
                setPendingFrom(selected);
            else {
                System.err.println("can not choose piece");
                LogUtils.logDebug("can not choose piece");
            }
        }
        else {
            setPendingFrom(null);
            if(previous.equals(selected))
                return new JumpCommand(board.getPiece(selected), board);
            return new MoveCommand(previous, selected.copy(), board);
        }

        return null;
    }

    @Override
    public int getScore(){
        return score;
    }

}