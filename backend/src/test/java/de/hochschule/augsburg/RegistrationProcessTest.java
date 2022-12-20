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
    @Deployment(resources = {"subject_registration.bpmn"})
    public void shouldExecuteHappyPath() {
        DelegateExpressions.registerJavaDelegateMock("registrationResultsMailing");
        // Given we create a new process instance
        final ProcessInstance processInstance = runtimeService()
                .startProcessInstanceByKey("Process_Register_Subject");

        assertThat(processInstance).isActive();


        runtimeService().signalEventReceived("SignalEvent_Results_Available");

        assertThat(processInstance).isWaitingAt("Send_Registration_Result");

        execute(job());

        assertThat(processInstance).isWaitingAt("Close_Registration");

        DelegateExpressions.registerJavaDelegateMock("closeRegistrationDelegate");

        execute(job());

        assertThat(processInstance).isEnded();
    }

    @Test
    @Deployment(resources = {"subject_registration.bpmn"})
    public void shouldCancelRegistration() {

        // Given we create a new process instance
        final ProcessInstance processInstance = runtimeService()
                .startProcessInstanceByKey("Process_Register_Subject");

        assertThat(processInstance).isActive();

        runtimeService().correlateMessage("cancel_registration");

        assertThat(processInstance).isEnded();
    }
}
