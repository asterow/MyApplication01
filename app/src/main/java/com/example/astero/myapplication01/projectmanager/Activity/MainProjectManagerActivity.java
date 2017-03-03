package com.example.astero.myapplication01.projectmanager.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.astero.myapplication01.R;
import com.example.astero.myapplication01.projectmanager.data.ProjectList;
import com.example.astero.myapplication01.projectmanager.data.ProjectList.Project;
import com.example.astero.myapplication01.projectmanager.data.ProjectList.Tache;


public class MainProjectManagerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_project_manager);


//        ProjectList.addItem(new Project(1, "Projet 1", "Description du projet", null));
//        ProjectList.addItem(new Project(2, "Projet 2", "Description du projet", null));
//        ProjectList.addItem(new Project(3, "Projet 3", "Description du projet", null));
//        ProjectList.addItem(new Project(4, "Projet 4", "Description du projet", null));
//        ProjectList.addItem(new Project(5, "Projet 5", "Description du projet", null));
//        ProjectList.addItem(new Project(6, "Projet 6", "Description du projet", null));
//        ProjectList.PROJECTS.get(1).addTache(0, new Tache(1, "Tache 1", "Desc tache 1", "TO_DO", 1));
//        ProjectList.PROJECTS.get(1).addTache(1, new Tache(2, "Tache 2", "Desc tache 2", "TO_DO", 5));
//        ProjectList.PROJECTS.get(1).addTache(2, new Tache(3, "Tache 3", "Desc tache 3", "ON_PROGRESS", 4));
//        ProjectList.PROJECTS.get(1).addTache(3, new Tache(4, "Tache 4", "Desc tache 4", "DONE", 3));

        Intent i = new Intent(this, ProjectListActivity.class);
        startActivity(i);
        finish();
    }


}
