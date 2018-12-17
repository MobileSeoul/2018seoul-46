package me.quiz_together.root.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.model.broadcast.BroadcastType;
import me.quiz_together.root.model.broadcast.GiftType;
import me.quiz_together.root.model.question.CategoryType;
import me.quiz_together.root.model.question.Question;
import me.quiz_together.root.model.question.QuestionProp;
import me.quiz_together.root.model.user.User;
import me.quiz_together.root.model.user.UserDevice;
import me.quiz_together.root.model.user.UserFollower;
import me.quiz_together.root.model.user.UserInventory;
import me.quiz_together.root.model.user.UserStatus;
import me.quiz_together.root.support.typehandler.DateLongTypeHandler;
import me.quiz_together.root.support.typehandler.QuestionPropTypeHandler;
import me.quiz_together.root.support.typehandler.ValueEnumTypeHandler;

@Configuration
@EnableTransactionManagement
public class MybatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        sqlSessionFactoryBean.setConfiguration(mybatisConfiguration());
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    private org.apache.ibatis.session.Configuration mybatisConfiguration() {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setAutoMappingBehavior(AutoMappingBehavior.FULL);

        //typeAlias
        TypeAliasRegistry typeAliasRegistry = configuration.getTypeAliasRegistry();
        typeAliasRegistry.registerAlias("User", User.class);
        typeAliasRegistry.registerAlias("UserDevice", UserDevice.class);
        typeAliasRegistry.registerAlias("Broadcast", Broadcast.class);
        typeAliasRegistry.registerAlias("Question", Question.class);
        typeAliasRegistry.registerAlias("UserFollower", UserFollower.class);
        typeAliasRegistry.registerAlias("UserInventory", UserInventory.class);

        //typeHandler
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        typeHandlerRegistry.register(DateLongTypeHandler.class);
        typeHandlerRegistry.register(UserStatus.class, ValueEnumTypeHandler.class);
        typeHandlerRegistry.register(BroadcastStatus.class, ValueEnumTypeHandler.class);
        typeHandlerRegistry.register(BroadcastType.class, ValueEnumTypeHandler.class);
        typeHandlerRegistry.register(GiftType.class, ValueEnumTypeHandler.class);
        typeHandlerRegistry.register(CategoryType.class, ValueEnumTypeHandler.class);
        typeHandlerRegistry.register(QuestionProp.class, QuestionPropTypeHandler.class);


        return configuration;
    }
}
