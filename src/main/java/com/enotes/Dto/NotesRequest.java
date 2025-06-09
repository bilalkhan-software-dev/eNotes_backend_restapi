package com.enotes.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotesRequest {

    private String title;

    private String description;

    private CategoryDto category;

}
