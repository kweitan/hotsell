package com.sinjee.admin.entity;

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
public class SysMenu extends BaseEntity implements Serializable {

    private Integer sysMenuId ;

    /**菜单编码**/
    private String sysMenuNnumber;

    /**菜单名称**/
    private String sysMenuName;

    /**菜单路径**/
    private String sysMenuUrl;

    /**菜单级别**/
    private Integer sysMenuLevel;

    /**父ID**/
    private Integer sysMenuParentId;

    /**菜单 1-叶子节点 0-非叶子节点**/
    private Integer sysMenuIsLeaf;

    /**菜单图标**/
    private String sysMenuIcon;

    private String sysMenuPath;

    /**菜单备注**/
    private String sysMenuRemark;

    private String version;
}
