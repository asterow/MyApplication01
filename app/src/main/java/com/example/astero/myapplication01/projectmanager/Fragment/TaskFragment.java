package com.example.astero.myapplication01.projectmanager.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.astero.myapplication01.R;
import com.example.astero.myapplication01.projectmanager.Adapter.TaskRecyclerViewAdapter;
import com.example.astero.myapplication01.projectmanager.data.ProjectList;
import com.example.astero.myapplication01.projectmanager.data.ProjectList.Tache;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TaskFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_PROJECT_POS = "project-pos";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private int mProjectPos = 0;
    private OnListFragmentInteractionListener mListener;
    private GridLayoutManager gridLayoutManager;
    private TaskRecyclerViewAdapter taskRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TaskFragment newInstance(int columnCount, int projectPos) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(ARG_PROJECT_POS, projectPos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mProjectPos = getArguments().getInt(ARG_PROJECT_POS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                linearLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(linearLayoutManager);
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(ProjectList.PROJECTS.get(mProjectPos).getListTache(), mListener);
//            taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(ProjectList.TASKS, mListener);
            recyclerView.setAdapter(taskRecyclerViewAdapter);
        }
        return view;
    }

    public void add(int position, Tache tache) {
        //ProjectList.PROJECTS.get(mProjectPos).listTache.add(position, tache);
        taskRecyclerViewAdapter.add(position, tache);
        linearLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    public void notifyTasksChanged() {
        //ProjectList.PROJECTS.get(mProjectPos).listTache.add(position, tache);
        taskRecyclerViewAdapter.notifyDataSetChanged();
        linearLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(String ACTION, Tache item, View view);
    }
}
