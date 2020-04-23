package com.nihalkonda.newbie.app;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nihalkonda.newbie.R;
import com.nihalkonda.newbie.model.Action;
import com.nihalkonda.newbie.model.Process;
import com.nihalkonda.newbie.model.Step;
import com.nihalkonda.newbie.modelmanager.ProcessManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProcessInstructionsActivity extends AppCompatActivity {

    public static final String EXTRA_PROCESS_ENCODED="EXTRA_PROCESS_ENCODED";

    ProcessManager processManager;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.nextButton)
    ImageButton nextButton;

    @BindView(R.id.goBack)
    ImageButton backButton;

    TextView overlayTitle;

    TextView overlayDescription;

    View.OnClickListener overlayActionClicked;

    ArrayList<Action> actions;

    HashMap<Integer,Action> menuItemActionMap;

    WindowManager windowManager;

    View mTestView;

    PopupMenu.OnMenuItemClickListener popupMenuClicked;
    private ImageButton overlayNextButton,overlayBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_instructions);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        ButterKnife.setDebug(true);
        try {
            init();
            createProcess();
            openOverlay(this);
            showCurrentStep();
        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideOverlay();
    }

    private void init() {
        processManager = new ProcessManager();
        actions = new ArrayList<>();
        menuItemActionMap = new HashMap<>();
        popupMenuClicked = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                dispatchAction(menuItemActionMap.get(menuItem.getItemId()));
                return false;
            }
        };
        overlayActionClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(actions.size()==1){
                    dispatchAction(actions.get(0));
                    return;
                }
                menuItemActionMap.clear();
                PopupMenu menu = new PopupMenu(ProcessInstructionsActivity.this,view);
                int temp = 0;
                for(Action action : actions){
                    menuItemActionMap.put(
                            menu.getMenu().add(
                                    0,temp++,0,
                                    action.getName()
                            ).getItemId(),
                            action
                    );
                }
                menu.setOnMenuItemClickListener(popupMenuClicked);
                menu.show();
            }
        };
        nextButton.setOnClickListener(overlayActionClicked);
        title.setTypeface(Typeface.createFromAsset(getAssets(),
                "kellyslab.ttf"));
        description.setTypeface(Typeface.createFromAsset(getAssets(),
                "ralewaym.ttf"));
    }

    private void dispatchAction(Action action){
        processManager.dispatchAction(action);
        showCurrentStep();
    }

    private void showCurrentStep() {

        Step currentStep = processManager.getCurrentStep();

        actions = processManager.getCurrentActions();

        title.setText(currentStep.getTitle());
        overlayTitle.setText(currentStep.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            overlayTitle.setTooltipText(currentStep.getTitle());
        }
        description.setText(currentStep.getDescription());
        overlayDescription.setText(currentStep.getDescription());

        overlayNextButton.setVisibility(actions.size()>0? View.VISIBLE : View.INVISIBLE);
        nextButton.setVisibility(actions.size()>0? View.VISIBLE : View.INVISIBLE);
        overlayBackButton.setVisibility(processManager.hasBack()? View.VISIBLE : View.INVISIBLE);
        backButton.setVisibility(processManager.hasBack()? View.VISIBLE : View.INVISIBLE);
    }

