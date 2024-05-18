package com.dku.council.domain.oauth.model.entity;

import com.dku.council.global.base.BaseEntity;
import com.dku.council.domain.user.model.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class OauthConnection extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User user;

    @ManyToOne(fetch = LAZY)
    private OauthClient oauthClient;

    private ConnectionStatus status;

    @Override
    public Long getId() {
        return this.id;
    }

    private OauthConnection(User user, OauthClient oauthClient) {
        this.user = user;
        this.oauthClient = oauthClient;
        this.status = ConnectionStatus.CONNECTED;
    }

    public static OauthConnection of(User user, OauthClient oauthClient) {
        return new OauthConnection(user, oauthClient);
    }
}
