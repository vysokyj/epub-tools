package name.vysoky.epub.ant;

import name.vysoky.epub.EpubTOCGenerator;
import name.vysoky.epub.EpubTool;
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
    private int minLevel;
    private int maxLevel;

    public TOCTask() { }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setMinLevel(String minLevel) {
        this.minLevel = Integer.parseInt(minLevel);
    }

    public void setMaxLevel(String maxLevel) {
        this.maxLevel = Integer.parseInt(maxLevel);
    }

    @Override
    public void execute() {
        AntLogger.setProject(getProject());
        try {
            File directory = new File(dir);
            EpubTool tool = new EpubTool(directory);
            EpubTOCGenerator generator = new EpubTOCGenerator(tool, minLevel, maxLevel);
            generator.generate();
        } catch (Exception e) {
            logger.error("Exception!", e);
        }
    }
}

