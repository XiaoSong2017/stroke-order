package com.ws.strokeorder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

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
        return super.createRedisCache(name, cacheConfig);
    }

    @Bean
    public RedisCacheManager jsonRedisCacheManager() {
        return new RedisCacheManager(cacheWriter, defaultCacheConfiguration);
    }
}
