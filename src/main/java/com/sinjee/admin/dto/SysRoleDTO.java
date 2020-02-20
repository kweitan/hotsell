package com.sinjee.admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 创建时间 2020 - 02 -20
 *
 * @author kweitan
 */
@Getter
@Setter
public class SysRoleDTO extends BaseDTO implements Serializable {

    private Integer sysRoleId ;

    private String sysRoleName ;

    private String sysRoleNumber ;

    private String sysRoleType;

    private String sysRoleRemark;

    private Integer sysRoleParentId ;

    private String version;
}
