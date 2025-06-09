package com.enotes.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotesRequest {

    private Integer id;

    private String title;

    private String description;

    private CategoryDto category;

}
