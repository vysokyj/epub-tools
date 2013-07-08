package name.vysoky.epub.ant;

import name.vysoky.epub.EpubCorrector;
import name.vysoky.epub.EpubTool;
import org.apache.tools.ant.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.AntLogger;

import java.io.File;
import java.util.Locale;

/**
 * Smart quotation.
 *
 * @author Jiri Vysoky
 */
public class QuoteTask extends Task {

    final Logger logger = LoggerFactory.getLogger(QuoteTask.class);

    /** Default locale language used by corrector. */
    public static final String DEFAULT_LANGUAGE = "cs";
    /** Default locale country used by corrector. */
    public static final String DEFAULT_COUNTRY = "CZ";

    private String dir;
    private String language = DEFAULT_LANGUAGE;
    private String country = DEFAULT_COUNTRY;

    /**
     * Locale language name, e.g. "en" from "en_US" locale.
     * @param language language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Locale country name, e.g. "US" form "en_US" locale.
     * @param country country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * EPUB directory location.
     * @param dir directory path
     */
    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public void execute() {
        AntLogger.setProject(getProject());
        try {
            File directory = new File(dir);
            Locale locale = new Locale(language, country);
            EpubTool epubTool = new EpubTool(directory);
            EpubCorrector corrector = new EpubCorrector(epubTool, locale);
            corrector.executeSmartQuoter();
        } catch (Exception e) {
            logger.error("Unable to process epub!", e);
        }
    }

}
