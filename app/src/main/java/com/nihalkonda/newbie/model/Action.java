package com.nihalkonda.newbie.model;

public class Action {

    String id;
    String name;
    String stepId;

    public Action() {
        this("","","");
    }

    public Action(String id, String name, String stepId) {
        this.id = id;
        this.name = name;
        this.stepId = stepId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", stepId='" + stepId + '\'' +
                '}';
    }
}
