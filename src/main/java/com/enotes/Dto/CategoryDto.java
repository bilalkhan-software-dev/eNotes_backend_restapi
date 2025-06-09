package com.enotes.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.util.Date;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @Schema(hidden = true)
    private Integer id;

    private String name;

    private String description;

    private Boolean isActive;

    @Schema(hidden = true)
    private Boolean isDeleted;

    @Schema(hidden = true)
    private Integer createdBy;

    @Schema(hidden = true)
    private Date createdOn;

    @Schema(hidden = true)
    private Integer updatedBy;

    @Schema(hidden = true)
    private Date updatedOn;

}