//    private void createActionButton(Action action) {
//        Button button = new Button(this);
//        button.setText(action.getName());
//        button.setOnClickListener(actionClicked);
//        button.setTextSize(20);
//        button.setTag(action.getId());
//        actionMap.put(button.getTag().toString(),action);
//        System.out.println(actionMap);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(10,10,10,10);
//        actionsLayout.addView(button, layoutParams);
//    }

    private void createProcess() {

        Gson gson = new Gson();

        Process process = createDummyProcess();

        String encodedProcess = getIntent().getStringExtra(EXTRA_PROCESS_ENCODED);

        if(encodedProcess!=null){
            String decodedProcess = new String(Base64.decode(encodedProcess,Base64.DEFAULT));
            process = gson.fromJson(decodedProcess, Process.class);
        }

        System.out.println(gson.toJson(process));

        processManager.setProcess(process);

    }

    @Override
    protected void onDestroy() {
        windowManager.removeView(mTestView);
        super.onDestroy();
    }

    private Process createDummyProcess() {
        Step stepOpenW = new Step("OpenWA","Whatsapp","Click the green icon with phone");
        Step stepOpenChat = new Step("OpenChat","Chat Tab","Click the chat tab in the top");
        Step stepOpenIndvChat = new Step("OpenIndvChat","Individual Chat","Click on the contact in the list");
        Step stepOpenSettings = new Step("OpenSettings","Settings Tab","Click the settings tab in the top");
        Step stepOpenProfile = new Step("OpenProfile","Profile Page","Click the profile button in the list");
        Step stepOpenCamera = new Step("OpenCamera","Capture an Image","Click the big round white button in bottom center");
        Step stepCapturePreview = new Step("CapturePreview","Preview","See if the image is ok.");

        Action actionOpenChat = new Action(stepOpenChat.getId()+"action1","Open Chat Tab",stepOpenChat.getId());
        Action actionOpenIndvChat = new Action(stepOpenIndvChat.getId()+"action1","Open Individual Chat",stepOpenIndvChat.getId());
        Action actionCaptureImage = new Action(stepOpenCamera.getId()+"action1","Capture Image",stepOpenCamera.getId());
        Action actionCapturePreview = new Action(stepCapturePreview.getId()+"action1","Examine Preview",stepCapturePreview.getId());
        Action actionOpenSettings = new Action(stepOpenSettings.getId()+"action1","Open Settings Tab",stepOpenSettings.getId());
        Action actionOpenProfile = new Action(stepOpenProfile.getId()+"action1","Click Profile ListItem",stepOpenProfile.getId());
        Action actionCaptureProfilePicture = new Action(stepOpenCamera.getId()+"action2","Capture Profile Picture",stepOpenCamera.getId());

        Process process = new Process();

        process.setHeadStepId(stepOpenW.getId());

        process.putStepActionAssociation(stepOpenW,actionOpenChat);
        process.putStepActionAssociation(stepOpenW,actionOpenSettings);
        process.putStepActionAssociation(stepOpenChat,actionOpenIndvChat);
        process.putStepActionAssociation(stepOpenIndvChat,actionCaptureImage);
        process.putStepActionAssociation(stepOpenCamera,actionCapturePreview);
        process.putStepActionAssociation(stepOpenSettings,actionOpenProfile);
        process.putStepActionAssociation(stepOpenProfile,actionCaptureProfilePicture);
        process.putStep(stepCapturePreview);
        return process;
    }

    @OnClick(R.id.goBack)
    public void goBack(){
        processManager.goBack();
        showCurrentStep();
    }

    @OnClick(R.id.showOverlay)
    public void showOverlay(){
        hideFullScreen();
        mTestView.setVisibility(View.VISIBLE);
    }

    private void hideFullScreen() {
        moveTaskToBack(true);
    }

    public void showFullScreen(){
        hideOverlay();
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    private void hideOverlay() {
        mTestView.setVisibility(View.GONE);
    }

    public void openOverlay(final Context context) {

        mTestView=View.inflate(context, R.layout.overlay_layout, null);

        overlayTitle = mTestView.findViewById(R.id.overlayTitle);
        overlayDescription = mTestView.findViewById(R.id.overlayDescription);
        overlayNextButton = mTestView.findViewById(R.id.overlayNextButton);
        overlayBackButton = mTestView.findViewById(R.id.overlayGoBack);

        overlayTitle.setTypeface(Typeface.createFromAsset(getAssets(),
                "kellyslab.ttf"));
        overlayDescription.setTypeface(Typeface.createFromAsset(getAssets(),
                "ralewaym.ttf"));

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                //WindowManager.LayoutParams.FIRST_SUB_WINDOW
                700,
                475,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        layoutParams.x=0;
        layoutParams.y=0;

        windowManager.addView(mTestView, layoutParams);



        overlayDescription.post(new Runnable() {
            @Override
            public void run() {
                final String temp = overlayDescription.getText().toString();
                overlayDescription.setText("A\nB\nC");
                overlayDescription.post(new Runnable() {
                    @Override
                    public void run() {
                        overlayDescription.setMinHeight(overlayDescription.getMeasuredHeight());
                        overlayDescription.setText(temp);
                        mTestView.post(new Runnable() {
                            @Override
                            public void run() {
                                mTestView.setMinimumWidth(mTestView.getMeasuredWidth());
                                mTestView.setMinimumHeight(mTestView.getMeasuredHeight());
                                hideOverlay();
                            }
                        });
                    }
                });
            }
        });

        mTestView.findViewById(R.id.overlayClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideOverlay();
            }
        });

        overlayBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        mTestView.findViewById(R.id.overlayFullView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFullScreen();
            }
        });

        overlayNextButton.setOnClickListener(overlayActionClicked);

        mTestView.setOnTouchListener(new View.OnTouchListener() {
            int downX,downY;
            int downLX,downLY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                System.out.println(motionEvent.toString());
                final int X = (int) motionEvent.getRawX();
                final int Y = (int) motionEvent.getRawY();

                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    downX=X;
                    downY=Y;
                    downLX=layoutParams.x;
                    downLY=layoutParams.y;
                }else if(motionEvent.getAction()==MotionEvent.ACTION_MOVE){
                    layoutParams.x= X - (downX-downLX);
                    layoutParams.y= Y - (downY-downLY);
                    windowManager.updateViewLayout(mTestView,layoutParams);
                }

                return true;
            }
        });
    }
}