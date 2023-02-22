package ru.innopolis.process_automation.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("DelegateSendListToCompany")
public class DelegateSendListToCompany implements JavaDelegate {

  @Override
  public void execute(DelegateExecution execution) {

  }
}
