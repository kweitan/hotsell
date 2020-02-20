alter database sell_food CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

--- 商品信息表 ---
create table `product_info` (
  `product_id` int unsigned not null auto_increment,
  `product_name` varchar(64) not null comment '商品名称',
  `product_price` decimal(8,2) not null comment '单价',
  `product_stock` int not null comment '库存',
  `product_description` varchar(64) comment '描述',
  `product_icon` varchar(256) comment '小图',
  `product_standard` varchar(64) comment '规格',
  `product_tips` varchar(32) comment '商品提示信息',
  `product_labels` varchar(64) comment '商品标签',
  `product_unit` varchar(64) comment '商品单元',
  `product_number` varchar(32) not null unique key comment '商品编码 前三位代表品类 0000 中间日期20191215132700',
  `product_status` tinyint(3) DEFAULT '0' COMMENT '商品状态,1-上架 0-下架',
  `sequence_id` int COMMENT '排序',
  `enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
  `create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
    primary key (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '商品表信息表';

--- 商品信息详细表 ----
create table `product_detail_info` (
  `product_detail_id` int unsigned not null auto_increment,
  `product_number` varchar(32) not null comment '商品编码 前三位代表品类 0000 中间日期20191215132700',
  `product_detail_icon` varchar(512) comment '商品明细图',
  `product_detail_field` varchar(512) comment '商品明细属性',
  `product_detail_description` text comment '商品详细描述 富文本格式',
  `enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
  `create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
    primary key (`product_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '商品信息详细表';

--- 商品类目表 ---
create table `product_category` (
	`category_id` int not null auto_increment,
	`category_name` varchar(64) not null comment '类目名字',
	`category_number` varchar(32) not null unique key comment '类目编码',
	`category_status` tinyint(3) DEFAULT '1' COMMENT '类目状态,1-可用 0-不可用',
	`category_icon` varchar(256) comment '小图',
	`category_url` varchar(128) comment '跳转页面',
	`belong_index` tinyint(3) DEFAULT '0' COMMENT '是否属于首页,1-是 0-否',
	`sequence_id` int COMMENT '排序',
	`enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
	`create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
	primary key (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '商品类目表';

--- 商品类目中间表 ---
create table `product_category_mid` (
	`product_category_mid_id` int not null auto_increment,
	`product_number` varchar(32) not null comment '商品编码 前三位代表品类 0000 中间日期20191215132700',
	`category_number` varchar(32) not null comment '类目编码',
	`enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
	`create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
	primary key (`product_category_mid_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '商品类目中间表';

--- 订单表 ---
create table `order_master` (
	`order_id` int unsigned not null auto_increment,
	`order_number` varchar(64) not null unique key comment '订单编码',
	`buyer_name` varchar(32) not null comment '买家名字',
	`buyer_phone` varchar(32) not null comment '买家电话',
	`buyer_address` varchar(128) not null comment '买家地址',
	`buyer_message` varchar(256) comment '买家留言',
	`buyer_openid` varchar(64) not null comment '买家微信openid',
	`order_amount` decimal(8,2) not null comment '订单总金额',
	`act_amount` decimal(8,2) comment '订单实际支付金额',
	`transaction_id` varchar(32) comment '微信支付订单号',
	`order_status` varchar(16) not null comment '订单状态，默认0-新下单（等待支付） 1-完结 2-取消 3-等待发货 4-等待收货 5-已收货 6-等待评价',
	`pay_status` varchar(16) not null comment '支付状态，默认0-等待支付 1-支付成功',
	`order_remark` varchar(64) comment '订单备注',
	`enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
	`create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
	primary key (`order_id`),
	key `idx_buyer_openid` (`buyer_openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '订单表';

--- 订单详情表 ---
create table `order_detail` (
	`detail_id` int unsigned not null auto_increment,
	`order_number` varchar(64) not null comment '订单编码',
	`product_number` varchar(32) not null comment '商品编码 前三位代表品类 0000 中间日期20191215132700',
	`product_name` varchar(64) not null comment '商品名称',
	`product_price` decimal(8,2) not null comment '商品价格',
	`product_quantity` int not null comment '商品数量',
	`product_icon` varchar(256) comment '商品小图',
	`product_standard` varchar(64) comment '商品规格',
	`product_labels` varchar(32) comment '商品标签',
	`product_unit` varchar(32) not null comment '商品单位',
	`enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
	`create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
	primary key (`detail_id`),
	key `idx_order_number` (`order_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '订单详情表';

--- 地址表 ---
create table `address_info` (
	`address_id` int unsigned not null auto_increment,
	`buyer_name` varchar(32) not null comment '买家名字',
	`buyer_phone` varchar(32) not null comment '买家电话',
	`buyer_address` varchar(128) not null comment '买家地址',
	`select_status` tinyint(3) DEFAULT '0' COMMENT '商品状态,0-普通状态 1-默认选择状态',
	`address_labels` varchar(32) not null comment '标签 家 公司 岳父母家 父母家',
	`openid` varchar(64) not null comment '微信openid',
	`enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
	`create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
	primary key (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '地址信息表';

--- 普通买家信息表 ----
create table `buyer_info` (
  `buyer_id` int unsigned not null auto_increment,
  `buyer_name` varchar(32),
  `buyer_password` varchar(64) DEFAULT 'tempabcdefg' not null,
  `avatar_url` varchar(128) comment '买家微信头像',
  `buyer_country` varchar(32) comment '买家国家',
  `buyer_province` varchar(32) comment '买家省份',
  `buyer_city` varchar(32) comment '买家城市',
  `buyer_gender` tinyint(3) comment '买家性别 1-男 0-女',
  `open_id` varchar(64) not null comment '微信openid',
  `union_id` varchar(64) comment '微信unionid',
  `session_key` varchar(64) comment '微信session_key',
  `enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
  `create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
    primary key (`buyer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '普通买家信息表';

---买家评论表 ----
create table `buyer_review` (
  `buyer_review_id` int unsigned not null auto_increment,
  `order_number` varchar(32) not null comment '订单编码 前三位代表品类 0000 中间日期20191215132700',
  `openid` varchar(64) not null comment '微信openid',
  `enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
  `create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
    primary key (`buyer_review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '买家评论表';

---商品评论表 ----
create table `product_review` (
  `product_review_id` int unsigned not null auto_increment,
  `product_review_level` tinyint(3) DEFAULT '1' COMMENT '评论级别,1-好评 0-差评',
  `product_review_content` varchar(256) DEFAULT '好吃,喜欢' comment '评论商品 商品编码-好评差评',
  `openid` varchar(64) not null comment '微信openid',
  `order_number` varchar(32) not null comment '订单编码 前三位代表品类 0000 中间日期20191215132700',
  `buyer_review_id` int unsigned,
  `product_number` varchar(32) not null comment '商品编码 前三位代表品类 0000 中间日期20191215132700',
  `person_name` varchar(32) comment '评论者昵称',
  `person_icon` varchar(128) comment '评论者图像',
  `enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
  `create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
    primary key (`product_review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '商品评论表';

--- 快递信息表 ---
create table `express_delivery` (
	`express_id` int unsigned not null auto_increment,
	`express_number` varchar(64) not null comment '快递编码',
	`express_cor_name` varchar(32)  comment '快递公司名称',
	`express_cor_abbreviation` varchar(16)  comment '快递公司简称',
	`tracking_number` varchar(64) not null comment '快递单号',
	`order_number` varchar(64) not null comment '订单编码',
	`enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
	`create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
	primary key (`express_id`),
	key `idx_express_number` (`express_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '快递信息表';

--- 我的转发表 ---
create table `my_forward` (
	`my_forward_id` int unsigned not null auto_increment,
	`product_number` varchar(64) not null comment '商品编码',
  `product_name` varchar(64) not null comment '商品名称',
  `product_price` decimal(8,2) not null comment '单价',
  `product_description` varchar(64) comment '描述',
  `product_icon` varchar(256) comment '小图',
  `openid` varchar(64) not null comment '微信openid',
	`enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
	`create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
	primary key (`my_forward_id`),
	key `idx_product_number` (`product_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '我的转发表';

---Banner表 ----
create table `wechat_banner` (
  `banner_id` int unsigned not null auto_increment,
  `banner_url` varchar(128) comment  'banner连接地址',
  `banner_name` varchar(64) comment 'banner名称',
  `banner_icon` varchar(128) comment 'banner图片',
  `banner_width` int comment 'banner宽度',
  `banner_height` int comment 'banner高度',
  `enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
  `create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
    primary key (`banner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment 'banner表';

create table `wechat_search_keyword` (
	`search_keyword_id` int not null auto_increment,
	`search_keyword_name` varchar(32) not null comment '商品搜索关键词',
	`search_keyword_number` varchar(32) not null comment '商品搜索关键词编码',
	`search_keyword_status` tinyint(3) DEFAULT '0' COMMENT '状态,1-是 0-否 是否设置关键词',
	`enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-是 0-否 ',
	`create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
	primary key (`search_keyword_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '商品搜索关键词表';

--- 商户信息表 ---
create table `merchant_info` (
  `merchant_id` int unsigned not null auto_increment,
  `merchant_number` varchar(16) not null unique key comment '商户编码',
  `merchant_name` varchar(64) not null comment '商户名称',
  `merchant_phone` varchar(16) not null comment '商户电话',
  `merchant_desc` varchar(128)  comment '商户描述',
  `merchant_logo` varchar(256)  comment '商户logo',
  `merchant_wechat_number` varchar(32)  comment '商户微信号',
  `merchant_pay_number` varchar(32)  comment '商户支付宝',
  `merchant_qq` varchar(32)  comment '商户qq',
  `merchant_qualification_desc` varchar(128)  comment '商户资质描述',
  `merchant_qualification_icon` varchar(128)  comment '商户资质证书',
  `user_id` int comment '商户所属用户ID',
	`enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
	`create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
	primary key (`merchant_id`),
	key `idx_express_number` (`express_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '商户信息表';


--- 退款单表 ---
create table `refund_order` (
  `refund_order_id` int unsigned not null auto_increment,
  `refund_number` varchar(64) not null unique key comment '退款单号',
  `order_number` varchar(64) not null comment '订单号',
  `total_fee` decimal(8,2) not null comment '订单金额',
  `refund_fee` decimal(8,2) not null comment '退款金额',
  `refund_fee_type` varchar(8) DEFAULT 'CNY' comment '货币种类',
  `refund_type` varchar(32) comment '退款方式',
  `refund_desc` varchar(128) comment '退款原因',
  `refund_status` varchar(16) comment '退款状态',
  `enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
  `create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
	primary key (`refund_order_id`),
	key `idx_buyer_openid` (`buyer_openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '退款单表';

--- 订单流水表 ---
--- NEW -> SUCCESS -> SHIPMENT -> FINISHED ->   ---
---                  REFUND -> CANCEL ---
create table `order_flow` (
  `order_flow_id` int unsigned not null auto_increment,
  `order_flow_number` varchar(64) not null unique key comment '订单流水号',
  `order_number` varchar(64) not null comment '订单号',
  `pre_flow_status` varchar(16) not null comment '上一个流水状态',
  `flow_status` varchar(16) not null comment '流水状态',
  `flow_remark` varchar(64) comment '流水备注',
  `enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
  `create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
	primary key (`order_flow_id`),
	key `idx_order_number` (`order_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment ' 订单流水表';
--- NEW -> SUCCESS -> SHIPMENT -> FINISHED -> ---
--用户             -> REFUND -> CANCEL     -> REFUND -> CANCEL


-- 卖家信息 --
create table `seller_info` (
  `seller_id` int unsigned not null auto_increment,
  `seller_name` varchar(32) not null comment '卖家名称不能为空',
  `seller_password` varchar(128) not null comment '卖家密码',
  `avatar_url` varchar(128) comment '卖家头像',
  `seller_country` varchar(32) comment '卖家国家',
  `seller_province` varchar(32) comment '卖家省份',
  `seller_city` varchar(32) comment '卖家城市',
  `seller_gender` tinyint(3) comment '卖家性别 1-男 0-女',
  `seller_number` varchar(64) not null unique key comment '卖家编号',
  `seller_phone` varchar(16) comment '卖家电话',
  `seller_qq` varchar(32) comment '卖家qq',
  `seller_email` varchar(64) comment '卖家邮箱',
  `seller_wechat_id` varchar(64) comment '卖家微信号',
  `seller_pay_id` varchar(64) comment '卖家支付宝',
  `enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
  `create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
    primary key (`seller_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '卖家信息表';

	-- 菜单信息 --
create table `sys_menu` (
  `sys_menu_id` int unsigned not null auto_increment,
  `sys_menu_name` varchar(32) not null comment '菜单名称',
  `sys_menu_number` varchar(32) not null comment '菜单编码',
  `sys_menu_url` varchar(128) comment '菜单路径',
  `sys_menu_level` tinyint(3) comment '菜单级别',
  `sys_menu_parent_id` int comment '父ID',
  `sys_menu_is_leaf` tinyint(3) comment '菜单叶子节点 1-叶子 0-非叶子',
  `sys_menu_icon` varchar(128) comment '菜单图标',
  `sys_menu_path` varchar(64) comment '菜单path',
  `sys_menu_remark` varchar(64) comment '备注',
  `version` int comment '版本号',
  `enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
  `create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
    primary key (`sys_menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '菜单信息表';

	-- 角色信息表 --
create table `sys_role` (
  `sys_role_id` int unsigned not null auto_increment,
  `sys_role_name` varchar(32) not null comment '角色名称',
  `sys_role_number` varchar(32) not null comment '角色编码',
  `sys_role_type` varchar(16) comment '角色类型',
  `sys_role_parent_id` int comment '父ID',
  `sys_role_remark` varchar(64) comment '备注',
  `version` int comment '版本号',
  `enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
  `create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
    primary key (`sys_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '角色信息表';