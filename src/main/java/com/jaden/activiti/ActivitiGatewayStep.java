package com.jaden.activiti;

import com.jaden.activiti.pojo.Holiday;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * 一个Activiti 网关使用过程
 *  排他网关（也叫异或（XOR）网关   ExclusiveGateway
 *      注意：
 *          排他网关只会选择一个为true的分支执行。(即使有两个分支条件都为true，排他网关也会只选择一条分支去执行（id值小的执行）)
 *      缺点：
 *          如果条件都不满足，不使用排他网关，流程就结束了(是异常结束)。
 *          如果条件都不满足，使用排他网关则系统抛出异常。
 *              org.activiti.engine.ActivitiException: No outgoing sequence flow of the exclusive gateway
 *
 *  并行网关    ParallelGateway
 *      同时具备 fork分支、join汇聚功能
 *      注意：
 *          并行网关不会解析条件。 即使顺序流中定义了条件，也会被忽略
 *          并行网关在业务应用中常用于会签任务，会签任务即多个参与者共同办理的任务。
 *          所有分支到达汇聚结点，并行网关执行完成。
 */
public class ActivitiGatewayStep {
    public static void main(String[] args){
        //部署启动 只需执行一次
        //();

        //创建ProcessEngine 工作流流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //通过负责人assignee 查询待办任务
        // yiyi ——> eryi ——> 分支
        //      天数大于三天  和天数大于一天 两个条件都满足，走id值小的分支 erer  ——>  renshi ——> 并行
        //          caiwu、xingzheng
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holidayGateway")
                .taskAssignee("xingzheng")
                .singleResult();
        //用户办理任务
        if(task != null) {
            taskService.complete(task.getId());
        }
    }

    public static void deploymentRun(){
        //第一步：创建ProcessEngine 工作流流程引擎对象
        //          （默认创建、ProcessEngineConfiguration自定义创建）
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //第二步：流程定义
        //          bmpn/test.bpmn  及生成 test.png
        //第三步：流程定义部署
        //          两种方式（ClasspathRsource文件部署、ZipInputStream压缩输入流部署）
        RepositoryService repositoryService = processEngine.getRepositoryService();

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/holidayGateway.bpmn")
                //.addClasspathResource("bpmn/test.png")
                .name("审批流程")
                .deploy();
        //第四步：启动一个流程实例（ProcessInstance）
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Holiday holiday = new Holiday();
        holiday.setNum(5F);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("holiday",holiday);
        //流程变量赋值
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayGateway",map);
    }
}
