package com.nihalkonda.newbie.app;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nihalkonda.newbie.R;
import com.nihalkonda.newbie.model.Action;
import com.nihalkonda.newbie.model.Process;
import com.nihalkonda.newbie.model.Step;
import com.nihalkonda.newbie.modelmanager.ProcessManager;

import java.util.ArrayList;
import java.util.HashMap;

public class ProcessInstructionsActivity extends AppCompatActivity {

    ProcessManager processManager;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.actions)
    LinearLayout actionsLayout;

    View.OnClickListener actionClicked;

    HashMap<String,Action> actionButtonMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_instructions);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
        init();
        createProcess();
        showCurrentStep();
    }

    private void init() {
        processManager = new ProcessManager();
        actionButtonMap = new HashMap<>();
        actionClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(view.getTag().toString()+" is clicked");
                processManager.dispatchAction(actionButtonMap.get(view.getTag().toString()));
                showCurrentStep();
            }
        };
    }

    private void showCurrentStep() {

        Step currentStep = processManager.getCurrentStep();

        ArrayList<Action> actions = processManager.getCurrentActions();

        title.setText("Title : "+currentStep.getTitle());
        description.setText("Description : "+currentStep.getDescription());

        actionsLayout.removeAllViews();
        actionButtonMap.clear();
        for (Action action:actions){
            createActionButton(action);
        }

    }

    private void createActionButton(Action action) {
        Button button = new Button(this);
        button.setText(action.getName());
        button.setOnClickListener(actionClicked);
        button.setTextSize(20);
        button.setTag(action.getId());
        actionButtonMap.put(button.getTag().toString(),action);
        System.out.println(actionButtonMap);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,10,10,10);
        actionsLayout.addView(button, layoutParams);
    }

    private void createProcess() {

        Step stepA = new Step("A","Step A","Actions are 'B'");
        Step stepB = new Step("B","Step B","Actions are 'C'");
        Step stepC = new Step("C","Step C","Actions are 'E'");
        Step stepD = new Step("D","Step D","Actions are 'B'");
        Step stepE = new Step("E","Step E","Actions are 'D,F'");
        Step stepF = new Step("F","Step F","Actions are ''");

        //Action actionA = new Action("A","Open A","A");
        Action actionB = new Action("B","Open B","B");
        Action actionC = new Action("C","Open C","C");
        Action actionD = new Action("D","Open D","D");
        Action actionE = new Action("E","Open E","E");
        Action actionF = new Action("F","Open F","F");

        Process process = new Process();

        System.out.println("1 "+process.putStepActionAssociation(stepA,actionB));
        System.out.println("2 "+process.putStepActionAssociation(stepB,actionC));
        System.out.println("3 "+process.putStepActionAssociation(stepC,actionE));
        System.out.println("4 "+process.putStepActionAssociation(stepD,actionB));
        System.out.println("5 "+process.putStepActionAssociation(stepE,actionD));
        System.out.println("6 "+process.putStepActionAssociation(stepE,actionF));
        System.out.println("7 "+process.putStep(stepF));

        process.setHeadStepId("A");

        Gson gson = new Gson();

        System.out.println(gson.toJson(process));

        processManager.setProcess(process);

    }

    @OnClick(R.id.goBack)
    public void goBack(){
        processManager.goBack();
        showCurrentStep();
    }
}