package ru.emil.bashdb.script.entity;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import ru.emil.bashdb.script.handling.api.ScriptHandler;
import ru.emil.bashdb.script.handling.impl.ScriptHandlerImpl;


/**
 * The entity for storing the script
 */
public class Script {

  /* Absolute path to script */
  private final Path path;

  /* Original content of script */
  private final String content;

  /* Normalised content of script @see ScriptHandler#normalise */
  private final String normalisedContent;

  /* Traced content of script @see ScriptHandler#addTracing */
  private final String tracedContent;

  private Script(final ScriptBuilder scriptBuilder) {
    this.path = scriptBuilder.path;
    this.content = scriptBuilder.content;
    this.normalisedContent = scriptBuilder.normalisedContent;
    this.tracedContent = scriptBuilder.tracedContent;
  }

  public static class ScriptBuilder {

    private Path path;

    private String content;
    private String normalisedContent;
    private String tracedContent;
    private ScriptHandler scriptHandler;

    public ScriptBuilder() {
      scriptHandler = new ScriptHandlerImpl();
    }

    public ScriptBuilder withPath(final Path path) {
      this.path = path;

      return this;
    }

    public ScriptBuilder withPath(final String path) {
      this.path = Paths.get(path);

      return this;
    }

    public ScriptBuilder withContent(final String content) {
      this.content = content;

      return this;
    }


    public ScriptBuilder readContent() throws IOException {
      Preconditions.checkState(Objects.nonNull(path));
      Preconditions.checkState(Files.exists(path));

      this.content = scriptHandler.readScript(path);
      return this;
    }

    public ScriptBuilder normaliseContent() {
      Preconditions.checkState(Objects.nonNull(content));

      this.normalisedContent = scriptHandler.normalise(content);
      return this;
    }

    public ScriptBuilder traceContent() {
      Preconditions.checkState(Objects.nonNull(normalisedContent));

      this.tracedContent = scriptHandler.addTracing(normalisedContent);
      return this;
    }

    public Script build() {
      return new Script(this);
    }

  }

  public String getContent() {
    return content;
  }

  public Path getPath() {
    return path;
  }

  public String getTracedContent() {
    return tracedContent;
  }

  public String getNormalisedContent() {
    return normalisedContent;
  }

}
