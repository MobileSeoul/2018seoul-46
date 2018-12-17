package me.quiz_together.root.support;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class HashIdUtils {
    public static final String ZERO_USER_ID = "u0";
    public static final String ZERO_BROADCAST_ID = "b0";
    public static final String ZERO_CHAT_ID = "c0";
    public static final String ZERO_STREAM_ID = "s0";
    public static final String ZERO_QUESTION_ID = "s0";

    public enum HashIdType {
        USER_ID('u', "1C9C13923E5A28C99CCDCDF50ED2E8CE", ZERO_USER_ID),
        BROADCAST_ID('b', "2C9C13923E5A28C99CCDCDF50ED2E8CE", ZERO_BROADCAST_ID),
        CHAT_ID('c', "3C9C13923E5A28C99CCDCDF50ED2E8CE", ZERO_CHAT_ID),
        STREAM_ID('s', "4C9C13923E5A28C99CCDCDF50ED2E8CE", ZERO_STREAM_ID),
        QUESTION_ID('q', "5C9C13923E5A28C99CCDCDF50ED2E8CE", ZERO_QUESTION_ID);

        private static final int CACHE_SIZE = 10000;

        private final char prefix;
        private final byte[] secretKey;
        //        private final byte[] iv;
//        private final Cache<Long, String> cache = Caffeine.newBuilder.maximumSize(CACHE_SIZE).build();
        private final String zeroString;

        HashIdType(char prefix, String salt, String zeroString) {
            this.prefix = prefix;
            this.secretKey = decodeHex(salt);
//            this.iv = decodeHex(salt);
            this.zeroString = zeroString;
        }

        public char getPrefix() {
            return prefix;
        }

        public byte[] getSecretKey() {
            return secretKey;
        }

//        public byte[] getIv() {
//            return iv;
//        }

        public String getZeroString() {
            return zeroString;
        }

//        public Cache<Long, String> getCache() {
//            return cache;
//        }

        public boolean isZero(String id) {
            return StringUtils.equals(this.zeroString, id);
        }

        private static byte[] decodeHex(String value) {
            try {
                return Hex.decodeHex(value.toCharArray());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static String encryptId(HashIdType idType, long id) {
        if (id == 0L) {
            return idType.getZeroString();
        }

//            String hashId = idType.getCache().getIfPresent(id);
        String hashId = null;

        if (hashId == null) {
            hashId = encrypt(idType, id);
//                idType.getCache().put(id, hashId);
        }

        return hashId;
    }

    public static long decryptId(HashIdType idType, String hashId) {
        if (idType.isZero(hashId)) {
            return 0L;
        }

        ByteArrayDataInput byteArrayDataInput = decrypt(idType, hashId);
        long id = byteArrayDataInput.readLong();

//        long shardKey = byteArrayDataInput.readLong();
//            if (shardKey != SHARD_KEY) {
//                throw new IllegalArgumentException();
//            }
        return id;
    }

    private static String encrypt(HashIdType idType, long var1) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeLong(var1);
//            output.writeLong(var2);

        try {
            return idType.getPrefix() + Hex.encodeHexString(
                    CipherUtils.encryptAes(output.toByteArray(), idType.getSecretKey()));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static ByteArrayDataInput decrypt(HashIdType idType, String hashId) {
        try {
            return ByteStreams.newDataInput(CipherUtils.decrypteAes(
                    Hex.decodeHex(hashId.substring(1).toCharArray()), idType.getSecretKey()));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
