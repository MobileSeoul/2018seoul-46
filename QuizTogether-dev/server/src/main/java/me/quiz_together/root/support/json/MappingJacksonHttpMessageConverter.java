package me.quiz_together.root.support.json;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MappingJacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private static final Pattern COMPILE = Pattern.compile("[\r\n]+");

    public MappingJacksonHttpMessageConverter() {
        super(Jackson2ObjectMapperBuilder.json().build());
    }

    public MappingJacksonHttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException,
                                                                                        HttpMessageNotReadableException {
        Object object = super.readInternal(clazz, inputMessage);

        log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));

        return object;
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException,
                                                                                        HttpMessageNotWritableException {
        log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));

        super.writeInternal(object, outputMessage);
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException,
                                                                                                   HttpMessageNotWritableException {
        String responseBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        if (StringUtils.isNotBlank(responseBody)) {
            String[] lines = COMPILE.split(responseBody);
            log.info("<< RESPONSE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            for (String line : lines) {
                log.info("<< BODY << {}", line);
            }
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
        super.writeInternal(object, type, outputMessage);
    }
}
