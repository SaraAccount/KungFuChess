package graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import types.EPieceType;
import types.EState;
import utils.LogUtils;

public class GraphicsLoader {

    private static final Map<String, Image> cacheMap = new HashMap<>();

    public static BufferedImage loadSprite(EPieceType pieceType, int player, EState stateName, int frameIndex) {
        String path = String.format("/pieces/%s/states/%s/sprites/sprites%d/%d.png", pieceType.getVal(), stateName, player, frameIndex);

        if (cacheMap.containsKey(path)) {
            return (BufferedImage) cacheMap.get(path);
        }

        try {
            BufferedImage image = ImageIO.read(GraphicsLoader.class.getResourceAsStream(path));
            cacheMap.put(path, image);
            return image;
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Failed to load sprite: " + path);
            LogUtils.logDebug("Failed to load sprite: " + path);
            return null;
        }
    }

    public static BufferedImage[] loadAllSprites(EPieceType pieceType, int player, EState stateName) {
        List<BufferedImage> sprites = new ArrayList<>();
        int index = 1;
        BufferedImage sprite = loadSprite(pieceType, player, stateName, index);

        while (sprite != null) {
            sprites.add(sprite);
            index++;
            sprite = loadSprite(pieceType, player, stateName, index);
        }

        return sprites.toArray(BufferedImage[]::new);
    }
}