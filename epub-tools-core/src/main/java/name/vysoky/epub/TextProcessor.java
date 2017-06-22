package name.vysoky.epub;

/**
 * @author Jiri Vysoky
 */
public interface TextProcessor {

    /**
     * Process input string and return modified string.
     * @param input input string
     * @return output string
     */
    String process(String input);
}
