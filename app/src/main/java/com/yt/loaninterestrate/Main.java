package com.yt.loaninterestrate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

    private static  ImageButton btnBook,btnPersion,btnUpdate,btnSetting;
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
        initSkin(context ,v);
        return v;
    }


    public static void initSkin(Context context ,View v){
        final SharedPreferences sp = context.getSharedPreferences("Skin", context.MODE_PRIVATE);
        int which = sp.getInt("which", 0);
        FrameLayout fragment_main;
        fragment_main = (FrameLayout)v.findViewById(R.id.fragment_main);
        Resources resources = context.getResources();
        Drawable btnDrawable = null;
        if(which==1){
            btnDrawable = resources.getDrawable(R.drawable.bg1);
        }else if(which==2){
            btnDrawable = resources.getDrawable(R.drawable.bg2);
        }else{
            btnDrawable = resources.getDrawable(R.drawable.bg);
        }

        fragment_main.setBackgroundDrawable(btnDrawable);
    }

    public static void initTool(final View v){
        btnBook = (ImageButton)v.findViewById(R.id.btnBook);
        btnPersion = (ImageButton)v.findViewById(R.id.btnPersion);
        btnUpdate  = (ImageButton)v.findViewById(R.id.btnUpdate);
        btnSetting = (ImageButton)v.findViewById(R.id.btnSetting);

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, HelpActivity.class));
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
            public void onClick(View view) {
                CheckUpdate checkupdate = new CheckUpdate(context);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final SharedPreferences sp = context.getSharedPreferences("Skin", context.MODE_PRIVATE);

                int which = sp.getInt("which", 0);

                final String[] items = {"金属黄","卫士蓝","苹果绿"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("设置页面显示背景");
                builder.setSingleChoiceItems(items, which, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        initSkin(context,v);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("which", which);
                        edit.commit();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();

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
