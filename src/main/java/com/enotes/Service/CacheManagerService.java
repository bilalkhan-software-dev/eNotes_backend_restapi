package com.enotes.Service;

import org.springframework.cache.Cache;

import java.util.Collection;
import java.util.List;

public interface CacheManagerService {

    Collection<String> getCache();

    Cache getCacheName(String cacheName);

    void removeAllCache();

    void removeCacheByName(List<String> cacheName);
}
