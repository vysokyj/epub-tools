package name.vysoky.epub.ant;

import name.vysoky.epub.EpubConverter;
import org.apache.tools.ant.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.AntLogger;

/**
 * Ant Task converting EPUB to plain text.
 *
 * @author Jiri Vysoky
 */
public class PlainTextTask extends Task {
    final Logger logger = LoggerFactory.getLogger(PlainTextTask.class);

    private String input;
    private String output;
    private boolean simplify = false;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public boolean isSimplify() {
        return simplify;
    }

    public void setSimplify(boolean simplify) {
        this.simplify = simplify;
    }

    @Override
    public void execute() {
        AntLogger.setProject(getProject());
        try {
            EpubConverter.convertToPlainText(input, output, simplify);
            logger.info("Successfully converted EPUB file: " + input);
        } catch (Exception e) {
            logger.error("Unable convert EPUB to TXT!", e);
        }
    }
}
