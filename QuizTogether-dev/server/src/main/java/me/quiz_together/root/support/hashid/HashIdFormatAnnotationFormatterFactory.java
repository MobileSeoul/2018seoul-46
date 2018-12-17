package me.quiz_together.root.support.hashid;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

public class HashIdFormatAnnotationFormatterFactory extends EmbeddedValueResolutionSupport implements
                                                                                           AnnotationFormatterFactory<HashId> {
    private static final Set<Class<?>> FIELD_TYPES;

    @Override
    public Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    @Override
    public Printer<Long> getPrinter(HashId hashId, Class<?> aClass) {
        return configureFormatterFrom(hashId);
    }

    @Override
    public Parser<Long> getParser(HashId hashId, Class<?> aClass) {
        return configureFormatterFrom(hashId);
    }

    private Formatter<Long> configureFormatterFrom(HashId hashId) {
        return new HashIdExceptionIgnoreFormatter(hashId.value());
    }

    static {
        HashSet fieldType = new HashSet(1);
        fieldType.add(Long.class);
        FIELD_TYPES = Collections.unmodifiableSet(fieldType);
    }
}
