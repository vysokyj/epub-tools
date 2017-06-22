package name.vysoky.epub;

/**
 * @author Jiri Vysoky
 */
public interface StyleReport {

    public void styleClass(String resource, int line, int column, String name);

    public void styleId(String resource, int line, int column, String name);

    /**
     * Called when counted some progress percentage.
     * @param percentage
     */
    void progress(int percentage);
}
