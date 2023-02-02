package com.sy.cafe.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class EmitterRepositoryImplTest {

    @InjectMocks
    EmitterRepository emitterRepository = new EmitterRepositoryImpl();

    private Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private String DATA_PLATFORM_ID = "OrderData_";

    @Test
    @DisplayName("emitter 저장")
    void save() {
        // given
        String emitterId = DATA_PLATFORM_ID + System.currentTimeMillis();
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        // when, then
        assertDoesNotThrow(
                () -> emitterRepository.save(emitterId, sseEmitter));
    }

    @Test
    @DisplayName("저장된 emitter 찾기")
    void findAllEmitters() throws InterruptedException {
        // given
        String emitterId1 = DATA_PLATFORM_ID + System.currentTimeMillis();
        SseEmitter sseEmitter1 = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(emitterId1, sseEmitter1);

        Thread.sleep(100);

        String emitterId2 = DATA_PLATFORM_ID + System.currentTimeMillis();
        SseEmitter sseEmitter2 = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(emitterId2, sseEmitter2);

        // when
        Map<String, SseEmitter> allEmitters = emitterRepository.findAllEmitters(DATA_PLATFORM_ID);

        // then
        assertThat(allEmitters.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("repository에서 emitter 제거")
    void deleteById(){
        // given
        long currentTime = System.currentTimeMillis();
        String emitterId1 = DATA_PLATFORM_ID + currentTime;
        SseEmitter sseEmitter1 = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(emitterId1, sseEmitter1);

        String emitterId2 = DATA_PLATFORM_ID + (currentTime + 1000);
        SseEmitter sseEmitter2 = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(emitterId2, sseEmitter2);

        // when
        emitterRepository.deleteById(emitterId1);

        // then
        assertThat(emitterRepository.findAllEmitters(DATA_PLATFORM_ID).size()).isEqualTo(1);
    }
}