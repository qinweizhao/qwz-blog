package com.qinweizhao.blog.framework.security.service.impl;

import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.framework.security.service.OneTimeTokenService;
import com.qinweizhao.blog.util.HaloUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * One-time token service implementation.
 *
 * @author qinweizhao
 * @since 2019-03-19
 */
@Service
public class OneTimeTokenServiceImpl implements OneTimeTokenService {

    /**
     * One-time token expired day. (unit: day)
     */
    public static final int OTT_EXPIRED_DAY = 1;

    private final AbstractStringCacheStore cacheStore;

    public OneTimeTokenServiceImpl(AbstractStringCacheStore cacheStore) {
        this.cacheStore = cacheStore;
    }

    @Override
    public Optional<String> get(String oneTimeToken) {
        Assert.hasText(oneTimeToken, "One-time token must not be blank");

        // Get from cache store
        return cacheStore.get(oneTimeToken);
    }

    @Override
    public String create(String uri) {
        Assert.hasText(uri, "Request uri must not be blank");

        // Generate ott
        String oneTimeToken = HaloUtils.randomUUIDWithoutDash();

        // Put ott along with request uri
        cacheStore.put(oneTimeToken, uri, OTT_EXPIRED_DAY, TimeUnit.DAYS);

        // Return ott
        return oneTimeToken;
    }

    @Override
    public void revoke(String oneTimeToken) {
        Assert.hasText(oneTimeToken, "One-time token must not be blank");

        // Delete the token
        cacheStore.delete(oneTimeToken);
    }
}
