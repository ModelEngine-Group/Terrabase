package com.terrabase.enterprise.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseInfo {
    /**
     * status许可证状态
     * 0没有导入 1已经正常上传但是未激活 2已经激活 3已经注销
     */
    private String  status;

    /**
     * sbom信息
     */
    private List<LicenseInfoEx> sboms;
}




