package move;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import types.EPieceType;

public class Moves {

    public class Move {
        int differenceX;
        int differenceY;

        Move(int dx, int dy) {
            this.differenceX = dx;
            this.differenceY = dy;
        }

        public int getDx() {
            return differenceX;
        }

        public int getDy() {
            return differenceY;
        }
    }

    private final List<Move> moves;

    public List<Move> getMoves() {
        return moves;
    }

    public Moves(EPieceType pieceType, int playerId) throws IOException {
        moves = new ArrayList<>();

        String resourcePath = "pieces/" + pieceType.getVal() + "/moves" + playerId + ".txt";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.trim().split(":")[0].split(",");
                    if (parts.length == 2) {
                        int dx = Integer.parseInt(parts[0]);
                        int dy = Integer.parseInt(parts[1]);
                        moves.add(new Move(dx, dy));
                    }
                }
            }
        }
    }
}
