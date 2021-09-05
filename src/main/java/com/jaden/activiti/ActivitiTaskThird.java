package com.jaden.activiti;

import com.jaden.activiti.pojo.Holiday;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  第三步
 *  查询及执行任务
 *  执行任务时操作的表：
 *      act_hi_actinst
 *      act_hi_identitylink
 *      act_hi_taskinst
 *
 *      act_ru_identitylink
 *      act_ru_task
 *
 *  任务结束后，act_ru_ 表清空记录
 */
public class ActivitiTaskThird {
    public static void main(String[] args) {
        //1、默认方式创建ProcessEngine 流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、获取TaskService   任务管理对象
        TaskService taskService = processEngine.getTaskService();

        //3、通过流程定义的key及负责人assignee来 查询当前用户的任务列表
        /*List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("test")
                .taskAssignee("zhangsan")
                .list();*/
        // 若确负责人assignee 只有一条任务，可以使用 singleResult() 返回
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("test")
                .taskAssignee("shier")
                .singleResult();
        Holiday holiday = new Holiday();
        holiday.setNum(5F);
        Map<String,Object> mapHoliday = new HashMap<String, Object>();
        mapHoliday.put("holiday",holiday);
        //4、通过任务列表获取任务ID 完成任务处理
        if(task != null) {
            //taskService.complete(task.getId());
            //完成当前任务时设置 流程参数，必须在使用参数前设置
            taskService.complete(task.getId(), mapHoliday);
        }

        //任务列表的展示
        /*for(Task tasks :taskList){
            System.out.println("流程实例ID: "+tasks.getProcessInstanceId());
            System.out.println("任务ID: "+tasks.getId());
            System.out.println("任务负责人: "+tasks.getAssignee());
            System.out.println("任务名称: "+tasks.getName());
            //4、通过任务列表获取任务ID 完成任务处理
            taskService.complete(tasks.getId());
        }*/

    }
}
