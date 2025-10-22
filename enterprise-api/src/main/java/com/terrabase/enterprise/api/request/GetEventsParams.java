package com.terrabase.enterprise.api.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetEventsParams {
    private @Size(
            max = 128
    ) String alarmName;

    private @Positive Long startTime;

    private @Positive Long endTime;

    private @NotBlank @Pattern(
            regexp = "warning|minor|major|critical"
    ) String severity;

    private @Pattern(
            regexp = "^(?! )[_\\- .a-zA-Z0-9]+(?<! )$"
    ) @Size(
            max = 64
    ) String category;

    private @NotBlank @Pattern(
            regexp = "Uncleared|Cleared"
    ) String eventType;

    private @NotBlank @Pattern(
            regexp = "first_occur_time asc|clear_timt asc|first_occur_time desc|clear_time desc|last_occur_time desc|last_occur_time asc"
    ) String orderBy;

    private @NotBlank @Pattern(
            regexp = "en|zh|zh-cn|en-us"
    ) String language;

    private @Pattern(
            regexp = "^(?! )[_\\- .a-zA-Z0-9]+(?<! )$"
    ) @Size(
            max = 64
    ) String deviceType;

    private Boolean block = false;

    private @Positive Integer startSerialNo;

    private @Positive Integer endSerialNo;

    private @Size(
            max = 256
    ) String subject;

    private @Size(
            max = 256
    ) String subjectLikeName;

    private @Pattern(
            regexp = "^(?! )[_\\- .a-zA-Z0-9]+(?<! )$"
    ) @Size(
            max = 64
    ) String parts;

    private Boolean sendToeService;

    private String deviceId;

    private @Positive Integer startLine;

    private @Positive Integer endLine;

    private @Size(
            max = 64
    ) String eventId;

    private @Size(
            max = 16
    ) List<@Length(
                max = 64
        )
                String> categorys;

    private String cabinetId;

    private @Size(
            max = 512
    ) String devUrl;

    private @Min(1L) Integer pageNum;

    private @Max(2000L) Integer pageSize;

    private boolean specifyStartLine;
}
