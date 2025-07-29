package pieces;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import board.BoardConfig;
import graphics.GraphicsLoader;
import interfaces.IGraphicsData;
import interfaces.IPhysicsData;
import interfaces.IState;
import state.GraphicsData;
import state.PhysicsData;
import state.State;
import types.EPieceType;
import types.EState;
import utils.LogUtils;

public class PiecesFactory {

    private static double tileSize;
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Piece createPieceByType(EPieceType type, int playerId, Position pos, BoardConfig config) {
        tileSize = config.tileSize;

        Map<EState, IState> states = new HashMap<>();
        String basePath = "/pieces/" + type.getVal() + "/states/";

        try {
            // Step 1 – Find all existing states in the states directory
            URL dirURL = PiecesFactory.class.getResource(basePath);
            if (dirURL == null || !dirURL.getProtocol().equals("file")) {
                System.err.println("Cannot load states from: " + basePath);
                LogUtils.logDebug("Cannot load states from: " + basePath);
                return null;
            }

            File statesDir = new File(dirURL.toURI());
            File[] subdirs = statesDir.listFiles(File::isDirectory);
            if (subdirs == null) return null;

            // Step 2 – Load each state
            for (File stateFolder : subdirs) {
                EState stateName = EState.getValueOf(stateFolder.getName());
                String configPath = basePath + stateName + "/config.json";
                InputStream is = PiecesFactory.class.getResourceAsStream(configPath);
                if (is == null) {
                    System.err.println("Missing config for state: " + stateName);
                    LogUtils.logDebug("Missing config for state: " + stateName);
                    continue;
                }

                JsonNode root = mapper.readTree(is);
                JsonNode physicsNode = root.path("physics");
                double speed = physicsNode.path("speed_m_per_sec").asDouble(0.0);
                EState nextState = EState.getValueOf(physicsNode.path("next_state_when_finished").asText(stateName.toString()));

                IPhysicsData physics = new PhysicsData(speed, nextState);

                JsonNode graphicsNode = root.path("graphics");
                int fps = graphicsNode.path("frames_per_sec").asInt(1);
                boolean isLoop = graphicsNode.path("is_loop").asBoolean(true);

                BufferedImage[] sprites = GraphicsLoader.loadAllSprites(type, BoardConfig.getPlayerOf(pos.getRow()), stateName);
                if (sprites.length == 0) {
                    System.err.println("No sprites for state: " + stateName);
                    LogUtils.logDebug("No sprites for state: " + stateName);
                    continue;
                }

                IGraphicsData graphics = new GraphicsData(sprites, fps, isLoop);
                IState state = new State(stateName, pos, pos, tileSize, physics, graphics);
                states.put(stateName, state);
            }

            if (states.isEmpty()) {
                LogUtils.logDebug("No states loaded for piece: " + type.getVal());
                return null;
            }

            // Step 3 – Create the Piece with the first state as default
            EState initialState = EState.IDLE;
            return new Piece(type,playerId, states, initialState, pos);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.logDebug("Exception in createPieceByCode: " + e.getMessage());
            return null;
        }
    }
}
