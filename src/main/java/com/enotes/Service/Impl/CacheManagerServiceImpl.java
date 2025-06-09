package com.enotes.Service.Impl;

import com.enotes.Service.CacheManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheManagerServiceImpl implements CacheManagerService {

    private final CacheManager cacheManager;

    @Override
    public Collection<String> getCache() {

        Collection<String> allCaches = cacheManager.getCacheNames();
        for (String cacheName : allCaches) {
            Cache cache = cacheManager.getCache(cacheName);
            log.info("Cache :{}", cache);
        }
        return allCaches;
    }

    @Override
    public Cache getCacheName(String cacheName) {

        Cache cache = cacheManager.getCache(cacheName);
        log.info("Cache name :{}", cache);
        return cache;
    }

    @Override
    public void removeAllCache() {

        Collection<String> allCaches = cacheManager.getCacheNames();
        for (String cacheName : allCaches) {
            Cache cache = cacheManager.getCache(cacheName);
            log.info("Cleared cache name :{}", cache);
            assert cache != null;
            cache.clear();
        }
    }

    @Override
    public void removeCacheByName(List<String> cacheName) {

        for (String cache : cacheName) {
            Cache getCache = cacheManager.getCache(cache);
            assert getCache != null;
            getCache.clear();
        }

    }
}
