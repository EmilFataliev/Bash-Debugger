package com.bash.debugger.script.entity;

import com.bash.debugger.script.handling.api.ScriptHandler;
import com.bash.debugger.script.handling.impl.ScriptHandlerImpl;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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

  public static Script withPath(final Path path) {
    final ScriptHandler scriptHandler = new ScriptHandlerImpl();
    String content = null;
    final String handledContent;

    Preconditions.checkState(Objects.nonNull(path));
    Preconditions.checkState(Files.exists(path));

    try {
      content = scriptHandler.read(path);
    } catch (IOException e) {
      logger.error("Can't read " + path.toAbsolutePath().toString());
    }

    Preconditions.checkState(Objects.nonNull(content));
    handledContent = scriptHandler.handleScript(content);
    try {
      Files.write(path, handledContent.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }

    return new Script(
        path,
        content,
        handledContent
    );
  }

  private Script(final Path path, final String content, final String handledContent) {
    this.path = path;
    this.content = content;
    this.handledContent = handledContent;
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
