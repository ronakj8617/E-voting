package com.example.sharekhancalc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Eligibility#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Eligibility extends Fragment {
    Button close;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView name, epic, dob, pl_const, asmbly_const, local_const, allowed;
    DatabaseReference rf;

    public Eligibility() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Eligibility.
     */
    // TODO: Rename and change types and number of parameters
    public static Eligibility newInstance(String param1, String param2) {
        Eligibility fragment = new Eligibility();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_eligibility, container, false);



        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rf = FirebaseDatabase.getInstance().getReference("Voters");

        name = getActivity().findViewById(R.id.name);
        epic = getActivity().findViewById(R.id.epic);
        pl_const = getActivity().findViewById(R.id.pl_const);
        asmbly_const = getActivity().findViewById(R.id.asmbly_const);
        local_const = getActivity().findViewById(R.id.local_const);
        dob = getActivity().findViewById(R.id.DOB);
        allowed = getActivity().findViewById(R.id.allowed);

        Check_Eligibility c1 = (Check_Eligibility) getActivity();
        Firebase_Voter_Registration f1 = c1.getData();

        name.setText("Name : " + f1.getName());
        epic.setText("EPIC NO.: "+f1.getEpic());
        asmbly_const.setText("Assembly Constituency: "+f1.getAssembly());
        local_const.setText("Local Constituency: "+f1.getLocal());
        pl_const.setText("Parliament Constituency: "+f1.getParliament());
        allowed.setText("Allowed for E-voting: "+f1.getIsAllowed());
        dob.setText("DOB: "+f1.getDob());

        close = getActivity().findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getView().setVisibility(View.GONE);
                getActivity().getFragmentManager().popBackStack();

            }
        });

    }
}