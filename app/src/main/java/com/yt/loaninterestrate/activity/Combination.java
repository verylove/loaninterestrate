package com.yt.loaninterestrate.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.yt.loaninterestrate.Main;
import com.yt.loaninterestrate.MainActivity;
import com.yt.loaninterestrate.R;
import com.yt.loaninterestrate.tools.InterestRate;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Combination.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Combination#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Combination extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Spinner spinnerAgeLimit, spinnerSYInterestRate,spinnerGJJInterestRate,spinnerInterestRateSell;
    private EditText editTextSYLoanAmount, editTextSYRate,editTextGJJLoanAmount, editTextGJJRate;
    private ImageButton buttonCalculate;

    private double loanSYMoney,loanGJJMoney, loanYear, loanSYRate ,loanGJJRate , sellMoney;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Combination.
     */
    // TODO: Rename and change types and number of parameters
    public static Combination newInstance(String param1, String param2) {
        Combination fragment = new Combination();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Combination() {
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
         View v =inflater.inflate(R.layout.fragment_combination, container, false);
         Main.initTool(v);
        initData(v);

        //利率折扣
        final List<interestRateSellData> interestrateselldatas = new ArrayList<>();
        //贷款年限
        final List<AgeLimitData> agelimitdatas = new ArrayList<AgeLimitData>();

        for (int i = 1; i <= 30; i++) {
            AgeLimitData tmp = new AgeLimitData(i, i + "年（" + i * 12 + "期）");
            agelimitdatas.add(tmp);
            tmp = null;
        }
        ArrayAdapter<AgeLimitData> adapterAgeLimit = new ArrayAdapter<AgeLimitData>(getActivity(), android.R.layout.simple_spinner_item, agelimitdatas);
        adapterAgeLimit.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerAgeLimit.setAdapter(adapterAgeLimit);
        spinnerAgeLimit.setSelection(9);

        spinnerAgeLimit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), agelimitdatas.get(i).toString() + "", Toast.LENGTH_LONG).show();
                //取到贷款年数
                loanYear = agelimitdatas.get(i).key;


                double tmp11 = MainActivity.interestratess.get(spinnerGJJInterestRate.getSelectedItemPosition()).getRate(loanYear,1);
                editTextGJJRate.setText(ResultActivity.get4s5r(1 * tmp11, 2) + "");

                double tmp =  sellMoney;
                if(spinnerSYInterestRate.getSelectedItemPosition()!=-1) {
                    if (tmp == 0) {
                        double tmp1 = MainActivity.interestratess.get(spinnerSYInterestRate.getSelectedItemPosition()).getRate(loanYear);
                        editTextSYRate.setText(ResultActivity.get4s5r(1 * tmp1, 2) + "");
                    } else {
                        double tmp1 = MainActivity.interestratess.get(spinnerSYInterestRate.getSelectedItemPosition()).getRate(loanYear);
                        editTextSYRate.setText(ResultActivity.get4s5r(tmp * 0.1 * tmp1, 2) + "");
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //利率折扣
        interestrateselldatas.add(new interestRateSellData(0,"不打折"));
        for (int i=9;i>=1;i--){
            interestRateSellData tmp = new interestRateSellData(i,i+"折");
            interestrateselldatas.add(tmp);
            tmp = null;
        }

        //商业利率
        ArrayAdapter<InterestRate> adapterInterestRate = new ArrayAdapter<InterestRate>(getActivity(), android.R.layout.simple_spinner_item, MainActivity.interestratess);
        //商业
        adapterInterestRate.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerSYInterestRate.setAdapter(adapterInterestRate);
        spinnerSYInterestRate.setSelection(0);
        spinnerSYInterestRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                double tmp =  sellMoney;
                if(tmp==0){
                    double tmp1 = MainActivity.interestratess.get(i).getRate(loanYear);
                    editTextSYRate.setText(ResultActivity.get4s5r(1*tmp1,2)+"");
                }else{
                    double tmp1 = MainActivity.interestratess.get(i).getRate(loanYear);
                    editTextSYRate.setText(ResultActivity.get4s5r(tmp*0.1*tmp1,2)+"");
                }

                // Toast.makeText(getActivity(), MainActivity.interestratess.get(i).toString() + "", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //公积金
        spinnerGJJInterestRate.setAdapter(adapterInterestRate);
        spinnerGJJInterestRate.setSelection(0);
        spinnerGJJInterestRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    double tmp1 = MainActivity.interestratess.get(i).getRate(loanYear,1);
                    editTextGJJRate.setText(ResultActivity.get4s5r(tmp1,2)+"");

                // Toast.makeText(getActivity(), MainActivity.interestratess.get(i).toString() + "", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        ArrayAdapter<interestRateSellData> adapterInterestRateSell = new ArrayAdapter<interestRateSellData>(getActivity(),android.R.layout.simple_spinner_item,interestrateselldatas);
        adapterInterestRateSell.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerInterestRateSell.setAdapter(adapterInterestRateSell);
        spinnerInterestRateSell.setSelection(0);
        spinnerInterestRateSell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                sellMoney =  Double.parseDouble(interestrateselldatas.get(i).key.toString());
                if(spinnerSYInterestRate.getSelectedItemPosition()!=-1) {
                    if (i == 0) {
                        double tmp = 1;//不打折
                        double tmp1 = MainActivity.interestratess.get(spinnerSYInterestRate.getSelectedItemPosition()).getRate(loanYear);
                        editTextSYRate.setText(ResultActivity.get4s5r(tmp * tmp1, 2) + "");
                    } else {
                        double tmp = sellMoney;
                        double tmp1 = MainActivity.interestratess.get(spinnerSYInterestRate.getSelectedItemPosition()).getRate(loanYear);
                        editTextSYRate.setText(ResultActivity.get4s5r(tmp * 0.1 * tmp1, 2) + "");
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editTextSYRate.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"请输入商业贷款贷款利率",Toast.LENGTH_LONG).show();
                    return;
                }
                loanSYRate = Double.parseDouble(editTextSYRate.getText().toString()) / 100;

                if(editTextSYLoanAmount.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"请输入商业贷款额度",Toast.LENGTH_LONG).show();
                    return;
                }
                loanSYMoney = Double.parseDouble(editTextSYLoanAmount.getText().toString());

                if(editTextGJJRate.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"请输入公积金贷款利率",Toast.LENGTH_LONG).show();
                    return;
                }
                loanGJJRate = Double.parseDouble(editTextGJJRate.getText().toString()) / 100;


                if(editTextGJJLoanAmount.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"请输入公积金贷款额度",Toast.LENGTH_LONG).show();
                    return;
                }

                loanGJJMoney = Double.parseDouble(editTextGJJLoanAmount.getText().toString());


                ResultActivity resultTip = new ResultActivity(getActivity(),R.style.result_dialog,loanSYRate,loanSYMoney,loanGJJRate,loanGJJMoney,loanYear);

                resultTip.setActivity(getActivity());
                Window dialogWindow = resultTip.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

                lp.x = 35; // 新位置X坐标
                lp.y = 650; // 新位置Y坐标
                lp.width = 650; // 宽度
                lp.height = 600; // 高度
                lp.alpha = 1.0f; // 透明度

                dialogWindow.setAttributes(lp);

                resultTip.show();



            }
        });




                return v;
    }


    public void initData(View v){

        spinnerAgeLimit = (Spinner) v.findViewById(R.id.spinnerAgeLimit);//贷款年限选择列表

        editTextSYLoanAmount = (EditText) v.findViewById(R.id.editTextSYLoanAmount);//贷款金额
        editTextGJJLoanAmount = (EditText) v.findViewById(R.id.editTextGJJLoanAmount);//公积金贷款金额

        editTextSYRate = (EditText) v.findViewById(R.id.editTextSYRate);//商业贷款利率值
        editTextGJJRate = (EditText) v.findViewById(R.id.editTextGJJRate);//公积金款利率值

        spinnerSYInterestRate = (Spinner) v.findViewById(R.id.spinnerSYInterestRate);//商业贷款年利率选择列表
        spinnerGJJInterestRate = (Spinner) v.findViewById(R.id.spinnerGJJInterestRate);//公积金年利率选择列表

        spinnerInterestRateSell = (Spinner)v.findViewById(R.id.spinnerInterestRateSell);//利率折扣

        buttonCalculate = (ImageButton) v.findViewById(R.id.calculate);//计算按钮

        loanSYMoney = 0.0;
        loanGJJMoney = 0.0;
        loanYear = 9;
        loanSYRate = 0.0;
        loanGJJRate = 0.0;
        sellMoney = 0;



    }

    //利率折扣类
    public class interestRateSellData{
        Integer key;
        String value;

        public interestRateSellData(Integer key,String value){
            this.key = key;
            this.value = value;
        }

        public String toString(){return value;}
    }


    //贷款年限类
    public class AgeLimitData {

        Integer key;
        String value;

        public AgeLimitData(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return value;
        }

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
