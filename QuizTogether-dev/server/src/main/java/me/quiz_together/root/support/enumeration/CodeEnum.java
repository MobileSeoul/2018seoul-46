package me.quiz_together.root.support.enumeration;

public interface CodeEnum<T extends Enum<T> & CodeEnum<T>> {
	String getCode();
}
