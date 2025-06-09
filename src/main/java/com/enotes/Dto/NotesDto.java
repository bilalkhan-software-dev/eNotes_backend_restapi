package com.enotes.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Date;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotesDto {

    @Schema(hidden = true)
    private Integer id;

    private String title;

    private String description;

    private CategoryDto category;

    private FilesDto fileDetails;

    private Boolean isDeleted;


    private LocalDateTime deletedOn;

    @Schema(hidden = true)
    private Integer createdBy;

    @Schema(hidden = true)
    private Date createdOn;

    @Schema(hidden = true)
    private Integer updatedBy;

    @Schema(hidden = true)
    private Date updatedOn;


    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryDto{
        private Integer id;

        private String name;

    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FilesDto{
        private Integer id;

        private String originalFileName;

        private String displayFileName;

    }


}
