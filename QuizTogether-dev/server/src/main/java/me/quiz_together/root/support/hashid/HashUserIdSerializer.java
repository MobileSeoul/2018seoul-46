package me.quiz_together.root.support.hashid;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import me.quiz_together.root.support.HashIdUtils;
import me.quiz_together.root.support.HashIdUtils.HashIdType;

public class HashUserIdSerializer extends StdSerializer<Long> {
    public HashUserIdSerializer() {
        this(null);
    }

    public HashUserIdSerializer(Class<Long> t) {
        super(t);
    }

    @Override
    public void serialize(Long aLong, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(HashIdUtils.encryptId(HashIdType.USER_ID, aLong));
    }

}
