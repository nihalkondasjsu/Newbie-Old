package com.nihalkonda.newbie.utils.filemanager;

import android.os.Environment;

import com.nihalkonda.newbie.model.ProcedureGroup;

import java.io.File;

public class FileManager {

    public ProcedureGroup loadRootProcedureGroup(String basePath){
        ProcedureGroup procedureGroup = new ProcedureGroup();

        File root = new File(Environment.getDataDirectory(),basePath==null?"procedures":basePath);

        if(root.exists()==false){
            root.mkdirs();
            return procedureGroup;
        }



        return procedureGroup;
    }

}
