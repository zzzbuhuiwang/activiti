package com.jaden.activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * 一个Activiti 网关使用过程
 *  包含网关   InclusiveGateway
 *      注意：
 *          具有排他网关和并行网并的一些共同点
 *          可以设置流程变量，当流程变量取值都成立时，此时若干个分支都可以执行
 *
 *      是排他网关和并行网关结合体。
 *      在分支时，需要判断条件，符合条件的分支，将会执行，所有分支执行完成后最终才进行汇聚。
 */
public class ActivitiInclusiveGatewayStep {
    public static void main(String[] args){
        //部署启动 只需执行一次
        //deploymentRun();

        //创建ProcessEngine 工作流流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //通过负责人assignee 查询待办任务
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("examineGateway")
                .taskAssignee("lingqu")
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
                .addClasspathResource("bpmn/examineGateway.bpmn")
                //.addClasspathResource("bpmn/test.png")
                .name("体检流程")
                .deploy();
        //第四步：启动一个流程实例（ProcessInstance）
        RuntimeService runtimeService = processEngine.getRuntimeService();

        String userType = "2";//代表管理者
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userType",userType);//流程变量赋值
        //流程变量赋值
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("examineGateway",map);
    }
}
