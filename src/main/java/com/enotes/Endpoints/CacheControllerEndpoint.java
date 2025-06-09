package com.enotes.Endpoints;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Cache", description = "Cache fetch and clear apis")
@RequestMapping("/api/v1/cache")
public interface CacheControllerEndpoint {


    @Operation(summary = "All cache", tags = {"Cache"})
    @GetMapping("/all")
    public ResponseEntity<?> allCache();

    @Operation(summary = "Cache by name", tags = {"Cache"})
    @GetMapping("/{name}")
    public ResponseEntity<?> cacheByName(@PathVariable String name);


    @Operation(summary = "Clear cache", tags = {"Cache"})
    @DeleteMapping("/clear")
    public ResponseEntity<?> removeAllCache();
}
