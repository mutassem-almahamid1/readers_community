package com.project.readers_community.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchBookRequest {
   private String name;
   private List<String> categoryIds;
   private Integer rate;
}
