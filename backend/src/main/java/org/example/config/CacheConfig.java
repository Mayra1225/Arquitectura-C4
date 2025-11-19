package org.example.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class CacheConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisCacheManager cacheManager() {
        // TTL por cache name
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();

        // 24 horas para datos SRI
        cacheConfigs.put("sri-contribuyente", defaultConfig().entryTtl(Duration.ofHours(24)));
        cacheConfigs.put("sri-existe", defaultConfig().entryTtl(Duration.ofHours(24)));

        // 12 horas para vehiculo
        cacheConfigs.put("vehiculo", defaultConfig().entryTtl(Duration.ofHours(12)));

        // 7 dias para ant-puntos (menos disponibilidad -> mantener más tiempo)
        cacheConfigs.put("ant-puntos", defaultConfig().entryTtl(Duration.ofDays(7)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig())
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }

    private RedisCacheConfiguration defaultConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofHours(1)); // default
    }
}
