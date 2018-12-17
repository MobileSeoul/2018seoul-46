package me.quiz_together.root.support;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import me.quiz_together.root.support.enumeration.CodeEnum;
import me.quiz_together.root.support.enumeration.ValueEnum;

public class EnumUtils {

	public static <T extends Enum<T> & CodeEnum> T getCodeEnum(Class<T> enumClass, String code) {

		EnumSet<T> set = EnumSet.allOf(enumClass);

		for (T t : set) {

			if (t.getCode().equals(code)) {
				return t;
			}
		}

		return null;
	}

	public static <T extends Enum<T> & CodeEnum> T getCodeEnum(Class<T> enumClass, String code, T defaultEnum) {

		T codeEnum = getCodeEnum(enumClass, code);

		if (codeEnum == null) {
			return defaultEnum;
		}

		return codeEnum;
	}

	public static <T extends Enum<T> & ValueEnum> T getValueEnum(Class<T> enumClass, int value) {

		EnumSet<T> set = EnumSet.allOf(enumClass);

		for (T t : set) {

			if (t.getValue() == value) {
				return t;
			}
		}

		return null;
	}

	public static <T extends Enum<T> & ValueEnum> T getValueEnum(Class<T> enumClass, int value, T defaultEnum) {

		T valueEnum = getValueEnum(enumClass, value);

		if (valueEnum == null) {
			return defaultEnum;
		}

		return valueEnum;
	}
	
	@Deprecated
	public static <T extends Enum<T>> List<T> getEnumList(Class<T> enumClass) {
		return new ArrayList<T>(EnumSet.allOf(enumClass));
	}

    public static <E extends Enum<E>> E parseIgnoreCase(Class<E> clz, String str) {
        E[] constants = clz.getEnumConstants();
        if (str == null) {
            return null;
        }
        for (E e : constants) {
            if (e.name().equalsIgnoreCase(str)) {
                return e;
            }
        }
        throw new IllegalArgumentException(String.format("Illegal type='%s'. Supported type values: %s", str, constants));
    }
}
