package com.jaden.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 定义任务监听类
 *  必须实现 org.activiti.engine.delegate.TaskListener 接口
 *
 * 流程定义时可设置对应节点监听器，包含触发的时机以及调用的监听类
 *  Create：任务创建后触发
 *  Assignment：任务分配后触发
 *  Delete：任务完成后触发
 *  All：所有事件发生都触发
 */
public class ActivitiTaskListener implements TaskListener {

    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("yiyi");
    }
}
