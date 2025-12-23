package com.terrabase.enterprise.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto {
    private String userName;

    private String userId;

    private String role;

    private List<ResourceGroup> resourceGroups;
}
