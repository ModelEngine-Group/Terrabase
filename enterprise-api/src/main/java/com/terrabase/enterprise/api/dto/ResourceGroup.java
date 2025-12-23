package com.terrabase.enterprise.api.dto;

public record ResourceGroup(String id, String parentId, String name, String description) {
    private static final String PUBLIC_GROUP_ID = "000";

    public static ResourceGroup buildPublicGroup() {
        return new ResourceGroup(PUBLIC_GROUP_ID, null, "publicGroup", "systemPublicGroup");
    }
}
