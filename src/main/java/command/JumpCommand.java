package command;

import interfaces.IBoard;
import interfaces.ICommand;
import interfaces.IPiece;

public class JumpCommand implements ICommand {

    private final IPiece piece;
    private final IBoard board;

    public JumpCommand(IPiece piece, IBoard board){
        this.piece = piece;
        this.board = board;
    }

  
    @Override
    public void execute() {
        if(!board.isJumpLegal(piece))
            return;
        board.jump(piece);
        // EventPublisher.getInstance()
        //         .publish(GameEvent.PIECE_JUMP,
        //                 new GameEvent(GameEvent.PIECE_JUMP, new ActionData(board.getPlayerOf(Integer.parseInt(piece.getId().split(",")[0])),"piece "+piece+" jumping")));
    }
}
