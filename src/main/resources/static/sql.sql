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
	`buyer_openid` varchar(64) not null comment '买家微信openid',
	`order_amount` decimal(8,2) not null comment '订单总金额',
	`order_status` tinyint(3) not null default '0' comment '订单状态，默认0-新下单（等待支付） 1-完结 2-取消 3-等待发货 4-等待收货 5-已收货 6-等待评价',
	`pay_status` tinyint(3) not null default '0' comment '支付状态，默认0-等待支付 1-支付成功',
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
  `buyer_name` varchar(32) not null,
  `buyer_password` varchar(128) not null,
  `buyer_icon` varchar(128) not null, '买家微信头像',
  `openid` varchar(64) not null comment '微信openid',
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
  `buyer_review_level` varchar(64) DEFAULT '1' COMMENT '评论级别,1-好评 0-差评',
  `buyer_review_product` varchar(512) comment '评论商品',
  `order_number` varchar(32) not null comment '订单编码 前三位代表品类 0000 中间日期20191215132700',
  `openid` varchar(64) not null comment '微信openid',
  `buyer_review_content` varchar(512) comment '评论内容',
  `enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
  `create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
    primary key (`buyer_review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '买家评论表';

--- 快递信息表 ---
create table `express_delivery` (
	`express_id` int unsigned not null auto_increment,
	`express_number` varchar(64) not null comment '快递编码',
	`express_cor_name` varchar(32)  comment '快递公司名称',
	`express_cor_abbreviation` varchar(16)  comment '快递公司简称',
	`tracking_number` varchar(64) comment '快递单号',
	`order_number` varchar(64) not null comment '订单编码',
	`enable_flag` tinyint(3) DEFAULT '1' COMMENT '状态,1-可用 0-不可用',
	`create_time` timestamp not null comment '创建时间',
  `creator` varchar(32) not null comment '创建者',
  `update_time` timestamp not null comment '修改时间',
  `updater` varchar(32) not null comment '更新者',
	primary key (`express_id`),
	key `idx_express_number` (`express_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 comment '快递信息表';


