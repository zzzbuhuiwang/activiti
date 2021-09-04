package com.jaden.activiti.handle;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;

import java.util.List;

/**
 * Activiti 历史流程的操作
 *      act_hi_*相关的表
 */
public class ActivitiHiHandle {

    public static void main(String[] args){
        //1、默认方式创建ProcessEngine 流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取HistoryService 历史流程对象
        HistoryService historyService = processEngine.getHistoryService();
        //3、历史流程实例查询器
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        //4、通过流程实例的id 获取历史流程实例list() 输出并根据StartTime进行排序
        List<HistoricActivityInstance> instances = historicActivityInstanceQuery.processInstanceId("5")
                .orderByHistoricActivityInstanceStartTime().asc().list();

        //5、查询历史流程结果
        for (HistoricActivityInstance instance :instances){
            System.out.println(instance.getActivityId());
            System.out.println(instance.getActivityName());
            System.out.println(instance.getProcessDefinitionId());
            System.out.println(instance.getProcessInstanceId());
        }
    }
}
