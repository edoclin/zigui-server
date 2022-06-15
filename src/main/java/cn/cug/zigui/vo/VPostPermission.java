package cn.cug.zigui.vo;

import lombok.Data;

@Data
public class VPostPermission {
    String permissionId;
    String routeId;
    String permissionName;
    String permissionDesc;
    PermissionJson permissions;
}
