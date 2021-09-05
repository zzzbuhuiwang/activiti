package com.jaden.activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;

/**
 * 一个简单、完整的Activiti 使用过程
 */
public class ActivitiASimpleStep {
    public static void main(String[] args){
        //第一步：创建ProcessEngine 工作流流程引擎对象
        //          （默认创建、ProcessEngineConfiguration自定义创建）
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //第二步：流程定义
        //          bmpn/test.bpmn  及生成 test.png
        //第三步：流程定义部署
        //          两种方式（ClasspathRsource文件部署、ZipInputStream压缩输入流部署）
        RepositoryService repositoryService = processEngine.getRepositoryService();

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/test.bpmn")
                .addClasspathResource("bpmn/test.png")
                .name("审批流程")
                .deploy();
        //第四步：启动一个流程实例（ProcessInstance）
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("test");
        //第五步：用户查询待办任务(Task)
        //          若确定负责人assignee 只有一条任务，可以使用 singleResult() 返回 Task 后办理
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("test")
                .taskAssignee("zhangsan")
                .list();
        //第五步扩展：候选人查询、拾取、归还、交接
        List<Task> candidateTaskList = taskService.createTaskQuery()
                .processDefinitionKey("test")
                .taskCandidateUser("候选人xxx")
                .list();
        for(Task task : candidateTaskList){
            if(task!=null){
                taskService.claim(task.getId(),"候选人xxx");
                //任务负责人设置为null 即 归还任务
                taskService.setAssignee(task.getId(), null);
                //任务负责人设置为交接人xxx 即 交接任务
                //  交接人xxx 要在交接前确定是否为候选人，是候选人再交接
                taskService.setAssignee(task.getId(), "交接人xxx");
                System.out.println("任务拾取完毕!");
            }
        }

        //第六步：用户办理任务
        for(Task task :taskList){
            if(task != null) {
                taskService.complete(task.getId());
            }
        }
        //第七步：流程结束。工作流流程中用户循序执行第五步、第六步，直到没有下一个任务/结点

    }
}
