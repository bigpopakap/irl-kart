package irl.util;

import java.awt.*;
import java.util.Random;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/17/15
 */
public class ColorUtils {

    private static final int MAX = 255;

    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public static Color makeTransparent(Color color, double trans) {
        return new Color(
            color.getRed(),
            color.getGreen(),
            color.getBlue(),
            (int) (trans * MAX)
        );
    }

    public static Color random() {
        return random(1.0);
    }

    public static Color random(double trans) {
        Random random = new Random(System.currentTimeMillis());
        return new Color(
            random.nextInt(MAX),
            random.nextInt(MAX),
            random.nextInt(MAX),
            (int) (trans * MAX)
        );
    }

}
