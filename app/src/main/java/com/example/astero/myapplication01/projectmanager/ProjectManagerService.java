package com.example.astero.myapplication01.projectmanager;

import com.example.astero.myapplication01.projectmanager.data.ProjectList;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.*;

/**
 * Created by astero on 04/03/2017.
 */

public interface ProjectManagerService {
    @Headers("Accept: application/json")
    @GET("project/{id}/task")
    Call<List<ProjectList.Tache>> listTask(@Path("id") String id);

    @Headers("Accept: application/json")
    @GET("projects")
    Call<List<ProjectList.Project>> listProject();
}
