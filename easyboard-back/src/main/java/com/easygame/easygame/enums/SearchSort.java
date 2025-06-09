package com.easygame.easygame.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum SearchSort {
    TITLE_ASC(Sort.by(Sort.Direction.ASC, "title")),
    TITLE_DESC(Sort.by(Sort.Direction.DESC, "title")),
    CREATEDAT_ASC(Sort.by(Sort.Direction.ASC, "createdAt")),
    CREATEDAT_DESC(Sort.by(Sort.Direction.DESC, "createdAt")),
    UPDATEAT_ASC(Sort.by(Sort.Direction.ASC, "updateAt")),
    UPDATEAT_DESC(Sort.by(Sort.Direction.DESC, "updateAt"));

    private final Sort sortValue;
}
