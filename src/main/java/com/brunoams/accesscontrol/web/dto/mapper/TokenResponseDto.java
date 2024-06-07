package com.brunoams.accesscontrol.web.dto.mapper;

import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokenResponseDto {
    private String accessToken;
}
