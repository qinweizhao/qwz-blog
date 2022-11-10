package com.qinweizhao.blog.framework.security.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Access token.
 *
 * @since 19-4-29
 */
@Data
public class AuthToken {

    /**
     * Access token.
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * Expired in. (seconds)
     */
    @JsonProperty("expired_in")
    private int expiredIn;

    /**
     * Refresh token.
     */
    @JsonProperty("refresh_token")
    private String refreshToken;
}
