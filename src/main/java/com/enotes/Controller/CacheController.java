package com.enotes.Controller;

import com.enotes.Endpoints.CacheControllerEndpoint;
import com.enotes.Service.CacheManagerService;
import com.enotes.Util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class CacheController implements CacheControllerEndpoint {

    private final CacheManagerService cacheManagerService;

    @Override
    public ResponseEntity<?> allCache() {

        Collection<String> caches = cacheManagerService.getCache();
        if (CollectionUtils.isEmpty(caches)) {
            return CommonUtil.createBuildResponseMessage("No caches found", HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(caches, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> cacheByName(String name) {
        Cache cacheName = cacheManagerService.getCacheName(name);


        if (ObjectUtils.isEmpty(cacheName)) {
            return CommonUtil.createErrorResponseMessage("No cache found by name", HttpStatus.NOT_FOUND);
        }

        return CommonUtil.createBuildResponse(cacheName, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> removeAllCache() {
        cacheManagerService.removeAllCache();
        return CommonUtil.createErrorResponseMessage("Successfully clear all cache ", HttpStatus.OK);
    }
}
