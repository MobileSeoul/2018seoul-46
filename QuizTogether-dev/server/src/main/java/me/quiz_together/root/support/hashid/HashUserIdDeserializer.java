package me.quiz_together.root.support.hashid;

import java.io.IOException;
import java.text.ParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import me.quiz_together.root.support.HashIdUtils.HashIdType;

public class HashUserIdDeserializer extends StdDeserializer<Long> {
    private static HashIdFormatter formatter = new HashIdFormatter(HashIdType.USER_ID);

    public HashUserIdDeserializer() {
        this(null);
    }
    public HashUserIdDeserializer(Class<Long> t) {
        super(t);
    }

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        try {
            return formatter.parse(_parseString(jsonParser, deserializationContext), null);
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }
}
