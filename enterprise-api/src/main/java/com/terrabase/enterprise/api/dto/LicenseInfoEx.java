package com.terrabase.enterprise.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseInfoEx {
    private String name;

    private String total;

    private String unit;
}
