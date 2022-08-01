package de.hochschule.augsburg.process;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.junit5.ProcessEngineExtension;

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
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("Process_Registration_Window", Map.of("registration_window_start", "2021-11-20T10:02:00", "registration_window_end", "2021-11-20T10:02:00"));
        assertThat(processInstance).isActive();

        assertThat(processInstance).isWaitingAt("Activity_Mail_Registration_Soon");

        complete(task());

        assertThat(processInstance).isWaitingAt("TimeEvent_RegistrationWindow_Start");

        execute(job());

        assertThat(processInstance).isWaitingAt("Activity_Registration_release");

        complete(task());

        assertThat(processInstance).isWaitingAt("Activity_Mail_Registration_start");

        complete(task());

        assertThat(processInstance).isWaitingAt("TimeEvent_RegistrationWindow_End");

        execute(job());

        runtimeService().signalEventReceived("Signal_RegisterWindow_Timeout");


        assertThat(processInstance).isWaitingAt("Activity_Registration_Lock");

        complete(task());

        assertThat(processInstance).isWaitingAt("TimEvent_Lock_Wait");

        execute(job());

        assertThat(processInstance).isWaitingAt("Activity_Registration_Algorithm");

        complete(task());

        runtimeService().signalEventReceived("Signal_Results_Available");

        assertThat(processInstance).isWaitingAt("TimEvent_Results_Wait");

        execute(job());

        assertThat(processInstance).isWaitingAt("Activity_Mail_Results_Available");

        complete(task());

        assertThat(processInstance).isEnded();

    }
}
