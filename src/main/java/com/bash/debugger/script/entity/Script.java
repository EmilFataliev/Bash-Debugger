package com.bash.debugger.script.entity;

import com.bash.debugger.script.handling.api.ScriptHandler;
import com.bash.debugger.script.handling.impl.ScriptHandlerImpl;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The entity for storing the script
 */
public class Script {

  /* Absolute path to script */
  private final Path path;

  private final String content;

  private final String handledContent;

  private static final Logger logger = LoggerFactory.getLogger(Script.class);

  private Script(final ScriptBuilder scriptBuilder) {
    this.path = scriptBuilder.path;
    this.content = scriptBuilder.content;
    this.handledContent = scriptBuilder.handledContent;
  }

  public static class ScriptBuilder {

    /**
     * Block of entity fields
     **/
    private Path path;
    private String content;
    private String handledContent;
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


    public ScriptBuilder readContent() {
      Preconditions.checkState(Objects.nonNull(path));
      Preconditions.checkState(Files.exists(path));

      try {
        this.content = scriptHandler.read(path);
      } catch (IOException e) {
        logger.error("Can't read " + path.toAbsolutePath().toString());
      }
      return this;
    }

    public ScriptBuilder handleContent() {
      Preconditions.checkState(Objects.nonNull(content));

      this.handledContent = scriptHandler.handleScript(content);
      return this;
    }

    public Script build() {
      return new Script(this);
    }


  }

  public Path getPath() {
    return path;
  }

  public String getContent() {
    return content;
  }

  public String getHandledContent() {
    return handledContent;
  }

}
