package biogenesis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonFileSaver {
  /**
   * Annotation to exclude a field from being serialized by Gson.
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.FIELD })
  public @interface ExcludeFromGson {
    // Field tag only annotation
  }

  /**
   * Exclusion strategy for Gson. It excludes fields that are annotated with
   * {@link ExcludeFromGson}.
   */
  public static class ExclusionStrategyForGson implements ExclusionStrategy {
    public ExclusionStrategyForGson() {
    }

    public boolean shouldSkipClass(Class<?> clazz) {
      return false;
    }

    public boolean shouldSkipField(FieldAttributes f) {
      return f.getAnnotation(ExcludeFromGson.class) != null;
    }
  }

  public static class JavaAwtColorAdapter implements JsonSerializer<java.awt.Color> {
    @Override
    public JsonElement serialize(java.awt.Color src, java.lang.reflect.Type typeOfSrc,
        JsonSerializationContext context) {
      JsonObject obj = new JsonObject();
      obj.addProperty("r", src.getRed());
      obj.addProperty("g", src.getGreen());
      obj.addProperty("b", src.getBlue());
      obj.addProperty("a", src.getAlpha());
      return obj;
    }
  }

  public static void saveWorldJson(World world, File f) {
    try {
      final FileWriter w = new FileWriter(f);
      try {
        GsonBuilder builder = new GsonBuilder()
            .registerTypeAdapter(java.awt.Color.class, new JavaAwtColorAdapter())
            .excludeFieldsWithoutExposeAnnotation();
        w.write(builder.create().toJson(world));
      } finally {
        w.close();
      }
    } catch (IOException ex) {
      System.err.println(ex.getMessage());
    }
  }
}
