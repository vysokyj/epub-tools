package name.vysoky.epub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Smart quotes text processor.
 *
 * @author Jiri Vysoky
 */
public class SmartQuoter implements TextProcessor {

    final Logger logger = LoggerFactory.getLogger(SmartQuoter.class);
    
    public static final char[] DOUBLE_QUOTES = { '"', '“', '”', '„', '"' };
    public static final char[] SINGLE_QUOTES = { '\'', '‘', '’', '‹', '›' };
    
    public static final char DEFAULT_DOUBLE_QUOTE = '"';
    public static final char DEFAULT_SINGLE_QUOTE = '\'';

    // switches
    private boolean lastDoubleQuoteIsLeading = false;
    private boolean lastSingleQuoteIsLeading = false;

    private char leadingDoubleQuote = '"';
    private char trailingDoubleQuote = '"';
    private char leadingSingleQuote = '\'';
    private char trailingSingleQuote = '\'';
    
    private final Pattern leadingDoubleQuotePattern;
    private final Pattern trailingDoubleQuotePattern;
    private final Pattern leadingSingleQuotePattern;
    private final Pattern trailingSingleQuotePattern;
    
    public SmartQuoter(char leadingDoubleQuote, char trailingDoubleQuote,
                       char leadingSingleQuote, char trailingSingleQuote) {
        this.leadingDoubleQuote = leadingDoubleQuote;
        this.trailingDoubleQuote = trailingDoubleQuote;
        this.leadingSingleQuote = leadingSingleQuote;
        this.trailingSingleQuote = trailingSingleQuote;
        this.leadingDoubleQuotePattern = Pattern.compile("([\\s:;])?" + leadingDoubleQuote + "([^\\s])?"); 
        this.leadingSingleQuotePattern = Pattern.compile("([\\s:;])?" + leadingSingleQuote + "([^\\s])?");
        this.trailingDoubleQuotePattern = Pattern.compile("[^\\s]" + leadingDoubleQuote + "([\\s.,;?!])?");
        this.trailingSingleQuotePattern = Pattern.compile("[^\\s]" + leadingSingleQuote + "([\\s.,;?!])?");
    }

    @Override
    public String process(String input) {
        input = convertToDefaultQuotes(input);
        char[] chars = input.toCharArray();
        processChars(chars);
        return new String(chars);
    }    

    private String convertToDefaultQuotes(String input) {
        for (char c : SINGLE_QUOTES) input = input.replace(c, DEFAULT_SINGLE_QUOTE);
        for (char c : DOUBLE_QUOTES) input = input.replace(c, DEFAULT_DOUBLE_QUOTE);
        return input;
    }
    
    private String getNearChars(char[] input, int index) {
        char[] chars = new char[3];
        chars[0] = ((index - 1) >= 0) ? input[index - 1] : '\n';
        chars[1] = input[index];
        chars[2] = ((index + 1) < input.length) ? input[index + 1] : '\n';
        return new String(chars);        
    }
    
    private boolean isProbablyLeadingDoubleQuote(char[] input, int index) {
        Matcher matcher = leadingDoubleQuotePattern.matcher(getNearChars(input, index));
        return matcher.matches();
    }

    private boolean isProbablyLeadingSingleQuote(char[] input, int index) {
        Matcher matcher = leadingSingleQuotePattern.matcher(getNearChars(input, index));
        return matcher.matches();
    }


    private boolean isProbablyTrailingDoubleQuote(char[] input, int index) {
        Matcher matcher = trailingDoubleQuotePattern.matcher(getNearChars(input, index));
        return matcher.matches();
    }

    private boolean isProbablyTrailingSingleQuote(char[] input, int index) {
        Matcher matcher = trailingSingleQuotePattern.matcher(getNearChars(input, index));
        return matcher.matches();
    }
    
    private void processDoubleQuote(char[] input, int index) {
        if (!lastDoubleQuoteIsLeading && isProbablyLeadingDoubleQuote(input, index)) {
            input[index] = leadingDoubleQuote;
            lastDoubleQuoteIsLeading = true;
            return;
        }

        if (lastDoubleQuoteIsLeading && isProbablyTrailingDoubleQuote(input, index)) {
            input[index] = trailingDoubleQuote;
            lastDoubleQuoteIsLeading = false;
            return;
        }
        
        if (lastDoubleQuoteIsLeading && isProbablyLeadingDoubleQuote(input, index)) {
            logger.warn("Probably missing trailing quote before " + index);
            input[index] = leadingDoubleQuote;
            lastDoubleQuoteIsLeading = true;
            return;
        }

        if (!lastDoubleQuoteIsLeading && isProbablyTrailingDoubleQuote(input, index)) {
            logger.warn("Probably missing leading quote before " + index);
            input[index] = leadingDoubleQuote;
            lastDoubleQuoteIsLeading = true;
            return;
        }
        
        if (!isProbablyLeadingDoubleQuote(input, index) && isProbablyTrailingDoubleQuote(input, index)) {
            logger.warn("Unknown quote using last matching " + index);
            input[index] = lastDoubleQuoteIsLeading ? trailingDoubleQuote : leadingDoubleQuote;
            lastDoubleQuoteIsLeading = !lastDoubleQuoteIsLeading;
        }            
    }
    
    private void processSingleQuote(char[] input, int index) {
        if (!lastSingleQuoteIsLeading && isProbablyLeadingSingleQuote(input, index)) {
            input[index] = leadingSingleQuote;
            lastSingleQuoteIsLeading = true;
            return;
        }

        if (lastSingleQuoteIsLeading && isProbablyTrailingSingleQuote(input, index)) {
            input[index] = trailingSingleQuote;
            lastSingleQuoteIsLeading = false;
            return;
        }

        if (lastSingleQuoteIsLeading && isProbablyLeadingSingleQuote(input, index)) {
            logger.warn("Probably missing trailing quote before " + index);
            input[index] = leadingSingleQuote;
            lastSingleQuoteIsLeading = true;
            return;
        }

        if (!lastSingleQuoteIsLeading && isProbablyTrailingSingleQuote(input, index)) {
            logger.warn("Probably missing leading quote before " + index);
            input[index] = leadingSingleQuote;
            lastSingleQuoteIsLeading = true;
            return;
        }

        if (!isProbablyLeadingSingleQuote(input, index) && isProbablyTrailingSingleQuote(input, index)) {
            logger.warn("Unknown quote using last matching " + index);
            input[index] = lastSingleQuoteIsLeading ? trailingSingleQuote : leadingSingleQuote;
            lastSingleQuoteIsLeading = !lastSingleQuoteIsLeading;
        }
    }
    
    private void processChars(char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == DEFAULT_DOUBLE_QUOTE) processDoubleQuote(chars, i);
            if (c == DEFAULT_SINGLE_QUOTE) processSingleQuote(chars, i);
        }
    }
}
