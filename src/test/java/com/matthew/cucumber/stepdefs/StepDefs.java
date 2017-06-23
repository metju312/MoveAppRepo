package com.matthew.cucumber.stepdefs;

import com.matthew.MoveApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = MoveApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
