package com.brunoams.accesscontrol.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Role {
    ADMIN("admin"),
    RH("rh"),
    WORKER("worker");

    private String role;
}




