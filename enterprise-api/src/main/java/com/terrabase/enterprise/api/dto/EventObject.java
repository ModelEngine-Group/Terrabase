package com.terrabase.enterprise.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventObject {
    protected String id;

    protected String eventId;

    protected String eventName;

    protected String eventType;

    protected String eventCategory;

    protected String deviceSn;

    protected String deviceId;

    protected String eventSubject;

    protected String severity;

    protected String eventSource;

    protected String parts;

    protected String clearType;

    protected String clearUser;

    protected String status;

    protected String confirmStatus;

    protected boolean blockingStatus;

    protected int occurCounts;

    protected Long clearTime;

    protected Long firstOccurTime;

    protected Long lastOccurTime;

    protected String readFlag;

    protected String eventDescription;

    protected String eventDescriptionArgs;

    protected String suggestion;

    protected String cause;

    protected Boolean sendToeService;

    protected String serialNumber;

    protected String devUrl;
}
