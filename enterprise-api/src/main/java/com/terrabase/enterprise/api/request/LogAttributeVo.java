package com.terrabase.enterprise.api.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogAttributeVo {
    private long sn;

    private String logType;

    private String username;

    private String operation;

    private String source;

    private String terminal;

    private String result;

    private String flag;

    private String paramType;

    private String detail;
}
