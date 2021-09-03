package com.jaden.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;

/**
 *   删除数据库  DROP DATABASE activiti;
 *  手动创建数据库  CREATE DATABASE activiti DEFAULT CHARACTER SET utf8;
 *  执行此类实现 activiti 数据库 中表自动创建
 */
public class ProcessEngineFirst
{
    public static void main( String[] args )
    {
        // 第一中方式创建 processEngine
        //ProcessEngine processEngine = generTables();
        // 第二种默认方式创建 processEngine
        //      前提条件：1.配置文件名称默认：activiti.cfg.xml   2.bean的id为默认：processEngineConfiguration
        //      注意：ProcessEngines 不是 ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
    }

    public static ProcessEngine generTables(){
        // 1、创建 ProcessEngineConfiguration
        // 注意：activiti.cfg.xml 数据源配置时加上：nullCatalogMeansCurrent=true
        /*  nullCatalogMeansCurrent=true，表示mysql默认当前数据库操作，在mysql-connector-java 5.xxx该参数默认为true，
            在6.xxx以上默认为false
         */
        // 第一个参数:配置文件名称
        // 第二个参数是processEngineConfiguration的bean的id,不指定时为默认值
        ProcessEngineConfiguration configuration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 2、通过ProcessEngineConfiguration创建ProcessEngine 工作流引擎 ，此时会创建数据库表
        ProcessEngine processEngine =
                configuration.buildProcessEngine();
        return processEngine;
    }
}
