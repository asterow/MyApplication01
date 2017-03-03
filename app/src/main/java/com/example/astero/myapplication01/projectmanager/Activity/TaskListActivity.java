package com.example.astero.myapplication01.projectmanager.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.astero.myapplication01.HTTPRequestAsyncTask;
import com.example.astero.myapplication01.R;
import com.example.astero.myapplication01.projectmanager.Fragment.TaskFragment;
import com.example.astero.myapplication01.projectmanager.data.ProjectList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class TaskListActivity extends AppCompatActivity implements HTTPRequestAsyncTask.AsyncResponse, TaskFragment.OnListFragmentInteractionListener {

    private TaskFragment taskFragment;
    private int projectSelected;
    int CREATE_FLAG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Intent i = getIntent();
        projectSelected = i.getIntExtra("project", 0);
        setTitle(ProjectList.PROJECTS.get(projectSelected).getName());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        taskFragment = TaskFragment.newInstance(1, projectSelected);
        ft.replace(R.id.fragmentTaskList, taskFragment);
        ft.addToBackStack(null);
        ft.commit();


    }

    // CALLED when click on Fragment's task's button Save or confirme Delete
    @Override
    public void onListFragmentInteraction(String ACTION, ProjectList.Tache task, View view) {
        switch (ACTION) {
            case ProjectList.ACTION_PUT_TASK:
                new HTTPRequestAsyncTask(this).execute(ProjectList.ACTION_PUT_TASK, "PUT", ProjectList.SERVER_URL +
                        "/tasks/" + task.getId() +
                        "?name=" + task.getName() +
                        "&desc=" + task.getDesc() +
                        "&state=" + task.getState() +
                        "&project.id=" + task.getProject().getId());
                break;
            case ProjectList.ACTION_DELETE_TASK:
                new HTTPRequestAsyncTask(this).execute(ProjectList.ACTION_DELETE_TASK, "DELETE", ProjectList.SERVER_URL +
                        "/tasks/" + task.getId());
                break;
        }

    }

    // BUTTON CREATE TASK
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreateTask:
                if (CREATE_FLAG == 0) {
                    CREATE_FLAG = 1;
                    ProjectList.Project project = ProjectList.PROJECTS.get(projectSelected);
                    int size = project.getListTache().size() + 1;
                    System.out.println("CREATE TASK -> PROJECTID  = " + project.getId());
                    new HTTPRequestAsyncTask(this).execute(ProjectList.ACTION_POST_TASK, "POST", ProjectList.SERVER_URL +
                            "/tasks?name=" + "Tache " + size +
                            "&desc=" + "Description task " + size +
                            "&state=TO_DO&project.id=" + project.getId());
                }
                break;
            default:
                Toast.makeText(view.getContext(), "Je suis un Toast par default", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    // RESULT of a HTTPRrequestAsyncTask
    @Override
    public void processFinish(String result, String ACTION) {
        try{
            switch (ACTION) {
                case ProjectList.ACTION_POST_TASK:
//                ProjectList.Tache task = new Gson().fromJson(result, ProjectList.Tache.class);
                    ProjectList.Tache task = new ObjectMapper().readValue(result, ProjectList.Tache.class);
                    if (task != null) {
                        taskFragment.add(0, task);
                        System.out.println("TASK ADDED : TASK PROJECT ID : " + task.getProject().getId());
                    }
                    CREATE_FLAG = 0;
                    break;
            }
            System.out.println(ACTION + " -> Result TaskActivity = " + result);
        }
        catch (Exception e) {
            e.printStackTrace();
            CREATE_FLAG = 0;
        }
    }
}
