package com.ws.strokeorder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author wangsong
 */
public class RedisCacheManagerConfig extends RedisCacheManager {
    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfiguration;

    public RedisCacheManagerConfig(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        return new CustomRedisCache(name, cacheWriter, cacheConfig);
    }

    @Bean
    public RedisCacheManager jsonRedisCacheManager() {
        return new RedisCacheManager(cacheWriter, defaultCacheConfiguration);
    }
}

class CustomRedisCache extends RedisCache {
    private RedisCacheWriter redisCacheWriter;
    private RedisCacheConfiguration cacheConfiguration;
    private static final String REGX_STR = ".*:\\d+$";
    private static String splitter = ":";

    /**
     * Create new {@link RedisCache}.
     *
     * @param name        must not be {@literal null}.
     * @param cacheWriter must not be {@literal null}.
     * @param cacheConfig must not be {@literal null}.
     */
    protected CustomRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
        redisCacheWriter = cacheWriter;
        cacheConfiguration = cacheConfig;
    }

    @Override
    public void put(Object key, Object value) {
        String name = super.getName();
        if (Pattern.matches(REGX_STR, name)) {
            String[] strings = name.split(splitter);
            Object cacheValue = preProcessCacheValue(value);
            if (!isAllowNullValues() && cacheValue == null) {
                throw new IllegalArgumentException(String.format(
                        "Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == null\")' or configure RedisCache to allow 'null' via RedisCacheConfiguration.",
                        name));
            }
            redisCacheWriter.put(strings[0], serializeCacheKey(createCacheKey(key)), serializeCacheValue(Objects.requireNonNull(cacheValue)), Duration.ofSeconds(Long.parseLong(strings[1])));
        } else {
            super.put(key, value);
        }
    }

    @Override
    protected String createCacheKey(Object key) {
        if (cacheConfiguration.usePrefix()) {
            return prefixCacheKey(convertKey(key));
        } else {
            return convertKey(key);
        }
    }

    private String prefixCacheKey(String key) {
        String name = super.getName();
        if (Pattern.matches(REGX_STR, name)) {
            return cacheConfiguration.getKeyPrefixFor(name.split(splitter)[0]) + key;
        } else {
            // allow contextual cache names by computing the key prefix on every call.
            return cacheConfiguration.getKeyPrefixFor(name) + key;
        }
    }
}
