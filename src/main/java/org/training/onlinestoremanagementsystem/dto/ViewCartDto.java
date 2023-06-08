package org.training.onlinestoremanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewCartDto {

    private String firstName;

    private String lastName;

    private String contactNumber;

    private List<ProductQuantityDto> productQuantityDtos;

    private double totalPrice;

    private String cartStatus;
}
