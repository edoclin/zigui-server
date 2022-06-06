package cn.cug.zigui.vo;

import lombok.Data;

import java.util.List;

@Data
public class VPostRole {
    String roleName;
    String roleDesc;
    List<String> permissionIds;

}
