package com.xeridia.resource;

import lombok.*;

import java.math.BigInteger;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MintRequest {
    private int amount;
}
