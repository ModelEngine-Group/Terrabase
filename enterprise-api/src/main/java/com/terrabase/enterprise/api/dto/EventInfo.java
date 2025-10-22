package com.terrabase.enterprise.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventInfo {
    private @Pattern(
            regexp = "^\\d{1,32}$",
            message = "id is a number"
    ) String id;

    private @Pattern(
            regexp = "^[A-Za-z0-9:_\\-=]{1,128}$",
            message = "serialNumber is an uuid"
    ) String serialNumber;

    private Integer syncNo;

    private @Length(
            max = 255,
            message = "eventName must less than 255"
    ) String eventName;

    private @NotEmpty @Pattern(
            regexp = "^(alter|event)$"
    ) String eventType;

    private @NotBlank @Length(
            max = 255,
            message = "evenSubject must less than 255"
    ) String eventSubject;

    private @NotBlank @Length(
            max = 255,
            message = "evenSubjectType must less than 255"
    ) String eventSubjectType;

    private @Length(
            max = 8000
    ) String eventDescription;

    private @Length(
            max = 64
    ) List<String> eventDescriptionArgs;

    private @NotBlank @Pattern(
            regexp = "warning|minor|major|critical"
    ) String severity;

    private @Length(
            max = 8000
    ) String effect;

    private @NotBlank @Length(
            max = 128,
            message = "evenCategory must less than 255"
    ) String eventCategory;

    @JsonAlias({"possibleCause", "cause"})
    private @Length(
            max = 8000
    ) String possibleCause;

    private @Length(
            max = 8000
    ) String suggestion;

    private @NotBlank @Pattern(
            regexp = "Uncleared|Cleared",
            message = "status must be Uncleared or Cleared"
    ) String status;

    private @Length(
            max = 32
    ) String firstOccurTime;

    private @Length(
            max = 32
    ) String clearTime;

    private @Length(
            max = 255,
            message = "eventSource must less than 255"
    ) String evenSource;

    private @Length(
            max = 255,
            message = "deviceSn must less than 255"
    ) String deviceSn;

    private @Length(
            max = 255,
            message = "deviceType must less than 255"
    ) String deviceType;

    private @Length(
            max = 255
    ) String devURL;

    private @NotBlank @Length(
            max = 64
    ) String parts;

    private @Pattern(
            regexp = "^(zh|zh-cn|en|en-us)$"
    ) String language;

    private int computerCategory;

    private boolean shouldDeleted;

    private String clearType;

    private String defineMatchKey;

    private String lastEventMatchKey;

    private Boolean shouldSaveDefine = true;

    private String deviceId;
}




