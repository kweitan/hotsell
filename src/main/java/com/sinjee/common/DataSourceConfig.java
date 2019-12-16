package com.sinjee.common;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author 小小极客
 * 时间 2019/12/15 23:44
 * @ClassName DataSourceConfig
 * 描述 DataSourceConfig
 **/
public class DataSourceConfig {
    @Autowired
    private PaginationInterceptor paginationInterceptor;

    @Primary
    @Bean(name = "helmetSqlSessionFactory")
    public SqlSessionFactory helmetSqlSessionFactory(@Qualifier("helmetDataSource") DataSource helmetDataSource)
            throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(helmetDataSource);
//		关键代码 设置 MyBatis-Plus 分页插件
        Interceptor[] plugins = {paginationInterceptor};
        sqlSessionFactory.setPlugins(plugins) ;
        return sqlSessionFactory.getObject();
    }
}
