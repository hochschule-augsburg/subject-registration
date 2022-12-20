package de.hochschule.augsburg;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.junit5.ProcessEngineExtension;
import org.camunda.bpm.extension.mockito.DelegateExpressions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

public class RegistrationProcessTest {

    @RegisterExtension
    ProcessEngineExtension extension = ProcessEngineExtension.builder()
            .build();

    @Test
    //@Disabled
    @Deployment(resources = {"subject_registration.bpmn"})
    public void shouldExecuteHappyPath() {
        // Given we create a new process instance
        final ProcessInstance processInstance = runtimeService()
                .startProcessInstanceByKey("Process_Register_Subject", Map.of("student", "max.mustermann"));

        assertThat(processInstance).isActive();

        assertThat(processInstance).isWaitingAt("UserTask_Subject_Update");

        complete(task());

        assertThat(processInstance).hasPassed("UserTask_Subject_Update");

        assertThat(processInstance).isWaitingAt("UserTask_Subject_Update");

        runtimeService().signalEventReceived("Signal_RegisterWindow_Timeout");

        assertThat(processInstance).isWaitingAt("SignalEvent_Results_Available");

        runtimeService().signalEventReceived("SignalEvent_Results_Available");

        assertThat(processInstance).isWaitingAt("UserTask_Confirm_Results");

        complete(task());

        assertThat(processInstance).isEnded();
    }

    @Test
    @Disabled
    @Deployment(resources = {"subject_registration.bpmn"})
    public void shouldCancelRegistration() {

        DelegateExpressions.registerJavaDelegateMock("cancellationDelegate")
                .onExecutionSetVariable("registrationCanceled", true);

        // Given we create a new process instance
        final ProcessInstance processInstance = runtimeService()
                .startProcessInstanceByKey("Process_Register_Subject", Map.of("student", "max.mustermann"));

        assertThat(processInstance).isActive();

        assertThat(processInstance).isWaitingAt("UserTask_Subject_Update");

        taskService().handleBpmnError(task().getId(), "registrationCancel");

        assertThat(processInstance).isWaitingAt("Activity_Cancel_Registration");

        execute(job());

        assertThat(processInstance).isEnded();

        assertThat(processInstance).hasVariables("registrationCanceled");
    }

    @Test
    @Disabled
    @Deployment(resources = {"subject_registration.bpmn"})
    public void shouldExecuteTimeout() {
        // Given we create a new process instance
        final ProcessInstance processInstance = runtimeService()
                .startProcessInstanceByKey("Process_Register_Subject", Map.of("student", "max.mustermann"));

        assertThat(processInstance).isActive();

        assertThat(processInstance).isWaitingAt("UserTask_Subject_Update");

        complete(task());

        assertThat(processInstance).hasPassed("UserTask_Subject_Update");

        assertThat(processInstance).isWaitingAt("UserTask_Subject_Update");

        runtimeService().signalEventReceived("Signal_RegisterWindow_Timeout");

        assertThat(processInstance).isWaitingAt("SignalEvent_Results_Available");

        runtimeService().signalEventReceived("SignalEvent_Results_Available");

        assertThat(processInstance).isWaitingAt("UserTask_Confirm_Results");

        execute(jobQuery().timers().singleResult());

        assertThat(processInstance).isEnded();
    }


}
