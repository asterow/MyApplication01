package com.example.astero.myapplication01;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageCardGridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageCardGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageCardGridFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<String> listGifUrl = new ArrayList();
    private int nbrCollumn = 1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private GiphyRecycleViewAdapter giphyRecycleViewAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;


    public ImageCardGridFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ImageCardGridFragment.
//     */
    // TODO: Rename and change types and number of parameters
    public static ImageCardGridFragment newInstance(int nbCol, ArrayList<String> listGifUrl) {
        ImageCardGridFragment fragment = new ImageCardGridFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, listGifUrl);
        args.putInt(ARG_PARAM2, nbCol);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listGifUrl = getArguments().getStringArrayList(ARG_PARAM1);
            nbrCollumn = getArguments().getInt(ARG_PARAM2);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_image_card_grid, container, false);
//        if (listGifUrl != null) {
//            System.out.println("###### @@@@@@ ###### FULL LIST");
            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
            //définit l'agencement des cellules, ici de façon verticale, comme une ListView


        ItemClickSupport.addTo(recyclerView)
                .setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                        Uri chemin = Uri.parse(listGifUrl.get(position).toString());
                        Intent naviguer = new Intent(Intent.ACTION_VIEW, chemin);
                        startActivity(naviguer);
                        return false;
                    }
                });
//        }
//        else
//            System.out.println("###### @@@@@@ ###### NULL LIST");

        return v;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridLayoutManager = new GridLayoutManager(getActivity(), nbrCollumn);
        recyclerView.setLayoutManager(gridLayoutManager);

        giphyRecycleViewAdapter = new GiphyRecycleViewAdapter(listGifUrl);
        recyclerView.setAdapter(giphyRecycleViewAdapter);
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }


    public void add(int position, String newGifUrl)
    {
        giphyRecycleViewAdapter.add(0, newGifUrl);
        gridLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        super.onViewCreated(v, savedInstanceState);



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
