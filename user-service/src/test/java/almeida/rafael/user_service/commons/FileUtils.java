package almeida.rafael.user_service.commons;

import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class FileUtils {
  @Autowired
  private ResourceLoader resourceLoader;

  public String readResourceFile(String filename) throws Exception {
    var file = resourceLoader.getResource("classpath:%s".formatted(filename)).getFile();
    return new String(Files.readAllBytes(file.toPath()));
  }
}
