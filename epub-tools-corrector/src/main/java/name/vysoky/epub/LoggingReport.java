package name.vysoky.epub;

import com.adobe.epubcheck.api.Report;
import com.adobe.epubcheck.util.FeatureEnum;

/**
 * EpubCheck report implementation
 * @author Jiri Vysoky
 */
public class LoggingReport implements Report {

    private int errorCount = 0;
    private int warningCount = 0;
    private int exceptionCount = 0;

    @Override
    public void error(String resource, int line, int column, String message) {
        errorCount++;
        log("ERROR", resource, line, column, message);
    }

    @Override
    public void warning(String resource, int line, int column, String message) {
        warningCount++;
        log("WARNING", resource, line, column, message);
    }

    @Override
    public void exception(String resource, Exception e) {
        exceptionCount++;
        System.out.println("EXCEPTION: " + resource);
        e.printStackTrace();
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }

    @Override
    public int getWarningCount() {
        return warningCount;
    }

    @Override
    public int getExceptionCount() {
        return exceptionCount;
    }

    @Override
    public void info(String resource, FeatureEnum feature, String value) {
//        String f =  (feature == null) ? "" : " " + feature.toString();
//        System.out.println("FEATURE: " + resource + f + " - " + value);
    }

    private void log(String tag, String resource, int line, int column, String message)  {
        String s;
        if (resource == null) s = tag + ": " + message;
        else s = tag + ": " + resource + " [" + line + ", "  + column + "] " + message;
        System.out.println(s);
    }
}
