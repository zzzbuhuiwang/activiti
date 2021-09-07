package com.jaden.activiti.spring;

import org.activiti.engine.RepositoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 通过 Spring与Junit整合测试  验证（Activiti 与 Spring 整合）是否成功
 *      成功标志：
 *          activiti库中是否重新创建相关的表
 *          输出一个 Repositoryervice资源服务对象
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:activiti-spring.cfg.xml")
public class ActivitiSpringTest {
    @Autowired
    private RepositoryService repositoryService;

    @Test
    public void testRepositoryService(){
        System.out.println("部署对象:"+repositoryService);
    }

}
