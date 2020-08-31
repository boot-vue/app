package com.bootvue.controller.user;

import groovy.util.logging.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class Test {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    @org.junit.jupiter.api.Test
    public void test() throws InterruptedException {

        ProcessDefinition holiday = repositoryService.createProcessDefinitionQuery()
                .processDefinitionName("holiday01").singleResult();

        ProcessInstance instance = runtimeService.startProcessInstanceById(holiday.getId());

        Task zs = taskService.createTaskQuery().taskAssignee("zs").singleResult();

        taskService.complete(zs.getId());

        historyService.createHistoricActivityInstanceQuery()
                .processDefinitionId(holiday.getId())
                .processInstanceId(instance.getId())
                .list().forEach(e -> System.out.println(e.getId() + " " + e.getActivityType() + "  " + e.getActivityName() + "  " + e.getAssignee()));

        Thread.sleep(3000L);
    }
}