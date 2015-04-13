package mobi.eyeline.ips.web.validators;

import com.google.common.io.Files;

import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ImageValidator {

  private static final List<String> SUPPORTED_EXTENSIONS = asList("jpg", "jpeg", "png");

  /**
   * @return {@code true} iff considered valid.
   */
  public boolean validate(String imageName) {
    if (isEmpty(imageName)) {
      return false;
    }

    final String ext = Files.getFileExtension(imageName);
    final String name = Files.getNameWithoutExtension(imageName);

    return isNotEmpty(ext) && isNotEmpty(name) &&
        SUPPORTED_EXTENSIONS.contains(ext.toLowerCase());
  }
}
