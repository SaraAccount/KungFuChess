package command;

import events.EventPublisher;
import events.GameEvent;
import events.listeners.ActionData;
import interfaces.IBoard;
import interfaces.ICommand;
import pieces.Position;
import utils.LogUtils;

public class MoveCommand implements ICommand {

    private final Position from;
    private final Position to;
    private final IBoard board;

    public MoveCommand(Position from, Position to, IBoard board) {
        this.from = from;
        this.to = to;
        this.board = board;
    }

    @Override
    public void execute() {
        if (!board.isMoveLegal(from, to)) {
            String mes = "Illegal move from " + from + " to " + to;
            EventPublisher.getInstance()
                            .publish(GameEvent.PIECE_MOVED,
                                    new GameEvent(GameEvent.PIECE_MOVED ,new ActionData(board.getPlayerOf(from), mes)));
            LogUtils.logDebug(mes);
            return;
        }
        String mes = "Moving from " + from + " to " + to;
        EventPublisher.getInstance()
                        .publish(GameEvent.PIECE_MOVED,
                                new GameEvent(GameEvent.PIECE_MOVED, new ActionData(board.getPlayerOf(from), mes)));
        LogUtils.logDebug(mes);
        board.move(from, to);
    }
}
