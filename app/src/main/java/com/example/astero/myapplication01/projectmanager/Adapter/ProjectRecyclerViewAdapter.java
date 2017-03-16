package com.example.astero.myapplication01.projectmanager.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.astero.myapplication01.R;
import com.example.astero.myapplication01.projectmanager.Fragment.ProjectFragment.OnListFragmentInteractionListener;
import com.example.astero.myapplication01.projectmanager.data.ProjectList;
import com.example.astero.myapplication01.projectmanager.data.ProjectList.Project;

import java.util.List;


public class ProjectRecyclerViewAdapter extends RecyclerView.Adapter<ProjectRecyclerViewAdapter.ViewHolder> {

    private final List<Project> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ProjectRecyclerViewAdapter(List<Project> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void add(int position, ProjectList.Project project) {
//        mValues.add(position, project); // on insère le nouvel objet dans notre       liste d'article lié à l'adapter
        ProjectList.addProject(position, project);
        notifyItemInserted(position); //
    }
    public void notifyProjectsChanged() {
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.textViewProjectName.setText(mValues.get(position).getName());
        holder.textViewProjectDesc.setText(mValues.get(position).getDesc());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewProjectName;
        public final TextView textViewProjectDesc;
        public Project mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            textViewProjectName = (TextView) view.findViewById(R.id.textViewProjectName);
            textViewProjectDesc = (TextView) view.findViewById(R.id.textViewProjectDesc);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewProjectDesc.getText() + "'";
        }
    }
}
