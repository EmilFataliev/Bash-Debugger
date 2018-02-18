package ru.emil.bashdb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class App {


  static String readScriptFromResources(final String scriptName)
      throws URISyntaxException, IOException {
    // Read script content
    Path path = Paths
        .get(Objects.requireNonNull(App.class.getClassLoader().getResource(scriptName)).toURI());
    StringBuilder script = new StringBuilder();
    Stream<String> lines = Files.lines(path);
    lines.forEach(line -> script.append(line).append("\n"));
    script.deleteCharAt(script.length() - 1);
    lines.close();

    return script.toString();
  }

  public static void main(String[] args) {
    try {

      String scriptContent = readScriptFromResources("script_with_reading.sh");

      BashScriptRunner.run(scriptContent);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
