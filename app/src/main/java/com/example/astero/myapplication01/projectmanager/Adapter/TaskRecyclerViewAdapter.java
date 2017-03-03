package com.example.astero.myapplication01.projectmanager.Adapter;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.astero.myapplication01.R;
import com.example.astero.myapplication01.projectmanager.Fragment.TaskFragment.OnListFragmentInteractionListener;
import com.example.astero.myapplication01.projectmanager.data.ProjectList;
import com.example.astero.myapplication01.projectmanager.data.ProjectList.Tache;

import java.util.ArrayList;


public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Tache> mValues;
    private final OnListFragmentInteractionListener mListener;

    public TaskRecyclerViewAdapter(ArrayList<Tache> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void add(int position, Tache tache) {
//        mValues.add(position, tache); // on insère le nouvel objet dans notre       liste d'article lié à l'adapter
        ProjectList.PROJECTS_MAP.get(tache.getProject().getId()).addTache(0, tache);
        notifyItemInserted(position); //
    }

    public void delete(Tache tache) {
        int position = mValues.indexOf(tache);
        mValues.remove(tache);
        notifyItemRemoved(position);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.textViewTaskName.setText(mValues.get(position).getName());
        holder.textViewTaskDesc.setText(mValues.get(position).getDesc());

        switch (holder.mItem.getState()) {
            case "TO_DO":
                holder.frameLayoutTaskColor.setBackgroundColor(
                        ContextCompat.getColor(holder.mView.getContext(), R.color.todo));
                holder.buttonTaskState.setText("TODO");

                break;
            case "ON_PROGRESS":
                holder.frameLayoutTaskColor.setBackgroundColor(
                        ContextCompat.getColor(holder.mView.getContext(), R.color.onprogress));
                holder.buttonTaskState.setText("ON PROGRESS");

                break;
            case "DONE":
                holder.frameLayoutTaskColor.setBackgroundColor(
                        ContextCompat.getColor(holder.mView.getContext(), R.color.done));
                holder.buttonTaskState.setText("DONE");
                break;
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //mListener.onListFragmentInteraction(holder.mItem, v);
                }
            }
        });
        holder.buttonTaskEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    switch (holder.buttonTaskEdit.getText().toString()) {
                        case "EDIT":
                            holder.buttonTaskEdit.setText("SAVE");
                            holder.editTextTaskName.setText(holder.mItem.getName());
                            holder.editTextTaskDesc.setText(holder.mItem.getDesc());
                            holder.editTextTaskName.selectAll();
                            holder.mView.findViewById(R.id.taskLayout).setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, holder.mView.getHeight()*2));

                            break;
                        case "SAVE":
                            holder.buttonTaskEdit.setText("EDIT");
                            holder.mItem.setName(holder.editTextTaskName.getText().toString());
                            holder.mItem.setDesc(holder.editTextTaskDesc.getText().toString());
                            holder.textViewTaskName.setText(holder.mItem.getName());
                            holder.textViewTaskDesc.setText(holder.mItem.getDesc());
                            holder.mView.findViewById(R.id.taskLayout).setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, holder.mView.getHeight()/2));
                            //TODO "UPDATE", "http://10.0.2.2:8080/tasks?name=" + holder.mItem.name + "1&desc=" + holder.mItem.desc + "&state=" + holder.mItem.state + "&project.id=" + String.valueOf(holder.mItem.projectId"
                            mListener.onListFragmentInteraction(ProjectList.ACTION_PUT_TASK, holder.mItem, holder.mView);
                            break;
                    }
                    holder.viewSwitcherTaskEdit.showNext();
//                    holder.viewSwitcherTaskDesk.showNext();
//                    holder.viewSwitcherTaskState.showNext();

//                    mListener.onListFragmentInteraction(holder.mItem, holder.mView);
                }
            }
        });
        holder.buttonTaskState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    switch (holder.buttonTaskState.getText().toString()) {
                        case "TODO":
                            holder.buttonTaskState.setText("ON PROGRESS");
                            holder.mItem.setState("ON_PROGRESS");
                            holder.frameLayoutTaskColor.setBackgroundColor(
                                    ContextCompat.getColor(holder.mView.getContext(), R.color.onprogress));
                            break;
                        case "ON PROGRESS":
                            holder.buttonTaskState.setText("DONE");
                            holder.mItem.setState("DONE");
                            holder.frameLayoutTaskColor.setBackgroundColor(
                                    ContextCompat.getColor(holder.mView.getContext(), R.color.done));
                            break;
                        case "DONE":
                            holder.buttonTaskState.setText("TODO");
                            holder.mItem.setState("TO_DO");
                            holder.frameLayoutTaskColor.setBackgroundColor(
                                    ContextCompat.getColor(holder.mView.getContext(), R.color.todo));
                            break;
                    }
                }
            }
        });
        holder.buttonTaskDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.mView.getContext());
                    builder.setTitle("DELETE");
                    builder.setMessage("Do you really want to delete this task ?");

                    String positiveText = "OK";
                    builder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    delete(holder.mItem);
                                    mListener.onListFragmentInteraction(ProjectList.ACTION_DELETE_TASK, holder.mItem, holder.mView);
                                }
                            });

                    String negativeText = "CANCEL";
                    builder.setNegativeButton(negativeText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // negative button logic
                                }
                            });

                    AlertDialog dialog = builder.create();
                    // display dialog
                    dialog.show();
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
        public final TextView textViewTaskName;
        public final TextView textViewTaskDesc;
        public final FrameLayout frameLayoutTaskColor;
        public final Button buttonTaskEdit;
        public final Button buttonTaskState;
        public final Button buttonTaskDelete;
        public final EditText editTextTaskName;
        public final EditText editTextTaskDesc;
        public final ViewSwitcher viewSwitcherTaskEdit;

        public Tache mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            textViewTaskName = (TextView) view.findViewById(R.id.textViewTaskName);
            textViewTaskDesc = (TextView) view.findViewById(R.id.textViewTaskDesc);
            frameLayoutTaskColor = (FrameLayout) view.findViewById(R.id.taskColor);
            buttonTaskEdit = (Button) view.findViewById(R.id.buttonTaskEdit);
            buttonTaskState = (Button) view.findViewById(R.id.buttonTaskState);
            buttonTaskDelete = (Button) view.findViewById(R.id.buttonTaskDelete);
            editTextTaskName = (EditText) view.findViewById(R.id.editTextTaskName);
            editTextTaskDesc = (EditText) view.findViewById(R.id.editTextTaskDesc);
            viewSwitcherTaskEdit = (ViewSwitcher) view.findViewById(R.id.switcherTaskEdit);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewTaskDesc.getText() + "'";
        }
    }
}
