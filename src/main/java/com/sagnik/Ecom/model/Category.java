package com.sagnik.Ecom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Long categoryId;

    private String categoryName;

   private List<Product> products;
}
