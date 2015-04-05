package com.yt.loaninterestrate.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.yt.loaninterestrate.Main;
import com.yt.loaninterestrate.MainActivity;
import com.yt.loaninterestrate.R;
import com.yt.loaninterestrate.tools.DataBaseHelp;
import com.yt.loaninterestrate.tools.InterestRate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


enum FormulaMode{
        MEONY,AREA
}

public class Busines extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FormulaMode formulaMode;
    private RadioButton radiolimit, radioarea, radioButtonAset, radioButtonTwoset;
    private LinearLayout layoutMoney, layoutArea;
    private Spinner spinnerAgeLimit, spinnerInterestRate, spinnerDownPayment,spinnerInterestRateSell;
    private EditText editTextLoanAmount, editTextUnitPrice, editTextArea, editTextRate;
    private ImageButton buttonCalculate;

    private OnFragmentInteractionListener mListener;


    private double loanMoney, loanYear, loanRate ,areaHouse, squareHouse;

    private Integer downPayment;

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
        formulaMode = FormulaMode.MEONY;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_busines, container, false);

        initData(v);
        Main.initTool(v);


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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //利率折扣
        final List<interestRateSellData> interestrateselldatas = new ArrayList<>();
        //利率折扣
        interestrateselldatas.add(new interestRateSellData(0,"不打折"));
        for (int i=9;i>=1;i--){
            interestRateSellData tmp = new interestRateSellData(i,i+"折");
            interestrateselldatas.add(tmp);
            tmp = null;
        }

        ArrayAdapter<InterestRate> adapterInterestRate = new ArrayAdapter<InterestRate>(getActivity(), android.R.layout.simple_spinner_item, MainActivity.interestratess);
        adapterInterestRate.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerInterestRate.setAdapter(adapterInterestRate);
        spinnerInterestRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                double tmp =  Double.parseDouble(interestrateselldatas.get(spinnerInterestRateSell.getSelectedItemPosition()).key.toString());
                if(tmp==0){
                    double tmp1 = MainActivity.interestratess.get(i).getRate(loanYear);
                    editTextRate.setText(ResultActivity.get4s5r(1*tmp1,2)+"");
                }else{
                    double tmp1 = MainActivity.interestratess.get(i).getRate(loanYear);
                    editTextRate.setText(ResultActivity.get4s5r(tmp*0.1*tmp1,2)+"");
                }

               // Toast.makeText(getActivity(), MainActivity.interestratess.get(i).toString() + "", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        ArrayAdapter<interestRateSellData> adapterInterestRateSell = new ArrayAdapter<interestRateSellData>(getActivity(),android.R.layout.simple_dropdown_item_1line,interestrateselldatas);
        adapterInterestRateSell.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerInterestRateSell.setAdapter(adapterInterestRateSell);
        spinnerInterestRateSell.setSelection(0);
        spinnerInterestRateSell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==0){
                    double tmp =  1;//不打折
                    double tmp1 = MainActivity.interestratess.get(spinnerInterestRate.getSelectedItemPosition()).getRate(loanYear);
                    editTextRate.setText(ResultActivity.get4s5r(tmp*tmp1,2)+"");
                }else{
                    double tmp =  Double.parseDouble(interestrateselldatas.get(i).key.toString());
                    double tmp1 = MainActivity.interestratess.get(spinnerInterestRate.getSelectedItemPosition()).getRate(loanYear);
                    editTextRate.setText(ResultActivity.get4s5r(tmp*0.1*tmp1,2)+"");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //首付
        final List<DownPayment> downpayments = new ArrayList<DownPayment>();
        for (int i = 2; i <= 9; i++) {
            DownPayment tmp = new DownPayment(i, i + "成");
            downpayments.add(tmp);
            tmp = null;
        }
        ArrayAdapter adapterDownPayment = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, downpayments);
        spinnerDownPayment.setAdapter(adapterDownPayment);
        spinnerDownPayment.setSelection(1);
        spinnerDownPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(getActivity(), downpayments.get(i).toString() + "", Toast.LENGTH_LONG).show();
                downPayment = downpayments.get(i).key;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //处理贷款金额计算方式
        radiolimit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radiolimit.isChecked()) {
                    layoutMoney.setVisibility(View.VISIBLE);
                    layoutArea.setVisibility(View.GONE);
                    formulaMode = FormulaMode.MEONY;
                }

            }
        });

        //处理面积计算方式
        radioarea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radioarea.isChecked()) {
                    layoutArea.setVisibility(View.VISIBLE);
                    layoutMoney.setVisibility(View.GONE);
                    formulaMode = FormulaMode.AREA;
                }
            }
        });


        radioButtonAset.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radioButtonAset.isChecked()) {
                    //Toast.makeText(getActivity(), "选择了一套", Toast.LENGTH_LONG).show();
                    spinnerDownPayment.setSelection(1);
                }
            }
        });

        radioButtonTwoset.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(radioButtonTwoset.isChecked()) {
                    //Toast.makeText(getActivity(), "选择了二套", Toast.LENGTH_LONG).show();
                    spinnerDownPayment.setSelection(4);
                };
            }
        });

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editTextRate.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"请输入贷款利率",Toast.LENGTH_LONG).show();
                    return;
                }
                loanRate = Double.parseDouble(editTextRate.getText().toString()) / 100;

                if(formulaMode == FormulaMode.MEONY) {
                    if(editTextLoanAmount.getText().toString().isEmpty()){
                        Toast.makeText(getActivity(),"请输入贷款额度",Toast.LENGTH_LONG).show();
                        return;
                    }
                    loanMoney = Double.parseDouble(editTextLoanAmount.getText().toString());
                }else{
                    //downPayment首付
                    if (editTextArea.getText().toString().isEmpty()){
                        Toast.makeText(getActivity(), "请输入房子的面积", Toast.LENGTH_LONG).show();
                        return;
                    }
                    areaHouse = Double.parseDouble(editTextArea.getText().toString());

                    if(editTextUnitPrice.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "请输入房子的单价", Toast.LENGTH_LONG).show();
                        return;
                    }
                    squareHouse = Double.parseDouble(editTextUnitPrice.getText().toString());

                    loanMoney = (areaHouse*squareHouse)*(1-(0.1*downPayment));

                }

                ResultActivity resultTip = new ResultActivity(getActivity(),R.style.result_dialog,loanRate,loanMoney,loanYear);

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

        radiolimit = (RadioButton) v.findViewById(R.id.radiolimit);//贷款额度按钮
        radioarea = (RadioButton) v.findViewById(R.id.radioarea);//面积按钮

        radioButtonAset = (RadioButton) v.findViewById(R.id.radioButtonAset);//一套
        radioButtonTwoset = (RadioButton) v.findViewById(R.id.radioButtonTwoSet);//二套


        editTextLoanAmount = (EditText) v.findViewById(R.id.editTextLoanAmount);//贷款金额
        editTextUnitPrice = (EditText) v.findViewById(R.id.editTextUnitPrice);//每平方单价
        editTextArea = (EditText) v.findViewById(R.id.editTextArea);//房子面积
        editTextRate = (EditText) v.findViewById(R.id.editTextRate);//利率值

        layoutMoney = (LinearLayout) v.findViewById(R.id.layoutMoney);//贷款金额区域
        layoutArea = (LinearLayout) v.findViewById(R.id.layoutArea);//面积区域

        spinnerAgeLimit = (Spinner) v.findViewById(R.id.spinnerAgeLimit);//贷款年限选择列表
        spinnerInterestRate = (Spinner) v.findViewById(R.id.spinnerInterestRate);//年利率选择列表
        spinnerDownPayment = (Spinner) v.findViewById(R.id.spinnerDownPayment);//首付选择列表
        spinnerInterestRateSell = (Spinner)v.findViewById(R.id.spinnerInterestRateSell);//利率折扣

        buttonCalculate = (ImageButton) v.findViewById(R.id.calculate);//计算按钮

        loanMoney = 0.0;
        loanYear = 9;
        loanRate = 0.0;

        areaHouse = 0.0;
        downPayment = 3;
        areaHouse = 0.0;


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




    //首付金额类
    public class DownPayment {
        public Integer key;
        public String value;

        public DownPayment(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
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
