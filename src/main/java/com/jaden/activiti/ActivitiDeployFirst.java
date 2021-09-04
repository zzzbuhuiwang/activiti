package com.jaden.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * 流程定义的部署
 * activiti表有哪些？
 *      act_ge_bytearray   流程定义bpmn文件及png文件信息
 *
 *      act_re_deployment  部署信息
 *      act_re_procdef     流程定义信息
 */
public class ActivitiDeployFirst {

    public static void main(String[] args){
        //1.默认方式创建ProcessEngine 流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取RepositoryService 资源服务对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3.1、读取bpmn、png文件部署操作
        //Deployment deployment = fileDeployment(repositoryService);
        //3.2、读取zip压缩文件部署操作
        Deployment deployment = zipDeployment(repositoryService);
        //4.输出部署的名称和ID
        System.out.println(deployment.getName());
        System.out.println(deployment.getId());
    }

    public static Deployment fileDeployment(RepositoryService repositoryService){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/test.bpmn")
                .addClasspathResource("bpmn/test.png")
                .name("审批流程")
                .deploy();
        return deployment;
    }

    public static Deployment zipDeployment(RepositoryService repositoryService){
        //1、读取zip文件生成InputStream流对象
        InputStream is = ActivitiDeployFirst.class.getClassLoader().getResourceAsStream("bpmn/test.zip");
        //2、将 inputstream流转化为ZipInputStream流
        ZipInputStream zipInputStream = new ZipInputStream(is);

        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .name("审批流程")
                .deploy();
        return deployment;
    }
}
