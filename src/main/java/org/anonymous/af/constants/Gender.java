package org.anonymous.af.constants;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("男"), FEMALE("女"), OTHER("其他"), SECRET("秘密");

    private final String value;

    Gender(String value) {
        this.value = value;
    }
}
