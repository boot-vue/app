package com.bootvue.controller.user;

import groovy.util.logging.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class Test {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @org.junit.jupiter.api.Test
    public void test() {

        ProcessDefinition holiday = repositoryService.createProcessDefinitionQuery().processDefinitionName("holiday01").singleResult();

        //ProcessInstance processInstance = runtimeService.startProcessInstanceById(holiday.getId());

        // System.out.println(processInstance.toString());

        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionId(holiday.getId())
                .taskAssignee("zs").list();

        taskList.forEach(e -> {
            System.out.println(e.toString());
        });
    }
}