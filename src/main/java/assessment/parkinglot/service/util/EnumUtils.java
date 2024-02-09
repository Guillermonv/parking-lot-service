package assessment.parkinglot.service.util;

public class EnumUtils {

    public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String value) {
        try {
            Enum.valueOf(enumClass, value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
