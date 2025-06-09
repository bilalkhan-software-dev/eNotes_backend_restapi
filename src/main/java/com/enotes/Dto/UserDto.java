package com.enotes.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

    @Schema(hidden = true)
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String mobileNo;

    private List<RoleDto> roles;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class RoleDto {

        private Integer id;

        @Schema(hidden = true)
        private String name;

    }
}
