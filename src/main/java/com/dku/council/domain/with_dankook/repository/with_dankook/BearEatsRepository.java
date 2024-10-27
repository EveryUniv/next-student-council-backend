package com.dku.council.domain.with_dankook.repository.with_dankook;

import com.dku.council.domain.with_dankook.model.entity.type.BearEats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BearEatsRepository extends WithDankookRepository<BearEats>{

    @Query("select b from BearEats b where b.masterUser.id = :userId and " +
            "(b.withDankookStatus='ACTIVE' or b.withDankookStatus='CLOSED') ")
    Page<BearEats> findAllBearEatsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select b from BearEats b " +
            "join WithDankookUser u " +
            "on b.id = u.withDankook.id " +
            "where u.participant.id = :userId and u.reviewStatus = false and " +
            "((b.withDankookStatus in ('FULL', 'CLOSED')) or (b.withDankookStatus = 'ACTIVE' and b.deliveryTime <= CURRENT_TIMESTAMP)) " +
            "order by b.lastModifiedAt DESC ")
    Page<BearEats> findAllPossibleReviewPost(@Param("userId") Long userId,
                                          Pageable pageable);

    @Query("select b from BearEats b where b.withDankookStatus = 'ACTIVE' and b.deliveryTime <= :expiredTime")
    List<BearEats> findAllBearEatsWithExpiredTime(@Param("expiredTime") LocalDateTime expiredTime);
}
