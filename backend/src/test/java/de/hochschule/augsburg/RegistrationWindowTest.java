package de.hochschule.augsburg;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.junit5.ProcessEngineExtension;

import org.camunda.bpm.extension.mockito.DelegateExpressions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

public class RegistrationWindowTest {
    @RegisterExtension

    ProcessEngineExtension extension = ProcessEngineExtension.builder().build();
    
    @Test
    @Deployment(resources = {"registration_window.bpmn"})
    public void processExecutionTest() {
        DelegateExpressions.registerJavaDelegateMock("registrationReminder");
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("Process_Registration_Window", Map.of("registration_window_start", "2021-11-20T10:02:00", "registration_window_end", "2021-11-20T10:02:00"));
        assertThat(processInstance).isActive();

        assertThat(processInstance).isWaitingAt("Activity_Mail_Registration_Soon");

        execute(job());

        assertThat(processInstance).isWaitingAt("TimeEvent_RegistrationWindow_Start");

        execute(job());

        assertThat(processInstance).isWaitingAt("Activity_Registration_release");

        complete(task());

        assertThat(processInstance).isWaitingAt("Activity_Mail_Registration_start");

        DelegateExpressions.registerJavaDelegateMock("registrationResultsMailing");


        execute(job());

        assertThat(processInstance).isWaitingAt("TimeEvent_RegistrationWindow_End");

        execute(job());

        assertThat(processInstance).isWaitingAt("Activity_Registration_Lock");

        DelegateExpressions.registerJavaDelegateMock("lockRegistrationWindow");

        execute(job());

        assertThat(processInstance).isWaitingAt("Activity_Registration_Algorithm");

        complete(task());

        runtimeService().signalEventReceived("Signal_Results_Available");

        assertThat(processInstance).isWaitingAt("TimEvent_Results_Wait");

        execute(job());

        DelegateExpressions.registerJavaDelegateMock("registrationResultsMailing");

        assertThat(processInstance).isWaitingAt("Activity_Mail_Results_Available");

        execute(job());

        assertThat(processInstance).isEnded();

    }
}
