package com.example.astero.myapplication01.projectmanager.data;

import com.example.astero.myapplication01.projectmanager.Fragment.ProjectFragment;
import com.example.astero.myapplication01.projectmanager.Fragment.TaskFragment;
import com.example.astero.myapplication01.projectmanager.ProjectManagerService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by astero on 28/02/2017.
 */

public class ProjectList {

    public static final String SERVER_URL = "http://10.0.2.2:8080";


    public static final List<Project> PROJECTS = new ArrayList<Project>();
    public static final List<Tache> TASKS = new ArrayList<Tache>();

    public static final Map<String, Project> PROJECTS_MAP = new HashMap();

    public static final String ACTION_GET_PROJECTS = "ACTION_GET_PROJECTS";
    public static final String ACTION_GET_TASKS = "ACTION_GET_TASKS";
    public static final String ACTION_POST_TASK = "ACTION_POST_TASK";
    public static final String ACTION_PUT_TASK = "ACTION_PUT_TASK";
    public static final String ACTION_DELETE_TASK = "ACTION_DELETE_TASK";
    public static final String ACTION_POST_PROJECT = "ACTION_POST_PROJECT";

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    public static final ProjectManagerService projectManagerService = retrofit.create(ProjectManagerService.class);

    public static void addProject(int position, Project project) {
        PROJECTS.add(position, project);
        PROJECTS_MAP.put(project.id, project);
    }

    public static void getProjectsFromServer(final ProjectFragment projectFragment) {


        Call<List<Project>> listProject = projectManagerService.listProject();
        listProject.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<ProjectList.Project>> call, Response<List<Project>> response) {
                PROJECTS.clear();
                PROJECTS.addAll(response.body());
                Collections.reverse(PROJECTS);
                projectFragment.notifyProjectsChanged();
                PROJECTS_MAP.clear();
                PROJECTS_MAP.putAll(PROJECTS.stream().collect(
                        Collectors.toMap(project -> project.getId(), project -> project)));
//                PROJECTS.forEach();

                System.out.println(PROJECTS.size());
            }

            @Override
            public void onFailure(Call<List<ProjectList.Project>> call, Throwable t) {

            }
        });
    }




    public static class Project {
        private String id;
        private String name;
        private String desc;
        private List<Tache> listTache = new ArrayList();
        public static final Map<String, Tache> mapTache = new HashMap();

        public Project(String id, String name, String desc, ArrayList<Tache> listTache) {
            this.id = id;
            this.name = name;
            this.desc = desc;
            if (listTache != null) {
                this.listTache.clear();
                this.listTache.addAll(listTache);
            }
        }

        public void addTache(int position, Tache tache) {
            listTache.add(position, tache);
            mapTache.put(tache.id, tache);
        }



        public Project() {


        }

        public  void getProjectsTasksFromServer(final TaskFragment taskFragment) {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(SERVER_URL)
//                    .addConverterFactory(JacksonConverterFactory.create())
//                    .build();
//            ProjectManagerService service = retrofit.create(ProjectManagerService.class);

            Call<List<Tache>> listProject = projectManagerService.listTask(id);
            listProject.enqueue(new Callback<List<Tache>>() {
                @Override
                public void onResponse(Call<List<ProjectList.Tache>> call, Response<List<Tache>> response) {
                    if (response.isSuccessful()) {
                        listTache.clear();
                        listTache.addAll(response.body());
                        Collections.reverse(listTache);
//                        listTache.
                        taskFragment.notifyTasksChanged();
                        System.out.println("listTache :" + listTache.toString());
                    }
                    else
                        System.out.println("Negative Response !!! Project_id : " + id);

                }

                @Override
                public void onFailure(Call<List<ProjectList.Tache>> call, Throwable t) {

                }
            });
        }


        public List<Tache> getListTache() {
            return listTache;
        }

        public void setListTache(List<Tache> listTache) {
            this.listTache = listTache;
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }


        @Override
        public String toString() {
            return "Project{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    ", listTache=" + listTache +
                    '}';
        }
    }

    public static class Tache {
        private  String id;
        private  String name;
        private  String desc;
        private  String state;
        private  Project project;

        public Tache(String id, String name, String desc, String state, Project project) {
            this.id = id;
            this.name = name;
            this.desc = desc;
            this.state = state;
            this.project = project;
        }
        public Tache() {

        }

        public Project getProject() {
            return project;
        }

        public void setProject(Project project) {
            this.project = project;
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return "Tache{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    ", state='" + state + '\'' +
                    ", project=" + project +
                    '}';
        }
    }
}

