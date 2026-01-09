package org.anonymous.af.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderEnum {
    MALE("男"), FEMALE("女"), OTHER("其他"), SECRET("秘密");

    private final String gender;
}
