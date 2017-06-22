package name.vysoky.epub.ant;

import name.vysoky.epub.EpubZip;
import org.apache.tools.ant.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.AntLogger;

import java.io.File;

/**
 * Task for compressing EPUB file.
 * EPUB has specific ZIP requirements, such as uncompressed mimetype file.
 *
 * @author Jiri Vysoky
 */
@SuppressWarnings("unused")
public class ZipTask extends Task {

    final Logger logger = LoggerFactory.getLogger(ZipTask.class);

    private String dir;
    private String file;

    public void setFile(String file) {
        this.file = file;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public void execute() {
        AntLogger.setProject(getProject());
        try {
            EpubZip.compress(new File(dir), new File(file));
            logger.info("Successfully compressed to file: " + file);
        } catch (Exception e) {
            logger.error("Unable to compress directory!", e);
        }
    }
}
