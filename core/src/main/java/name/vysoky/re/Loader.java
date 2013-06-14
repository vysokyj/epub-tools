package name.vysoky.re;

import java.util.List;

public interface Loader {
    List<ReplacingExpression> getReplacingExpressions() throws Exception;
}
