package com.easygame.easygame.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum SearchSort {
    TITLE_ASC(Sort.by(Sort.Direction.ASC, "title")),
    TITLE_DESC(Sort.by(Sort.Direction.DESC, "title")),
    CREATEAT_ASC(Sort.by(Sort.Direction.ASC, "createAt")),
    CREATEAT_DESC(Sort.by(Sort.Direction.DESC, "createAt")),
    UPDATEAT_ASC(Sort.by(Sort.Direction.ASC, "updateAt")),
    UPDATEAT_DESC(Sort.by(Sort.Direction.DESC, "updateAt"));

    private final Sort sortValue;
}
