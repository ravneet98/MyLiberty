package com.example.myliberty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myliberty.Models.Customer;
import com.example.myliberty.Models.Message;
import com.example.myliberty.Models.Plan;
import com.example.myliberty.Utils.dateToDaysUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.PaymentButtonIntent;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.config.UIConfig;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillingFragment#} factory method to
 * create an instance of this fragment.
 */
public class BillingFragment extends Fragment {
    TextView name,accountNumber,paymentLastDate,paymentAmount;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    String uid;
    Button makePayment;
    String _paymentAmount,outstandingPayment;
    PaymentButtonContainer paymentButtonContainer;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    ProgressBar progressBar;

    Customer customer;
    private static final String clientID = "AQUoNG5bje_mCjO9c1Qb_G3xiQxs-auBoxfi_NFMtfscDmaHXfcnGiWXfxlsXQ5XsrQH_bz5GBu_0IAU";

    public BillingFragment() {
        // Required empty public constructor

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_billing, container, false);
        mAuth = FirebaseAuth.getInstance();
        uid= mAuth.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference("accountInfo").child(uid);
        mDatabase.keepSynced(true);
        name=view.findViewById(R.id.name);
        accountNumber=view.findViewById(R.id.accountNumber);
        paymentLastDate=view.findViewById(R.id.paymentLastDate);
        paymentAmount=view.findViewById(R.id.paymentAmount);
        paymentButtonContainer=view.findViewById(R.id.payment_button_container);
        progressBar=view.findViewById(R.id.progressBar);
        paymentButtonContainer.setup(
                createOrderActions -> {
                    ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                    purchaseUnits.add(
                            new PurchaseUnit.Builder()
                                    .amount(
                                            new Amount.Builder()
                                                    .currencyCode(CurrencyCode.CAD)
                                                    .value(outstandingPayment)
                                                    .build()
                                    ).build()
                    );
                    OrderRequest order = new OrderRequest(
                            OrderIntent.CAPTURE,
                            new AppContext.Builder()
                                    .userAction(UserAction.PAY_NOW)
                                    .build(),
                            purchaseUnits
                    );
                    createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                },
                approval -> {
                    Log.i("paymentClick", "OrderId: " + approval.getData().getOrderId());
                    Toast.makeText(getContext(),"Payment Successful",Toast.LENGTH_SHORT).show();

                    updateData(customer,mDatabase);
                }
        );

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM, dd yyyy");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    customer=snapshot.getValue(Customer.class);
                    name.setText(customer.getName());
                    accountNumber.setText(customer.getAccountNumber());
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MMM, dd yyyy");
                    if(customer.getBillPaid()){
                        paymentButtonContainer.setVisibility(View.GONE);
                    }

                String dateString = customer.getCycleEndDate();

                try {
                    Date date = inputFormat.parse(dateString);
                    String formattedDate = outputFormat.format(date);
                  paymentLastDate.setText(formattedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("plans").child(customer.getPlanId());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Plan plan=snapshot.getValue(Plan.class);
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                        outstandingPayment=String.valueOf(
                                df.format(
                                        Float.parseFloat(plan.getPlanCost())/30*
                                                dateToDaysUtility.calculateDifference(
                                                        customer.getCycleStartDate(),
                                                        timestamp.toString(),"days")
                                ));


                            if(!customer.getBillPaid()&&!outstandingPayment.equals("0.00")){
                              paymentButtonContainer.setVisibility(View.VISIBLE);
                            }
                            _paymentAmount=plan.getPlanCost();
                            paymentAmount.setText("$"+outstandingPayment);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }


public void updateData(Customer customer,DatabaseReference mDatabase){
    progressBar.setVisibility(View.VISIBLE);
    Timestamp cycleDate=dateToDaysUtility.stringToTimestamp(customer.getCycleEndDate());
    Customer _customer=new Customer(customer.getAccountNumber(),
            customer.getName(),
            customer.getEmail(),
            customer.getPlanId(),
            customer.getMobileNumber(),
            dateToDaysUtility.addDays(cycleDate,1).toString(),
            dateToDaysUtility.addDays(cycleDate,31).toString(),
            true,
            customer.getMaxData(),
            customer.getMaxData());
    mDatabase.setValue(_customer).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                _paymentAmount="0";
                outstandingPayment="0";
                paymentButtonContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }else{

            }
        }
    });
}
}