package com.qinweizhao.blog.framework.security.service;

import java.util.Optional;

/**
 * One-time-token service interface.
 */
public interface OneTimeTokenService {

    /**
     * Get the corresponding uri.
     *
     * @param oneTimeToken one-time token must not be null
     * @return the corresponding uri
     */
    Optional<String> get(String oneTimeToken);

    /**
     * Create one time token.
     *
     * @param uri request uri.
     * @return one time token.
     */
    String create(String uri);

    /**
     * Revoke one time token.
     *
     * @param oneTimeToken one time token must not be null
     */
    void revoke(String oneTimeToken);
}
