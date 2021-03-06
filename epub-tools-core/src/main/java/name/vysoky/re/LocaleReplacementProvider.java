package name.vysoky.re;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocaleReplacementProvider implements ReplacementProvider {

    public static final String RESOURCE_ENCODING = "UTF-8";
    public static final String RESOURCE_DIRECTORY = "/";
    public static final String RESOURCE_EXTENSION = ".re";

    private String preparedComment;
    private List<Replacement> replacingExpressions = new ArrayList<Replacement>();
    private Locale locale;

    public LocaleReplacementProvider(Locale locale) throws IOException {
        this.locale = locale;
        parserResource();
    }

    @Override
    public List<Replacement> getReplacements() {
        return replacingExpressions;
    }

    private void parserResource() throws IOException {
        String resource = RESOURCE_DIRECTORY + locale.toString() + RESOURCE_EXTENSION;
        BufferedReader reader = null;
        try {
            InputStream is = getClass().getResourceAsStream(resource);
            if (is == null) throw new NullPointerException("Input stream is null!");
            reader = new BufferedReader(new InputStreamReader(is, RESOURCE_ENCODING));
            String line;
            while ((line = reader.readLine()) != null) parseLine(line);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e){
                // ignored
            }
        }
    }

    private void parseLine(String line) {
        if (line.trim().isEmpty()) return;
        if (line.trim().startsWith("#")) {
            preparedComment = line;
        } else {
            Replacement ex = new Replacement(preparedComment, line);
            replacingExpressions.add(ex);
            preparedComment = null;
        }
    }
}
