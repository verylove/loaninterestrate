package com.yt.loaninterestrate;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Busines extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RadioButton radiolimit,radioarea,radioButtonAset,radioButtonTwoset;
    private LinearLayout layoutMoney,layoutArea;
    private Spinner spinnerAgeLimit,spinnerInterestRate,spinnerDownPayment;
    private EditText editTextLoanAmount,editTextUnitPrice,editTextArea,editTextRate;

    private OnFragmentInteractionListener mListener;


    public static Busines newInstance(String param1, String param2) {
        Busines fragment = new Busines();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Busines() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_busines, container, false);
            radiolimit  = (RadioButton)v.findViewById(R.id.radiolimit);//贷款额度按钮
            radioarea  = (RadioButton)v.findViewById(R.id.radioarea);//面积按钮

            radioButtonAset = (RadioButton)v.findViewById(R.id.radioButtonAset);//一套
            radioButtonTwoset = (RadioButton)v.findViewById(R.id.radioButtonTwoSet);//二套


            editTextLoanAmount = (EditText)v.findViewById(R.id.editTextLoanAmount);//贷款金额
            editTextUnitPrice = (EditText)v.findViewById(R.id.editTextUnitPrice);//每平方单价
            editTextArea = (EditText)v.findViewById(R.id.editTextArea);//房子面积
            editTextRate = (EditText)v.findViewById(R.id.editTextRate);//利率值

            layoutMoney = (LinearLayout)v.findViewById(R.id.layoutMoney);//贷款金额区域
            layoutArea = (LinearLayout)v.findViewById(R.id.layoutArea);//面积区域

            spinnerAgeLimit = (Spinner)v.findViewById(R.id.spinnerAgeLimit);//贷款年限选择列表

            //贷款年限
            final List<AgeLimitData> agelimitdatas = new ArrayList<AgeLimitData>();

            for(int i =1;i<=30;i++){
                AgeLimitData tmp =  new AgeLimitData(i,i + "年（" + i * 12 + "期）");
                agelimitdatas.add(tmp);
                tmp = null;
            }
            ArrayAdapter<AgeLimitData> adapterAgeLimit = new ArrayAdapter<AgeLimitData>(getActivity(),android.R.layout.simple_spinner_item,agelimitdatas);
            spinnerAgeLimit.setAdapter(adapterAgeLimit);

            spinnerAgeLimit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getActivity(),agelimitdatas.get(i).toString()+"",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            //年利率
            spinnerInterestRate = (Spinner)v.findViewById(R.id.spinnerInterestRate);//年利率选择列表
            final List<InterestRate> interestratess = new ArrayList<InterestRate>();

            for(int i=9;i>0;i--){
                InterestRate tmp = new InterestRate(i,"201" + i + "年",i*1.0);
                interestratess.add(tmp);
                tmp = null;
            }

            ArrayAdapter<InterestRate> adapterInterestRate = new ArrayAdapter<InterestRate>(getActivity(),android.R.layout.simple_spinner_item,interestratess);
            spinnerInterestRate.setAdapter(adapterInterestRate);
            spinnerInterestRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    editTextRate.setText(interestratess.get(i).rate+"%");
                    Toast.makeText(getActivity(),interestratess.get(i).toString()+"",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            //首付
            spinnerDownPayment = (Spinner)v.findViewById(R.id.spinnerDownPayment);//首付选择列表
            final List<DownPayment> downpayments = new ArrayList<DownPayment>();
            for(int i=2;i<=9;i++){
                DownPayment tmp = new DownPayment(i,i+"成");
                downpayments.add(tmp);
                tmp = null;
            }
            ArrayAdapter adapterDownPayment = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,downpayments);
            spinnerDownPayment.setAdapter(adapterDownPayment);
            spinnerDownPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getActivity(),downpayments.get(i).toString()+"",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            //处理贷款金额计算方式
            radiolimit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(radiolimit.isChecked()){
                            layoutMoney.setVisibility(View.VISIBLE);
                            layoutArea.setVisibility(View.GONE);
                    }

                }
            });

            //处理面积计算方式
            radioarea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(radioarea.isChecked()){
                        layoutArea.setVisibility(View.VISIBLE);
                        layoutMoney.setVisibility(View.GONE);

                    }
                }
            });


        radioButtonAset.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(radioButtonAset.isChecked()){
                    Toast.makeText(getActivity(),"选择了一套",Toast.LENGTH_LONG).show();
                }
            }
        });

        radioButtonTwoset.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(getActivity(),"选择了二套",Toast.LENGTH_LONG).show();
            }
        });


        return v;
    }

    //贷款年限类
    public class AgeLimitData{

            Integer key;
            String value;

            public AgeLimitData(Integer key,String value){
                this.key = key;
                this.value = value;
            }

            public  String toString(){
                return value;
            }

    }

    //贷款利率类
    public class InterestRate{
        public Integer id;
        public String value;
        public Double rate;

        public InterestRate(Integer id,String value,Double rate){
            this.id = id;
            this.value = value;
            this.rate = rate;
        }

        public String toString(){
            return value;
        }


    }

    //首付金额类
    public class DownPayment{
        public  Integer key;
        public  String value;

        public DownPayment(Integer key,String value){
            this.key = key;
            this.value = value;
        }

        public String toString(){
            return value;
        }

    }


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


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
