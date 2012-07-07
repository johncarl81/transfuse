package org.androidtransfuse.integrationTest.saveState;

import android.widget.Button;
import android.widget.ProgressBar;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
@Activity
@Layout(R.layout.async_task_nonconfig)
public class NonConfigInstance {

    @Inject
    @Resource(R.string.start_task)
    private String startTaskText;

    @Inject
    @Resource(R.string.pause_task)
    private String pauseTaskText;

    @Inject
    private Provider<BackgroundAsyncTask> taskProvider;

    @NonConfigurationInstance
    private BackgroundAsyncTask asyncTask;

    @Inject @View(R.id.progress_bar)
    private ProgressBar progressBar;

    @Inject @View(R.id.restart_button)
    private Button restartButton;

    @RegisterListener(R.id.restart_button)
    private android.view.View.OnClickListener restartClick = new android.view.View.OnClickListener(){
        @Override
        public void onClick(android.view.View v) {
            if(asyncTask.isRunning()){
                if(asyncTask.isPaused()){
                    asyncTask.setPaused(false);
                }
                else{
                    asyncTask.setPaused(true);
                }
            }
            else{
                asyncTask.execute();
            }
        }
    };

    @OnStart
    public void createTask(){
        if(asyncTask == null || asyncTask.isCancelled()){
            asyncTask = taskProvider.get();
        }
        if(asyncTask.isRunning()){
            updateButton();
        }
    }

    public void updateProgress(@Observes ProgressEvent progressEvent){
        progressBar.setProgress(progressEvent.getProgress());
    }

    @UIThread
    public void resetProgress(@Observes ResetEvent resetEvent){
        asyncTask = taskProvider.get();
        updateButton();
    }

    @OnBackPressed
    public void cancelTask(){
        asyncTask.cancel(true);
    }

    public void pauseTriggered(@Observes PauseEvent pauseEvent){
        updateButton();
    }

    public void updateButton(){
        if(asyncTask.isPaused()){
            restartButton.setText(startTaskText);
        }
        else{
            restartButton.setText(pauseTaskText);
        }
    }
}
