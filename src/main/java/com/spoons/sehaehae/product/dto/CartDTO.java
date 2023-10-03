package com.spoons.sehaehae.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CartDTO {
    private int amount1;
    private ProductDTO productCode1;
    private int member1;
    private String useEco1;
    private String usePremium1;

}
