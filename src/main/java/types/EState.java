package types;

public enum EState {
    IDLE("idle", true, false),
    JUMP("jump", false, false),
    MOVE("move", false, true),
    LONG_REST("long_rest", false, true),
    SHORT_REST("short_rest", false, true);

    private final String name;
    private final boolean canAction;
    private final boolean canMoveOver;

    EState(String name, boolean canAction, boolean canMoveOver){
        this.name = name;
        this.canAction = canAction;
        this.canMoveOver = canMoveOver;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isCanAction(){
        return canAction;
    }

    public boolean isCanMoveOver() {
        return canMoveOver;
    }

    public static EState getValueOf(String s){
        return EState.valueOf(s.toUpperCase());
    }
}
