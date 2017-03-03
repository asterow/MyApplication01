package com.example.astero.myapplication01.projectmanager.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by astero on 28/02/2017.
 */

public class ProjectList {

    public static final String SERVER_URL = "http://10.0.2.2:8080";


    public static final List<Project> PROJECTS = new ArrayList<Project>();

    public static final Map<String, Project> PROJECTS_MAP = new HashMap();

    public static final String ACTION_GET_PROJECTS = "ACTION_GET_PROJECTS";
    public static final String ACTION_GET_TASKS = "ACTION_GET_TASKS";
    public static final String ACTION_POST_TASK = "ACTION_POST_TASK";
    public static final String ACTION_PUT_TASK = "ACTION_PUT_TASK";
    public static final String ACTION_DELETE_TASK = "ACTION_DELETE_TASK";
    public static final String ACTION_POST_PROJECT = "ACTION_POST_PROJECT";


    public static void addProject(int position, Project project) {
        PROJECTS.add(position, project);
        PROJECTS_MAP.put(project.id, project);
    }



    public static class Project {
        private String id;
        private String name;
        private String desc;
        private ArrayList<Tache> listTache = new ArrayList();
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


        public ArrayList<Tache> getListTache() {
            return listTache;
        }

        public void setListTache(ArrayList<Tache> listTache) {
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

