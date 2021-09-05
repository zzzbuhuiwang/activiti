package com.jaden.activiti;

import com.jaden.activiti.pojo.Holiday;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

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
 *  启动流程实例 并添加业务标识id
 *      即 act_ru_execution 表 businessKey 存入业务标识id
 *  单个流程实例挂起与激活
 *  启动流程实例 动态设置assignee
 *      流程设计bpmn 中 assignee 值使用 UEL 表达式
 *          ${assignee1}、${assignee2}
 */
public class ActivitiStartInstanceSecond {
    public static void main(String[] args){
        //1、默认方式创建ProcessEngine 流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、获取RunService 流程运行对象
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String,Object> mapAssign = new HashMap<String,Object>();
        mapAssign.put("assignee1","yiyi");
        mapAssign.put("assignee2","shier");

        Holiday holiday = new Holiday();
        holiday.setNum(5F);
        //3、通过流程定义的key 创建流程实例
        //第一个参数：流程定义key
        //ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("test");
        //第二个参数：业务标识id
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("test", "100");
        //runtimeService.setVariables(processInstance.getId(),mapAssign);
        //当前流程实例设置流程变量（流程实例Id，流程变量名，流程变量值）
        runtimeService.setVariable(processInstance.getId(),"holiday",holiday);
        //第三个参数：动态assignee键值对
        //ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("test", "100", mapAssign);

        //4、输出流程实例的相关信息
        System.out.println("流程部署ID: "+processInstance.getDeploymentId());//null
        System.out.println("流程定义ID: "+processInstance.getProcessDefinitionId());//test:1:4
        System.out.println("流程实例ID: "+processInstance.getId());//2501
        System.out.println("活动ID: "+processInstance.getActivityId());//null

        //获取当前流程定义的实例是否为暂停状态
        boolean suspended = processInstance.isSuspended();
        //获取流程实例Id
        String processInstanceId = processInstance.getId();
        if(suspended){
            //暂停时，激活流程实例
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("流程实例："+processInstanceId+"激活");
        }else{
            //没暂停时，挂起流程实例
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("流程实例："+processInstanceId+"挂起");
        }
    }
}
