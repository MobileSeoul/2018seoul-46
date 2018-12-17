package me.quiz_together.root.support.hashid;

import java.text.ParseException;
import java.util.Locale;

import me.quiz_together.root.support.HashIdUtils.HashIdType;

public class HashIdExceptionIgnoreFormatter extends HashIdFormatter {
    public HashIdExceptionIgnoreFormatter(HashIdType idType) {
        super(idType);
    }
    //Fommeter Parsing오류시 StandardC
    @Override
    public Long parse(String s, Locale locale) throws ParseException {
        try {
            return super.parse(s, locale);
        } catch (Exception e) {
            return -1L;
        }
    }
}
