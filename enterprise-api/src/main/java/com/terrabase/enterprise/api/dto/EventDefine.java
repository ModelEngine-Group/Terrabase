package com.terrabase.enterprise.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDefine {
    private @NotEmpty String eventId;

    private @NotEmpty String name;

    private String effect;

    private @NotEmpty String category;

    private String description;

    private String subjectType;

    private @NotEmpty @Pattern(
            regexp = "^(alter|event)$"
    ) String type;

    private @NotEmpty String parts;

    private String cause;

    private @Pattern(
            regexp = "^(warning|minor|major|critical)$"
    ) String severity;

    private String suggestion;

    private String version;

    private @Pattern(
            regexp = "^(zh|zh-cn|en|en-us)$"
    ) String language;

    private @Size(
            max = 256
    ) String defineMatchKey;
}




