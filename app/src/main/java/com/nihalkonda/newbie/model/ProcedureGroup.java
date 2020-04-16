package com.nihalkonda.newbie.model;

import java.util.ArrayList;

public class ProcedureGroup {

    String name;

    ArrayList<Procedure> prcedures;

    ArrayList<ProcedureGroup> procedureGroups;

    public ProcedureGroup() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Procedure> getPrcedures() {
        return prcedures;
    }

    public void setPrcedures(ArrayList<Procedure> prcedures) {
        this.prcedures = prcedures;
    }

    public ArrayList<ProcedureGroup> getProcedureGroups() {
        return procedureGroups;
    }

    public void setProcedureGroups(ArrayList<ProcedureGroup> procedureGroups) {
        this.procedureGroups = procedureGroups;
    }

}
