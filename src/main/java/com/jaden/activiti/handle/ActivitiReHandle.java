package com.jaden.activiti.handle;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

/**
 * Activiti 流程定义的操作
 *  前提：已部署流程定义即以下表 已经记录对应的流程定义信息
 *      act_ge_bytearray
 *
 *      act_re_deployment
 *      act_re_procdef
 *
 *  查询流程定义信息
 *  查询资源文件
 *      需求：
 *          1.从Activiti的act_ge_bytearray表中读取两个资源文件
 *          2.将两个资源文件保存到路径：   G:\Activiti7开发计划\Activiti7-day03\资料
 *
 *      思路：
 *          1.第一种方式使用actviti的api来实现
 *          2.第二种方式：其实就是原理层面，可以使用jdbc的对blob类型，clob类型数据的读取，并保存
 *              IO流转换，引入commons-io.jar包处理IO操作
 *
 *  删除流程定义
 *  流程定义挂起与激活 （全部流程实例挂起与激活）
 */
public class ActivitiReHandle {
    public static void main(String[] args){
        //1、默认方式创建ProcessEngine 流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取RepositoryService 资源服务对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3、流程定义查询器
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //4、通过流程定义的key 获取流程定义对象，
        ProcessDefinition processDefinition = processDefinitionQuery.processDefinitionKey("test").singleResult();
        //4、key对应多个流程定义时，list() 输出并根据流程定义的版本号进行排序
        /*List<ProcessDefinition> processDefinitions = processDefinitionQuery.processDefinitionKey("test")
                .orderByProcessDefinitionVersion().desc().list();*/
        //一、查询流程定义信息
        System.out.println("流程定义ID："+processDefinition.getId());
        System.out.println("流程定义名称："+processDefinition.getName());
        System.out.println("流程定义的Key："+processDefinition.getKey());
        System.out.println("流程定义的版本号："+processDefinition.getVersion());
        System.out.println("流程部署的ID:"+processDefinition.getDeploymentId());

        //二、查询资源文件
        //getResourceAsStream() 第一个参数: 部署id,第二个参数: 资源名称
        //processDefinition.getDiagramResourceName() 获取png图片资源的名称
        InputStream pngIs = repositoryService
                .getResourceAsStream(processDefinition.getDeploymentId(),processDefinition.getDiagramResourceName());
        //processDefinition.getResourceName() 获取bpmn文件的名称
        InputStream bpmnIs = repositoryService
                .getResourceAsStream(processDefinition.getDeploymentId(),processDefinition.getResourceName());
        OutputStream pngOs =null;
        OutputStream bpmnOs = null;
        try {
            pngOs =
                    new FileOutputStream("C:\\log\\"+processDefinition.getDiagramResourceName());
            bpmnOs =
                    new FileOutputStream("C:\\log\\"+processDefinition.getResourceName());
            //输入流，输出流的转换
            IOUtils.copy(pngIs,pngOs);
            IOUtils.copy(bpmnIs,bpmnOs);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //先关闭输出、后关闭输入流
            IOUtils.closeQuietly(pngOs);
            IOUtils.closeQuietly(bpmnOs);
            IOUtils.closeQuietly(pngIs);
            IOUtils.closeQuietly(bpmnIs);
        }

        //三、删除流程定义  参数为流程部署的id
        /*
         * 注意事项：
         *      deleteDeployment 方法默认第二个参数为 false 代表不级联删除（即当前流程有未完成审批流程时，删除失败）
         *      若强制删除，可显式定义第二个参数为true （即不管是否有未完成审批流程，先删除未完成的流程节点 再删除流程定义信息）
         *          repositoryService.deleteDeployment("1",true);
         */
        //repositoryService.deleteDeployment(processDefinition.getDeploymentId());
        //四、流程定义挂起与激活
        //获取流程定义的实例是否都为暂停状态
        boolean suspended = processDefinition.isSuspended();
        //获取流程定义Id
        String processDefinitionId = processDefinition.getId();
        if(suspended){
            //暂停时，激活流程定义
            repositoryService.activateProcessDefinitionById(processDefinitionId,true,null);
            System.out.println("流程定义："+processDefinitionId+"激活");
        }else{
            //没暂停时，挂起流程定义
            repositoryService.suspendProcessDefinitionById(processDefinitionId,true,null);
            System.out.println("流程定义："+processDefinitionId+"挂起");
        }
    }
}
