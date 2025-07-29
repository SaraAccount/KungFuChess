package interfaces;

public interface IGame {

    void addCommand(ICommand cmd);

    void update();

    IPlayer getPlayer1();

    IPlayer getPlayer2();

    IBoard getBoard();

    void handleSelection(IPlayer player);

    IPlayer win();
}
