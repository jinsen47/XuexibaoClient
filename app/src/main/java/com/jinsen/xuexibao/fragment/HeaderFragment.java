package com.jinsen.xuexibao.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.jinsen.xuexibao.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeaderFragment.OnChoose} interface
 * to handle interaction events.
 * Use the {@link HeaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeaderFragment extends Fragment {

    private RadioButton mGoodBtn;
    private RadioButton mFineBtn;
    private RadioButton mBadBtn;
    private Spinner mSpinner;

    private String submit;

    private OnChoose mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HeaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HeaderFragment newInstance() {
        HeaderFragment fragment = new HeaderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HeaderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View headerFragment = inflater.inflate(R.layout.fragment_header, container, false);

        mGoodBtn = ((RadioButton) headerFragment.findViewById(R.id.good));
        mFineBtn = ((RadioButton) headerFragment.findViewById(R.id.fine));
        mSpinner = ((Spinner) headerFragment.findViewById(R.id.spinner));

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner, android.R.layout.simple_spinner_item);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Spinner", position + "");

                String tmp = ",3,";
                switch (position) {
                    case 0: submit = tmp + "1";break;
                    case 1: submit = tmp + "2";break;
                    case 2: submit = tmp + "4,念答案";break;
                    case 3: submit = tmp + "4,音量小";break;
                    case 4: submit = tmp + "3";break;
                    case 5: submit = tmp + "4,不要录英语";break;
                    case 6: submit = tmp + "4,其他";break;
                    default: break;
                }
                onCheck(submit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mGoodBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) submit = ",1";
                onCheck(submit);
            }
        });

        mFineBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) submit = ",2";
                onCheck(submit);
            }
        });

        // Inflate the layout for this fragment
        return headerFragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onCheck(String submit) {
        if (mListener != null) {
            mListener.onChoose(submit);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnChoose) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String getSubmit() {
        Log.d("HeaderFragment", submit);
        if (submit == null)
            return ",2";
        return submit;
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
    public interface OnChoose {
        // TODO: Update argument type and name
        public void onChoose(String submit);
    }

}
