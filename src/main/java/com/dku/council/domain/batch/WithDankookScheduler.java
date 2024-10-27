package com.dku.council.domain.batch;

import com.dku.council.domain.with_dankook.model.entity.type.BearEats;
import com.dku.council.domain.with_dankook.model.entity.type.Study;
import com.dku.council.domain.with_dankook.repository.with_dankook.BearEatsRepository;
import com.dku.council.domain.with_dankook.repository.with_dankook.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithDankookScheduler {

    private final BearEatsRepository bearEatsRepository;
    private final StudyRepository studyRepository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void updateBearEats() {
        LocalDateTime expiredTime = LocalDateTime.now().minusHours(6);
        List<BearEats> list = bearEatsRepository.findAllBearEatsWithExpiredTime(expiredTime);
        for (BearEats bearEats : list) {
            bearEats.markAsClosed();
        }
    }

    @Scheduled(cron = "0 0 0,12 * * *")
    @Transactional
    public void updateStudy() {
        List<Study> list = studyRepository.findAllStudyWithExpiredTime(LocalDateTime.now());
        for (Study study : list) {
            study.markAsClosed();
        }
    }
}
