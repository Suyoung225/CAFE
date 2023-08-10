package com.sy.cafe.menu.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PopularMenuCacheService {

    // 인기 메뉴 캐시 추가
    @CachePut(value = "menu", cacheManager = "cacheManager")
    public void setPopularMenuCache() {
        log.info("캐시 추가");
    }

}
