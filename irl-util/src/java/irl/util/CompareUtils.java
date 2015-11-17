package irl.util;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class CompareUtils {

    public static boolean equal(Object o1, Object o2) {
        return o1 != null ? o1.equals(o2) : (o2 == null);
    }

}
