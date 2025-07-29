package game;

import board.Board;
import board.BoardConfig;
import interfaces.*;
import java.util.LinkedList;
import java.util.Queue;

public class Game implements IGame {

    private final IPlayer player1;
    private final IPlayer player2;
    private Queue<ICommand> commandQueue = null;
    private final IBoard board;

    
    public Game(BoardConfig bc, IPlayer player1, IPlayer player2) {
        this.board = new Board(bc,new IPlayer[]{ player1, player2 });
        this.player1 = player1;
        this.player2 = player2;
        commandQueue = new LinkedList<>();
    }

    @Override
    public void addCommand(ICommand cmd){
        commandQueue.add(cmd);
    }

    @Override
    public void update() {
        while (!commandQueue.isEmpty()) {
            commandQueue.poll().execute();
        }
    }

    @Override
    public IPlayer getPlayer1() {
        return player1;
    }

    @Override
    public IPlayer getPlayer2() {
        return player2;
    }

    @Override
    public IBoard getBoard() {
        return board;
    }

    @Override
    public void handleSelection(IPlayer player){
        ICommand cmd = player.handleSelection(getBoard());
        if(cmd != null){
            addCommand(cmd);
        }
    }

    @Override
    public IPlayer win(){
        if(board.getPlayers()[0].isFailed())
            return player2;
        if(board.getPlayers()[1].isFailed())
            return player1;
        return null;
    }
}
