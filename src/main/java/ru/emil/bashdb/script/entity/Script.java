package ru.emil.bashdb.script.entity;

import java.io.IOException;
import java.nio.file.Path;
import ru.emil.bashdb.script.proccessing.ScriptProccessorImpl;
import ru.emil.bashdb.script.proccessing.ScriptProcessor;

public class Script {

  private final Path path;
  private final String content;
  private final String tracedContent;

  private Script(final Path path, final String content, final String tracedContent) {
    this.path = path;
    this.content = content;
    this.tracedContent = tracedContent;
  }

  public static Script of(final Path path) throws IOException {
    final ScriptProcessor scriptProcessor = new ScriptProccessorImpl();
    final String scriptContent = scriptProcessor.readScript(path);
    return new Script(
        path,
        scriptContent,
        scriptProcessor.addTracing(scriptContent)
    );
  }

  public Path getPath() {
    return path;
  }

  public String getTracedContent() {
    return tracedContent;
  }

  public String getContent() {
    return content;
  }
}
