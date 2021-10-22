package com.selfserv.delegates;

import javax.inject.Inject;
import javax.inject.Named;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("SendEmailDelegate")
public class SendEmailDelegate implements JavaDelegate {

    public static final String CONFIG_INPUT = "configInput";
    public static final String USER_EMAIL = "userEmail";
    private final Logger log = LoggerFactory.getLogger(SendEmailDelegate.class);

    public void execute(DelegateExecution execution) throws Exception {
        //  String requestContent = execution.getVariable("CONFIG_INPUT").toString();
        String requestContent = execution.toString();
        //String emailID = execution.getVariable("USER_EMAIL").toString();
        // ...
        try {
            //SendEmailService.sendEmail(emailID, requestContent);
            log.debug("The pipeline request configuration is : {}", requestContent);
        } catch (Exception e) {
            throw new BpmnError("Error sending confirmation Mail" + e.getMessage());
        }
    }
}
