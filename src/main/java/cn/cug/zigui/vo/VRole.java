package cn.cug.zigui.vo;

import lombok.Data;

import java.util.List;

@Data
public class VRole {
    Integer roleId;
    String roleName;
    String roleDesc;
    String createdTime;
    List<String> permissionIds;
}
