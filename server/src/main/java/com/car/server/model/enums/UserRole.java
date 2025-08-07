package com.car.server.model.enums;

public enum UserRole {
    OWNER("owner"),
    USER("user");

    private final String roleName;

    // 构造函数
    UserRole(String roleName) {
        this.roleName = roleName;
    }

    // 获取角色名称
    public String getRoleName() {
        return roleName;
    }


}
