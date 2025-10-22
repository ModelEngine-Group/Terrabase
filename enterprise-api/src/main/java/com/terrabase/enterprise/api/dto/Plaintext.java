package com.terrabase.enterprise.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Plaintext {
    @ToString.Exclude
    @JsonProperty("plain")
    private String plain;
}
