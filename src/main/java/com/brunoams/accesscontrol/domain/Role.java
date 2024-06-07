package com.brunoams.accesscontrol.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Role {
    ADMIN("admin"),
    RH("rh"),
    WORKER("worker");

    private String role;
}




