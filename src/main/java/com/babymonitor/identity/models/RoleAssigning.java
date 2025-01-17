package com.babymonitor.identity.models;

import java.util.UUID;

public class RoleAssigning {

    private UUID userID;
    private String roleName;

    public UUID getUserID() {
        return userID;
    };

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
