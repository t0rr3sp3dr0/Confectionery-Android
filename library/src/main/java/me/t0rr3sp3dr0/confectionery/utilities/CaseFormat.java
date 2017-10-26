package me.t0rr3sp3dr0.confectionery.utilities;

/**
 * A helper for case format manipulation.
 *
 * @author Pedro Tôrres
 * @since 0.0.4
 */
public final class CaseFormat {
    public static String pascalToSnake(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (i > 0 && Character.isUpperCase(c) && !Character.isUpperCase(s.charAt(i - 1)))
                sb.append('_');
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
}
