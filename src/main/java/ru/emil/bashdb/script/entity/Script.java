package ru.emil.bashdb.script.entity;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import ru.emil.bashdb.env.BashEnv;
import ru.emil.bashdb.script.proccessing.ScriptProcessor;
import ru.emil.bashdb.script.proccessing.ScriptProcessorImpl;

public class Script {

  private final Path path;
  private final String content;
  private final String normalisedContent;
  private final String tracedContent;
  private final String bashEnvironment;

  private Script(final ScriptBuilder scriptBuilder) {
    this.path = scriptBuilder.path;
    this.content = scriptBuilder.content;
    this.normalisedContent = scriptBuilder.normalisedContent;
    this.tracedContent = scriptBuilder.tracedContent;
    this.bashEnvironment = scriptBuilder.bashEnvironment;
  }

  public static class ScriptBuilder {

    private Path path;

    private String content;
    private String normalisedContent;
    private String tracedContent;
    private String bashEnvironment;
    private ScriptProcessor scriptProcessor;

    public ScriptBuilder() {
      scriptProcessor = new ScriptProcessorImpl();
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
      Preconditions.checkNotNull(path);
      Preconditions.checkArgument(Files.exists(path));

      this.content = scriptProcessor.readScript(path);

      return this;
    }

    public ScriptBuilder normaliseContent() {
      Preconditions.checkNotNull(content);

      this.normalisedContent = scriptProcessor.normalise(content);

      return this;
    }

    public ScriptBuilder traceContent() {
      Preconditions.checkNotNull(normalisedContent);

      this.tracedContent = scriptProcessor.addTracing(normalisedContent);

      return this;
    }

    public ScriptBuilder evaluateBashEnvironment() throws IOException {
      this.bashEnvironment = BashEnv.getBashEnvironmentRuntime();

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

  public String getBashEnvironment() {
    return bashEnvironment;
  }
}
