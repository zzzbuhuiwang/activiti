package com.jaden.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * 第二步
 * 启动流程实例:
 *     前提是先已经完成流程定义的部署工作
 *
 *     背后影响的表：
 *          act_hi_actinst     已完成的活动信息
 *          act_hi_identitylink   参与者信息
 *          act_hi_procinst   流程实例
 *          act_hi_taskinst   任务实例
 *
 *          act_ru_execution   执行表
 *          act_ru_identitylink   参与者信息
 *          act_ru_task  任务
 */
public class ActivitiStartInstanceSecond {
    public static void main(String[] args){
        //1、默认方式创建ProcessEngine 流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、获取RunService 流程运行对象
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3、通过流程定义的key 创建流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("test");

        //4、输出流程实例的相关信息
        System.out.println("流程部署ID: "+processInstance.getDeploymentId());//null
        System.out.println("流程定义ID: "+processInstance.getProcessDefinitionId());//test:1:4
        System.out.println("流程实例ID: "+processInstance.getId());//2501
        System.out.println("活动ID: "+processInstance.getActivityId());//null
    }
}
