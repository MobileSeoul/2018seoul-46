package me.quiz_together.root.support.hashid;

import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.Formatter;

import me.quiz_together.root.support.HashIdUtils;
import me.quiz_together.root.support.HashIdUtils.HashIdType;

public class HashIdFormatter implements Formatter<Long> {
    private HashIdType idType;

    public HashIdFormatter(HashIdType idType) {
        this.idType = idType;
    }

    @Override
    public Long parse(String s, Locale locale) throws ParseException {
        if(StringUtils.isBlank(s)) {
            return null;
        }
        return HashIdUtils.decryptId(idType, s);
    }

    @Override
    public String print(Long aLong, Locale locale) {
        return HashIdUtils.encryptId(idType, aLong);
    }
}
