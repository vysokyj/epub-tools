package name.vysoky.epub.ant;

import name.vysoky.epub.EpubTool;
import name.vysoky.epub.TOCGenerator;
import org.apache.tools.ant.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.AntLogger;

import java.io.File;

/**
 * Generates TOC
 * @author Jiri Vysoky
 */
@SuppressWarnings("unused")
public class TOCTask extends Task {

    final Logger logger = LoggerFactory.getLogger(TOCTask.class);

    private String dir;
    private byte minLevel;
    private byte maxLevel;

    public TOCTask() { }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setMinLevel(String minLevel) {
        this.minLevel = Byte.parseByte(minLevel);
    }

    public void setMaxLevel(String maxLevel) {
        this.maxLevel = Byte.parseByte(maxLevel);
    }

    @Override
    public void execute() {
        AntLogger.setProject(getProject());
        try {
            File directory = new File(dir);
            EpubTool tool = new EpubTool(directory);
            TOCGenerator generator = new TOCGenerator(tool, minLevel, maxLevel);
            generator.generate();
        } catch (Exception e) {
            logger.error("Exception!", e);
        }
    }
}

