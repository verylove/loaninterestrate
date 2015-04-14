package com.yt.loaninterestrate.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yt.loaninterestrate.Main;
import com.yt.loaninterestrate.MainActivity;
import com.yt.loaninterestrate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tax.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tax#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tax extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RadioButton radioOld,radioNew,radioButtonTotal,radioButtonDiff,radioYes5,radioNo5,radioYes1,radioNo1,radioYesOnly,radioNoOnly;
    private EditText editTextArea,editTextUnitPrice,editTextAllPrice,editTextAGoAllPrice;
    private Spinner spinnerHouseType;
    private LinearLayout layoutOld,layoutOldHouse,layoutOver5,HiddenPart;
    private Boolean isOver5,isFrist,isOnly;
    private Double HouseArea,AreaUnit,HousePrice,AGoHousePrice;
    private ImageButton calculate,btnHome,buttonReset;
    private Integer HouseType,OldNew,AllOff;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tax.
     */
    // TODO: Rename and change types and number of parameters
    public static Tax newInstance(String param1, String param2) {
        Tax fragment = new Tax();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Tax() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen"); //统计页面
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }

    public static boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MainActivity.mViewPager.setCurrentItem(0);
        }
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tax, container, false);
       // Main.initTool(v);
        initData(v);
        Main.initSkin(getActivity() ,v);
        btnHome = (ImageButton)v.findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mViewPager.setCurrentItem(0);
            }
        });

        buttonReset = (ImageButton)v.findViewById(R.id.btnReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetData();
            }
        });

        radioOld.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layoutOld.setVisibility(View.VISIBLE);
                    OldNew= 1;
                   // Toast.makeText(getActivity(),"选中",Toast.LENGTH_LONG).show();
                }else{
                    layoutOld.setVisibility(View.GONE);
                    OldNew= 2;
                    //Toast.makeText(getActivity(),"没选中",Toast.LENGTH_LONG).show();
                }
            }
        });

        radioNew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    layoutOld.setVisibility(View.VISIBLE);
                    OldNew= 1;
                    // Toast.makeText(getActivity(),"选中",Toast.LENGTH_LONG).show();
                }else{
                    layoutOld.setVisibility(View.GONE);
                    OldNew= 2;
                    //Toast.makeText(getActivity(),"没选中",Toast.LENGTH_LONG).show();
                }
            }
        });

        radioButtonTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layoutOldHouse.setVisibility(View.GONE);
                    AllOff = 1;
                }else{
                    layoutOldHouse.setVisibility(View.VISIBLE);
                    AllOff = 2;
                }
            }
        });

        radioButtonDiff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    layoutOldHouse.setVisibility(View.GONE);
                    AllOff = 1;
                }else{
                    layoutOldHouse.setVisibility(View.VISIBLE);
                    AllOff = 2;
                }
            }
        });

        radioYes5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isOver5 = true;
                }else{
                    isOver5 = false;
                }
            }
        });

        radioNo5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    isOver5 = true;
                }else{
                    isOver5 = false;
                }
            }
        });

        radioYes1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isFrist = true;
                }else{
                    isFrist = false;
                }
            }
        });

        radioNo1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    isFrist = true;
                }else{
                    isFrist = false;
                }
            }
        });

        radioYesOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isOnly = true;
                }else{
                    isOnly = false;
                }
            }
        });

        radioNoOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    isOnly = true;
                } else {
                    isOnly = false;
                }
            }
        });

        editTextArea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                }else{
                    String Area = editTextArea.getText().toString();
                    if(Area.length()!=0){
                        HouseArea = Double.parseDouble(editTextArea.getText().toString());
                    }else{
                        HouseArea = 0.0;
                    }

                    String UnitPrice = editTextUnitPrice.getText().toString();

                    if(UnitPrice.length()!=0) {
                        AreaUnit = Double.parseDouble(UnitPrice);
                    }else{
                        AreaUnit = 0.0;
                    }
                    editTextAllPrice.setText(ResultActivity.formatFloatNumber(AreaUnit*HouseArea));
                }
            }
        });

        editTextUnitPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                }else{
                    String Area = editTextArea.getText().toString();
                    if(!Area.isEmpty()){
                        HouseArea = Double.parseDouble(editTextArea.getText().toString());
                    }else{
                        HouseArea = 0.0;
                    }

                    String UnitPrice = editTextUnitPrice.getText().toString();
                    if(UnitPrice!="") {
                        AreaUnit = Double.parseDouble(UnitPrice);
                    }else{
                        AreaUnit = 0.0;
                    }
                    editTextAllPrice.setText(ResultActivity.formatFloatNumber(AreaUnit*HouseArea));
                }
            }
        });



        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(editTextArea.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "请输入房子面积", Toast.LENGTH_LONG).show();
                    return ;
                }

                if(editTextUnitPrice.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "请输入房子平米单价", Toast.LENGTH_LONG).show();
                    return ;
                }

                if( OldNew==1) {
                    if (editTextAllPrice.getText().toString().length() != 0 && Double.parseDouble(editTextAllPrice.getText().toString())!=0.0) {
                        HousePrice = Double.parseDouble(editTextAllPrice.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), "请输入房子总价", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(AllOff==2) {
                        if (editTextAGoAllPrice.getText().toString().length() != 0) {
                            AGoHousePrice = Double.parseDouble(editTextAGoAllPrice.getText().toString());
                        } else {
                            Toast.makeText(getActivity(), "请输入房子原价", Toast.LENGTH_LONG).show();
                            return;
                        }

                        HousePrice = HousePrice - AGoHousePrice;
                    }

                }else{

                    HouseArea = Double.parseDouble(editTextArea.getText().toString());
                    AreaUnit = Double.parseDouble(editTextUnitPrice.getText().toString());
                    HousePrice = Double.parseDouble(editTextArea.getText().toString()) * Double.parseDouble(editTextUnitPrice.getText().toString());

                }



                ResultTax result = new ResultTax(getActivity(),R.style.result_dialog,OldNew,HouseType,AllOff,
                       HouseArea, HousePrice,AGoHousePrice,isOver5,isFrist,isOnly);
                result.setHiddenPart(HiddenPart);
                result.setActivity(getActivity());
                result.show();

            }
        });



        final List<HouseTypeData> housetypedatas = new ArrayList<HouseTypeData>();
        HouseTypeData tmp = new HouseTypeData(1,"普通住宅");
        housetypedatas.add(tmp);
        tmp = null;
        tmp = new HouseTypeData(2,"非普通住宅");
        housetypedatas.add(tmp);
        tmp = null;
        tmp = new HouseTypeData(3,"经济适用房");
        housetypedatas.add(tmp);
        tmp = null;



        ArrayAdapter<HouseTypeData> adapterAgeLimit = new ArrayAdapter<HouseTypeData>(getActivity(), android.R.layout.simple_spinner_item, housetypedatas);
        adapterAgeLimit.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerHouseType.setAdapter(adapterAgeLimit);
        spinnerHouseType.setSelection(0);

        spinnerHouseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                HouseType = housetypedatas.get(i).key;
                if(HouseType==3) {
                    layoutOver5.setVisibility(View.GONE);
                }else{
                    layoutOver5.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return v;
    }

    class HouseTypeData{
        private Integer key;
        private String value;

        public HouseTypeData(Integer key,String value){
            this.key = key;
            this.value = value;
        }

        public String toString(){
            return value;
        }

    }

    public void ResetData(){

        radioOld.setChecked(true);
        radioButtonTotal.setChecked(true);
        radioYes5.setChecked(true);
        radioYes1.setChecked(true);
        radioYesOnly.setChecked(true);

        spinnerHouseType.setSelection(0);

        editTextArea.setText("");
        editTextUnitPrice.setText("");
        editTextAllPrice.setText("");
        editTextAGoAllPrice.setText("");


    }

    public void initData(View v){
        //二手房
        radioOld = (RadioButton)v.findViewById(R.id.radioOld);
        //新房
        radioNew = (RadioButton)v.findViewById(R.id.radioNew);
        //计征方式 全额
        radioButtonTotal = (RadioButton)v.findViewById(R.id.radioButtonTotal);
        //计征方式 差额
        radioButtonDiff = (RadioButton)v.findViewById(R.id.radioButtonDiff);
        //已经过五年
        radioYes5 = (RadioButton)v.findViewById(R.id.radioYes5);
        //没有过五年
        radioNo5 = (RadioButton)v.findViewById(R.id.radioNo5);
        //首套房
        radioYes1 = (RadioButton)v.findViewById(R.id.radioYes1);
        //不是首套房
        radioNo1 = (RadioButton)v.findViewById(R.id.radioNo1);
        //家庭唯一住房
        radioYesOnly = (RadioButton)v.findViewById(R.id.radioYesOnly);
        //不是家庭唯一住房
        radioNoOnly = (RadioButton)v.findViewById(R.id.radioNoOnly);


        //房子面积
        editTextArea = (EditText)v.findViewById(R.id.editTextArea);
        //每平方单价
        editTextUnitPrice = (EditText)v.findViewById(R.id.editTextUnitPrice);
        //房子总价
        editTextAllPrice = (EditText)v.findViewById(R.id.editTextAllPrice);
        //房子原价
        editTextAGoAllPrice = (EditText)v.findViewById(R.id.editTextAGoAllPrice);

        //房子的性质
        spinnerHouseType = (Spinner)v.findViewById(R.id.spinnerHouseType);

        //二手房部分
        layoutOld = (LinearLayout)v.findViewById(R.id.layoutOld);
        //原房子价格部分
        layoutOldHouse = (LinearLayout)v.findViewById(R.id.layoutOldHouse);
        //过5年部分
        layoutOver5 = (LinearLayout)v.findViewById(R.id.layoutOver5);

        HiddenPart = (LinearLayout)v.findViewById(R.id.HiddenPart);

        calculate = (ImageButton)v.findViewById(R.id.calculate);

        isOver5 = true;
        isFrist = true;
        isOnly = true;

        HouseArea = 0.0;
        AreaUnit = 0.0;
        HousePrice = 0.0;
        AGoHousePrice = 0.0;

        HouseType = 1;
        OldNew = 1;
        AllOff =1;
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
