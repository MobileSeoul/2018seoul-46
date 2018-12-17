package me.quiz_together.root.support;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiVersion {
    V1_0(1.0f),
    UNKNOWN(0.0f);

    private final float version;
    private static final Map<Float, ApiVersion> CODE_LOOKUP = new HashMap<>();

    public static final ApiVersion LATEST_API_VER = V1_0;

    static {
        for (ApiVersion version : ApiVersion.values()) {
            CODE_LOOKUP.put(version.getVersion(), version);
        }
    }

    public static ApiVersion find(float version) {
        if(CODE_LOOKUP.containsKey(version)) {
            return CODE_LOOKUP.get(version);
        }

        return UNKNOWN;
    }

    public boolean isLower(ApiVersion other) {
        return getVersion() < other.getVersion();
    }
}
