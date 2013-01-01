/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
                asyncTask.togglePause();
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
        updateButton(!asyncTask.isRunning());
    }

    public void updateProgress(@Observes ProgressEvent progressEvent){
        progressBar.setProgress(progressEvent.getProgress());
    }

    public void resetProgress(@Observes ResetEvent resetEvent){
        asyncTask = taskProvider.get();
        updateButton(true);
    }

    @OnBackPressed
    public void cancelTask(){
        asyncTask.cancel(true);
    }

    public void pauseTriggered(@Observes UpdateEvent updateEvent){
        updateButton(updateEvent.isPaused());
    }

    public void updateButton(boolean paused){
        if(paused){
            restartButton.setText(startTaskText);
        }
        else{
            restartButton.setText(pauseTaskText);
        }
    }
}
