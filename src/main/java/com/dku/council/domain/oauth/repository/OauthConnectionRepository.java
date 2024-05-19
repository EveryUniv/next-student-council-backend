package com.dku.council.domain.oauth.repository;

import com.dku.council.domain.oauth.model.entity.OauthClient;
import com.dku.council.domain.oauth.model.entity.OauthConnection;
import com.dku.council.domain.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthConnectionRepository extends JpaRepository<OauthConnection, Long> {

    Optional<OauthConnection> findByUserAndOauthClient(User user, OauthClient oauthClient);
}
