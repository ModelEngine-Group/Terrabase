package com.terrabase.enterprise.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CertCollectInfo {
    private String productName;

    private long issueTime;

    private long expirationTime;

    private String issuer;

    private String subject;

    private String serialNumber;

    private String certType;

    private String status;

    private Integer alertBeforeExpirationDays;

    private String certName;

    private String productVersion;

    private String patchVersion;

    private String deviceEsn;
}




