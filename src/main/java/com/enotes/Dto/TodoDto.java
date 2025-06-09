package com.enotes.Dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TodoDto {

    @Schema(hidden = true)
    private Integer id;

    private String title;

    private StatusDto status;

    @Schema(hidden = true)
    private Integer createdBy;

    @Schema(hidden = true)
    private Date createdOn;

    @Schema(hidden = true)
    private Integer updatedBy;

    @Schema(hidden = true)
    private Date updatedOn;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class StatusDto{
        private Integer id;
        private String name;
    }
}
