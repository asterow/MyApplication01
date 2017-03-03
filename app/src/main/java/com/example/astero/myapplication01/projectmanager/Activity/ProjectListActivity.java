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
import com.example.astero.myapplication01.projectmanager.Fragment.ProjectFragment;
import com.example.astero.myapplication01.projectmanager.data.ProjectList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ProjectListActivity extends AppCompatActivity implements HTTPRequestAsyncTask.AsyncResponse, ProjectFragment.OnListFragmentInteractionListener {

    private ProjectFragment projectFragment;
    int CREATE_FLAG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        setTitle("Project List");

        ProjectList.PROJECTS.clear();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        projectFragment = ProjectFragment.newInstance(1);
        ft.replace(R.id.fragmentProjectList, projectFragment);
        ft.addToBackStack(null);
        ft.commit();

        
        new HTTPRequestAsyncTask(this).execute(ProjectList.ACTION_GET_PROJECTS, "GET", ProjectList.SERVER_URL +
                "/projects?max=-1");
    }

    @Override // CALLED WHEN CLICK ON FRAGMENT'S ITEM
    public void onListFragmentInteraction(ProjectList.Project item) {
        int position = ProjectList.PROJECTS.indexOf(item);
        System.out.println("Project name = " + item.getName());
        Intent i = new Intent(this, TaskListActivity.class);
        i.putExtra("project", position);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    // BUTTON CREATE PROJECT
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreateProject:
                if (CREATE_FLAG == 0) {
                    CREATE_FLAG = 1;
                    int size;
                    if (ProjectList.PROJECTS.size() > 0)
                     size = Integer.parseInt(ProjectList.PROJECTS.get(0).getId()) + 1;
                    else
                        size = 1;
                    new HTTPRequestAsyncTask(this).execute(ProjectList.ACTION_POST_PROJECT, "POST", ProjectList.SERVER_URL +
                            "/projects?name=" + "Project id : " + size +
                            "&desc=" + "Description project id : " + size);
                }
                else
                    System.out.println("WAITING FOR FLAG");
                break;
            default:
                Toast.makeText(view.getContext(), "Je suis un Toast par default", Toast.LENGTH_SHORT).show();

        }
    }

    // RESULT of a HTTPRrequestAsyncTask
    @Override
    public void processFinish(String result, String ACTION) {
        try {
            if (result != null) {

                switch (ACTION) {
                    case ProjectList.ACTION_GET_PROJECTS:
//                    Type listTypeProject = new TypeToken<ArrayList<ProjectList.Project>>() {}.getType(); //Convert result to objects
//                    List<ProjectList.Project> listProjects = new Gson().fromJson(result, listTypeProject);
                        List<ProjectList.Project> listProjects = new ObjectMapper().readValue(result, new TypeReference<List<ProjectList.Project>>() {
                        });
                        if (listProjects != null) {
                            ProjectList.Project p;
                            final int sizeListProject = listProjects.size();
                            for (int i = 0; i < sizeListProject; i++) {
                                p = listProjects.get(i);
                                p.setListTache(new ArrayList());
                                System.out.println(ACTION + " -> PROJECT NAME = " + p.getName() + " PROJECT ID = " + p.getId());
                                projectFragment.add(0, p);

                            }
                            new HTTPRequestAsyncTask(this).execute(ProjectList.ACTION_GET_TASKS, "GET", ProjectList.SERVER_URL +
                                    "/tasks?max=-1");
                        }
                        break;
                    case ProjectList.ACTION_GET_TASKS:
//                    Type listTypeTask = new TypeToken<ArrayList<ProjectList.Tache>>() {}.getType();
//                    List<ProjectList.Tache> listTasks = new Gson().fromJson(result, listTypeTask);
                        List<ProjectList.Tache> listTasks = new ObjectMapper().readValue(result, new TypeReference<List<ProjectList.Tache>>() {
                        });
                        if (listTasks != null && ProjectList.PROJECTS != null) {
                            ProjectList.Tache t;
                            final int sizeListTask = listTasks.size();
                            for (int i = 0; i < sizeListTask; i++) {
                                t = listTasks.get(i);
                                System.out.println(ACTION + " -> PROJECT TASK = " + t.getName() + " PROJECT ID = " + t.getProject().getId());
                                ProjectList.PROJECTS_MAP.get(t.getProject().getId()).addTache(0, t);
                            }
                        }


                        break;
                    case ProjectList.ACTION_POST_PROJECT:
//                    ProjectList.Project project = new Gson().fromJson(result, ProjectList.Project.class);
                        ProjectList.Project project = new ObjectMapper().readValue(result, ProjectList.Project.class);
                        if (project != null) {
                            project.setListTache(new ArrayList());
                            projectFragment.add(0, project);
                        }
                        CREATE_FLAG = 0;

                        break;
                }

            }
            System.out.println("##### PROJECT ACTIVITY : RESULT HTTPRequestAsyncTask= " + result);
        }
        catch (Exception e) {
            e.printStackTrace();
            CREATE_FLAG = 0;

        }
    }
}
