package com.enotes.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class NotesResponse {

   List<NotesDto> notesDtoList;

   private Long totalElements;
   private Integer totalPages;
   private Integer pageSize;
   private Integer pageNo;
   private Boolean isFirst;
   private Boolean isLast;
   private String sortBy;
   private String direction;



}
