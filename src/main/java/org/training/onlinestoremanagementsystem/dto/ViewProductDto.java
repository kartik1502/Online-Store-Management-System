package org.training.onlinestoremanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewProductDto {

    private int productId;

    @NotNull
    @Pattern(regexp = "[A-Za-z ]+", message = "Product name should caontain only alphabets")
    private String productName;

    @Min(1)
    private int quantity;

    @NotNull
    @Pattern(regexp = "[A-Za-z ]+", message = "Company name should caontain only alphabets")
    private String companyName;

    @Min(1)
    private double price;

}
