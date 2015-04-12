package com.yt.loaninterestrate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.yt.loaninterestrate.activity.HelpActivity;
import com.yt.loaninterestrate.activity.History;
import com.yt.loaninterestrate.tools.CheckUpdate;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Main#newInstance} factory method to
 * create an instance of this fragment.
 */


public class Main extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageButton btn1,btn2,btn3,btn4;

    private static  ImageButton btnHome,btnPersion,btnUpdate,btnSetting;
    public static Context context;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Main.
     */
    // TODO: Rename and change types and number of parameters
    public static Main newInstance(String param1, String param2) {
        Main fragment = new Main();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Main() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.context = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_main, container, false);

        CheckUpdate ckup = new CheckUpdate(getActivity(),"Tip",v);

        btn1 = (ImageButton)v.findViewById(R.id.imageButton1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mViewPager.setCurrentItem(3);
            }
        });


        btn2 = (ImageButton)v.findViewById(R.id.imageButton2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mViewPager.setCurrentItem(1);
            }
        });


        btn3 = (ImageButton)v.findViewById(R.id.imageButton3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mViewPager.setCurrentItem(4);
            }
        });


        btn4 = (ImageButton)v.findViewById(R.id.imageButton4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mViewPager.setCurrentItem(2);
            }
        });

        initTool(v);

        return v;
    }



    public static void initTool(View v){
        btnHome = (ImageButton)v.findViewById(R.id.btnHome);
        btnPersion = (ImageButton)v.findViewById(R.id.btnPersion);
        btnUpdate  = (ImageButton)v.findViewById(R.id.btnUpdate);
        btnSetting = (ImageButton)v.findViewById(R.id.btnSetting);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mViewPager.setCurrentItem(0);
            }
        });

        btnPersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenet = new Intent();
                intenet.setClass(context,History.class);
                context.startActivity(intenet);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckUpdate checkupdate = new CheckUpdate(context);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,HelpActivity.class));

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
