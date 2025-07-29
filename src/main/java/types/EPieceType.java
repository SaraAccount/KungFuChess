package types;

public enum EPieceType {
    B("B", false, 1),
    K("K", false, 100),
    N("N", true, 50),
    P("P", false, 20),
    Q("Q", false, 80),
    R("R", false, 30)
    ;

    private final String val;
    private final boolean canSkip;
    private final int score;

    EPieceType(String val, boolean canSkip, int score){
        this.val = val;
        this.canSkip = canSkip;
        this.score=score;
    }

    public String getVal() {
        return val;
    }

    public boolean isCanSkip() {
        return canSkip;
    }

    public int getScore() {
        return score;
    }
}
