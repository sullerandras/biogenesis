package biogenesis;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Wall represents an obstacle in the game. It is used to restrict movement of
 * organisms.
 * Walls are non movable objects, they are drawn as rectangles.
 * Organisms will bounce off walls if they collide with them.
 */
public class Wall extends java.awt.Rectangle {
  private static final long serialVersionUID = 1L;

  public Wall(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  public void draw(java.awt.Graphics g) {
    g.setColor(java.awt.Color.LIGHT_GRAY);
    g.fillRect(x, y, width, height);
  }

  public static class Serializer implements JsonSerializer<Wall> {
    @Override
    public JsonElement serialize(Wall src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject root = new JsonObject();

      root.addProperty("x", src.x);
      root.addProperty("y", src.y);
      root.addProperty("width", src.width);
      root.addProperty("height", src.height);

      return root;
    }
  }
}
