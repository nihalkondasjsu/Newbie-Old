package com.nihalkonda.newbie.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Process implements Serializable {

    String headStepId;

    HashMap<String, Step> stepMap;

    HashMap<String, Action> actionMap;

    HashMap<String, ArrayList<String>> stepActionMap;

    public Process() {
        stepMap = new HashMap<String, Step>();
        actionMap = new HashMap<String, Action>();
        stepActionMap = new HashMap<String, ArrayList<String>>();
    }

    public String getHeadStepId() {
        System.out.println("getHeadStepId "+ headStepId);
        return headStepId;
    }

    public void setHeadStepId(String headStepId) {
        System.out.println("setHeadStepId "+ headStepId);
        this.headStepId = headStepId;
    }

    public Step getStepById(String id){
        System.out.println("getStepById "+ id +" > "+stepMap.get(id));
        return stepMap.get(id);
    }

    public Step putStep(Step step){
        System.out.println("putStep "+step);
        if(stepActionMap.containsKey(step.getId()) == false){
            stepActionMap.put(step.getId(),new ArrayList<String>());
        }
        return stepMap.put(step.getId(),step);
    }

    public Action getActionById(String id){
        System.out.println("getActionById "+id+" > "+actionMap.get(id));
        return actionMap.get(id);
    }

    public Action putAction(Action action){
        System.out.println("putAction "+action);
        return actionMap.put(action.getId(),action);
    }

    public boolean putStepActionAssociation(Step step, Action action){
        System.out.println("putStepActionAssociation "+step+" "+action);
        if(stepMap.containsKey(step.getId()) == false){
            putStep(step);
        }
        if(actionMap.containsKey(action.getId()) == false){
            putAction(action);
        }
        if(stepActionMap.containsKey(step.getId()) == false){
            stepActionMap.put(step.getId(),new ArrayList<String>());
        }
        return stepActionMap.get(step.getId()).add(action.getId());
    }

    public ArrayList<String> getActionsOfStep(Step step){
        System.out.println("getActionsOfStep "+step+" "+stepActionMap.get(step.getId()));
        return stepActionMap.get(step.getId());
    }

}
