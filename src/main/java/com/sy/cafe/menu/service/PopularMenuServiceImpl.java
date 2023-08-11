package com.sy.cafe.menu.service;

import com.sy.cafe.aop.UpdateLock;
import com.sy.cafe.menu.controller.dto.PopularMenuDto;
import com.sy.cafe.menu.repository.MenuRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopularMenuServiceImpl implements PopularMenuService{
    private final MenuRepository menuRepository;
    private final PopularMenuCacheService cacheService;

    public PopularMenuServiceImpl(MenuRepository menuRepository, PopularMenuCacheService cacheService) {
        this.menuRepository = menuRepository;
        this.cacheService = cacheService;
    }

    @Override
    @Cacheable(value = "menu", cacheManager = "cacheManager")
    public List<PopularMenuDto> top3PopularMenusIn7Days() {
        return menuRepository.popularMenus();
    }

    // 인기 메뉴 캐시 추가
    @UpdateLock
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateCache() {
        cacheService.setPopularMenuCache();
    }
}
