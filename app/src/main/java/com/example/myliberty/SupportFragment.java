package com.example.myliberty;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myliberty.Adapter.planAdapter;
import com.example.myliberty.Adapter.supportAdapter;
import com.example.myliberty.Models.Plan;
import com.example.myliberty.Models.SupportQueries;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;


public class SupportFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference mDatabase;
    supportAdapter supportAdapter;
    ArrayList<SupportQueries> supportQueries;
    ImageButton message,addQuery;
    Button submit,back;
    LinearLayout add_query_layout;
    EditText query;
    FirebaseAuth mAuth;
    Animation animShow, animHide;
    public SupportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_support, container, false);
        recyclerView=view.findViewById(R.id.query_view);
        add_query_layout=view.findViewById(R.id.add_query_layout);
        message=view.findViewById(R.id.message);
        submit=view.findViewById(R.id.submit);
        addQuery=view.findViewById(R.id.add);
        back=view.findViewById(R.id.back);
        query=view.findViewById(R.id.query);
        animShow = AnimationUtils.loadAnimation( getContext(), R.anim.view_show);
        animHide = AnimationUtils.loadAnimation( getContext(), R.anim.view_hide);

        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference("queries");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        supportQueries=new ArrayList<>();
        supportAdapter=new supportAdapter(getContext(),supportQueries);
        recyclerView.setAdapter(supportAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerView.getVisibility()==View.GONE){
                    recyclerView.startAnimation(animShow);
                    recyclerView.setVisibility(View.VISIBLE);
                    add_query_layout.startAnimation(animHide);
                    add_query_layout.setVisibility(View.GONE);

                }
            }
        });
        addQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(add_query_layout.getVisibility()==View.GONE){
                    recyclerView.startAnimation(animHide);
                    recyclerView.setVisibility(View.GONE);
                    add_query_layout.startAnimation(animShow);
                    add_query_layout.setVisibility(View.VISIBLE);

                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(query.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please add something is query field",Toast.LENGTH_SHORT).show();
                }else{
                    submit_query();
                    hideKeyboard(getActivity());
                }

            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageFragment messageFragment=new MessageFragment();

                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flFragment, messageFragment);
                fragmentTransaction.commit();
            }
        });
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    SupportQueries query=dataSnapshot.getValue(SupportQueries.class);


                    supportQueries.add(query);
                }
                supportAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }
    public void submit_query(){
        String _query= query.getText().toString();

      String key = mDatabase.push().getKey();
      SupportQueries obj=new SupportQueries(key,_query,"",new Timestamp(System.currentTimeMillis()).toString(),"","in Progress",mAuth.getUid());

        mDatabase.child(key).setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(),"Query submitted successfully",Toast.LENGTH_SHORT).show();
                    if(recyclerView.getVisibility()==View.GONE){
                        recyclerView.startAnimation(animShow);
                        recyclerView.setVisibility(View.VISIBLE);
                        add_query_layout.startAnimation(animHide);
                        add_query_layout.setVisibility(View.GONE);

                    }
                }
            }
        });


    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}