package board;

import java.util.List;
import events.EventPublisher;
import events.GameEvent;
import events.listeners.ActionData;
import interfaces.IBoard;
import interfaces.IPiece;
import interfaces.IPlayer;
import move.Moves;
import pieces.Position;
import types.EState;
import utils.LogUtils;


public class Board implements IBoard {

    private final IPiece[][] boardGrid;
    public final IPlayer[] players;
    public final BoardConfig boardConfig;
    
    public Board(BoardConfig bc, IPlayer[] players) {
        boardConfig = bc;
        this.boardGrid = new IPiece[bc.numRowsCols.getX()][bc.numRowsCols.getY()];
        this.players = players;

        for (IPlayer p : players)
            for (IPiece piece : p.getPieces()) {
                String[] pos = piece.getId().split(",");
                boardGrid[Integer.parseInt(pos[0])][Integer.parseInt(pos[1])] = piece;
            }
    }

    @Override
    public void placePiece(IPiece piece) {
        int row = piece.getRow();
        int col = piece.getCol();
        if (isInBounds(row, col)) {
            boardGrid[row][col] = piece;
        } else {
            throw new IllegalArgumentException("Invalid position row=" + row + ", col=" + col);
        }
    }

    @Override
    public boolean hasPiece(int row, int col) {
        return isInBounds(row, col) && boardGrid[row][col] != null;
    }

    @Override
    public IPiece getPiece(int row, int col) {
        if (!isInBounds(row, col))
            return null;
        return boardGrid[row][col];
    }

    @Override
    public IPiece getPiece(Position pos) {
        return getPiece(pos.getRow(), pos.getCol());
    }

    @Override
    public int getPlayerOf(int row) {
        return BoardConfig.getPlayerOf(row);
    }

    @Override
    public int getPlayerOf(Position pos){
        return getPlayerOf(pos.getRow());
    }

    @Override
    public int getPlayerOf(IPiece piece){
        return getPlayerOf(Integer.parseInt(piece.getId().split(",")[0]));
    }

    @Override
    public void move(Position from, Position to) {
        if (!isInBounds(from) || !isInBounds(to))
            return;

        IPiece piece = boardGrid[from.getRow()][from.getCol()];
        if (piece != null) {
            piece.move(to);
        }
    }

    @Override
    public void updateAll() {
        // Step 1 - Reset previous positions
        resetPreviousPositions();

        // Step 2 - Update state and handle captures before movement
        updatePiecesAndHandlePreMoveCaptures();

        // Step 3 - Handle captures after landing and update board positions
        handlePostMoveCapturesAndUpdateBoard();
    }

    private void resetPreviousPositions() {
        for (int row = 0; row < boardConfig.numRowsCols.getX(); row++) {
            for (int col = 0; col < boardConfig.numRowsCols.getY(); col++) {
                IPiece piece = boardGrid[row][col];
                if (piece != null) {
                    int newRow = piece.getRow();
                    int newCol = piece.getCol();
                    if (newRow != row || newCol != col) {
                        boardGrid[row][col] = null;
                    }
                }
            }
        }
    }

    private void updatePiecesAndHandlePreMoveCaptures() {
        for (IPlayer player : players) {
            for (IPiece piece : player.getPieces()) {
                if (piece.isCaptured()) continue;

                if (piece.getCurrentState().isActionFinished()) {
                    int targetRow = piece.getCurrentState().getTargetRow();
                    int targetCol = piece.getCurrentState().getTargetCol();

                    IPiece target = boardGrid[targetRow][targetCol];
                    if (target != null && target != piece && !target.isCaptured() && target.canMoveOver()) {
                        if (target.getCurrentStateName() == EState.JUMP) {
                            players[piece.getPlayer()].markPieceCaptured(piece);
                            logCapture("Captured before move", piece);
                        } else {
                            players[target.getPlayer()].markPieceCaptured(target);
                            logCapture("Captured before move", target);
                        }
                    }
                }

                piece.update();
            }
        }
    }

    private void handlePostMoveCapturesAndUpdateBoard() {
        for (IPlayer player : players) {
            for (IPiece piece : player.getPieces()) {
                if (piece.isCaptured()) continue;

                int row = piece.getRow();
                int col = piece.getCol();

                IPiece existing = boardGrid[row][col];
                if (existing != null && existing != piece && !existing.isCaptured()) {
                    logState("State", existing.getCurrentStateName());
                    if (existing.getCurrentStateName() != EState.JUMP) {
                        players[existing.getPlayer()].markPieceCaptured(existing);
                        logCapture("Captured on landing", existing);
                    } else {
                        players[piece.getPlayer()].markPieceCaptured(piece);
                        logCapture("No capture: piece not jumping on landing", piece);
                    }
                }

                boardGrid[row][col] = piece;
            }
        }
    }

    private void logCapture(String message, IPiece piece) {
        String mes = message + ": " + piece.getId();
        EventPublisher.getInstance().publish(GameEvent.PIECE_CAPTURED, new GameEvent(GameEvent.PIECE_CAPTURED, new ActionData(-1 ,"score update")));
        LogUtils.logDebug(mes);
    }

    private void logState(String message, EState state) {
        String mes = message + ": " + state;
        LogUtils.logDebug(mes);
    }

    @Override
    public boolean isInBounds(int r, int c) {
        return boardConfig.isInBounds(r,c);
    }

    @Override
    public boolean isInBounds(Position p){
        return isInBounds(p.getRow(), p.getCol());
    }

    @Override
    public boolean isMoveLegal(Position from, Position to) {
        IPiece fromPiece = getPiece(from);
        if (fromPiece == null)
            return false;

        // Check resting states first
        if (!fromPiece.getCurrentStateName().isCanAction())
            return false;

        // Check if the move is in the legal move list
        List<Moves.Move> moves = fromPiece.getMoves().getMoves();

        int dx = to.getRow() - from.getRow();
        int dy = to.getCol() - from.getCol();

        boolean isLegal = moves.stream().anyMatch(m -> m.getDx() == dx && m.getDy() == dy);

        if (!isLegal)
            return false;

        // Check path clearance (except knights)
        if (!fromPiece.getType().isCanSkip() && !isPathClear(from, to)) {
            isPathClear(from, to);
            return false;
        }

        // Check if capturing own piece
        IPiece toPiece = getPiece(to);
        return toPiece == null || fromPiece.getPlayer() != toPiece.getPlayer();
    }

    @Override
    public boolean isPathClear(Position from, Position to) {
        int dRow = Integer.signum(to.differenceX(from));
        int dCol = Integer.signum(to.differenceY(from));

        Position current = from.add(dRow, dCol);

        while (!current.equals(to)) {
            if (getPiece(current) != null && !getPiece(current).canMoveOver())
                return false;
            current = current.add(dRow, dCol);
        }

        return true;
    }

    @Override
    public boolean isJumpLegal(IPiece p) {
        return p.getCurrentStateName().isCanAction();
    }

    @Override
    public void jump(IPiece p) {
        if (p == null) return;
        p.jump();
    }

    @Override
    public IPlayer[] getPlayers() {
        return players;
    }

    @Override
    public int getCOLS() {
        return boardConfig.numRowsCols.getY();
    }

    @Override
    public int getROWS() {
        return boardConfig.numRowsCols.getX();
    }

    @Override
    public BoardConfig getBoardConfig() {
        return boardConfig;
    }
}
