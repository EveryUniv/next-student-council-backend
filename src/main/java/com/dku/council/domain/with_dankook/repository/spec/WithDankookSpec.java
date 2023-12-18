package com.dku.council.domain.with_dankook.repository.spec;

import com.dku.council.domain.with_dankook.model.WithDankookStatus;
import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import org.springframework.data.jpa.domain.Specification;

public class WithDankookSpec {

    public static <T extends WithDankook> Specification<T> withActive() {
        return (root, query, builder) ->
                builder.equal(root.get("withDankookStatus"), WithDankookStatus.ACTIVE);
    }

    public static <T extends WithDankook> Specification<T> withTitleOrBody(String keyword) {
        if (keyword == null || keyword.equals("null")) {
            return Specification.where(null);
        }

        String pattern = "%" + keyword + "%";
        return (root, query, builder) ->
                builder.or(
                        builder.like(root.get("title"), pattern),
                        builder.like(root.get("content"), pattern)
                );
    }
}
