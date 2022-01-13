package com.example.sharekhancalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button b1;
    TextView stt, gst, brk, sebi, duty, txn;
    EditText e1, e2, e3, bkr;
    int qty;
    RadioGroup grp;
    RadioButton intr, del;
    float buy, sell, bk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.calc);
        e1 = (EditText) findViewById(R.id.et1);
        e2 = (EditText) findViewById(R.id.et2);
        e3 = (EditText) findViewById(R.id.et3);
        bkr = (EditText) findViewById(R.id.et4);
        stt = (TextView) findViewById(R.id.STT);
        gst = (TextView) findViewById(R.id.gst);
        brk = (TextView) findViewById(R.id.brokerage);
        sebi = (TextView) findViewById(R.id.sebi);
        duty = (TextView) findViewById(R.id.duty);
        txn = (TextView) findViewById(R.id.Txn);

        grp = (RadioGroup) findViewById(R.id.grp);
        intr = (RadioButton) findViewById(R.id.intr);
        del = (RadioButton) findViewById(R.id.delivery);


        intr.setChecked(true);
    }

    public void calculate(View v) {
        if (del.isSelected()) {
            del.setChecked(true);
            intr.setChecked(false);

            delivery(v);
        }
   else if (intr.isSelected()) {
            intr.setChecked(true);

            del.setChecked(false);

            intraday(v);
        }    }

    public void intraday(View v) {
        intr.setChecked(true);
        del.setChecked(false);
        init();
        double brokerage, gstt, sttt, dutyy, txnn, sbi;
        brokerage = ((buy * bk) / 100) + ((sell * bk) / 100);
        sttt = ((sell) * .025) / 100;
        dutyy = ((buy + sell) * .015) / 100;
        txnn = ((buy + sell) * .00325) / 100;
        sbi = ((buy + sell) * .0001) / 100;
        gstt = ((brokerage  + txnn + sbi) * 18) / 100;
        brk.setText("Brokerage = " + brokerage);
        stt.setText("STT = " + sttt);
        txn.setText("Transaction Charges = " + txnn);
        sebi.setText("SEBI Charges = " + sbi);
        duty.setText("Stamp Duty = " + dutyy);
        gst.setText("GST = " + gstt);
    }

    public void delivery(View v) {
        del.setChecked(true);
        intr.setChecked(false);
        init();
        double brokerage, gstt, sttt, dutyy, txnn, sbi;
        brokerage = ((buy * bk) / 100) + ((sell * bk) / 100);
        sttt = ((buy + sell) * .1) / 100;
        dutyy = ((buy + sell) * .0195) / 100;
        txnn = ((buy + sell) * .00325) / 100;
        sbi = ((buy + sell) * .0001) / 100;
        gstt = ((brokerage + txnn + sbi) * 18) / 100;

        brk.setText("Brokerage = " + brokerage);

        stt.setText("STT = " + sttt);
        txn.setText("Transaction Charges = " + txnn);
        sebi.setText("SEBI Charges = " + sbi);
        duty.setText("Stamp Duty = " + dutyy);
        gst.setText("GST = " + gstt);
    }

    public void init() {
        if (e3.getText().toString().isEmpty())
            qty = 0;
        else
            qty = Integer.parseInt(e3.getText().toString());
        if (e1.getText().toString().isEmpty())
            buy = 0;
        else
            buy = Float.parseFloat(e1.getText().toString()) * qty;
        if (e2.getText().toString().isEmpty())
            sell = 0;
        else
            sell = Float.parseFloat(e2.getText().toString()) * qty;
        if (e1.getText().toString().isEmpty())
            bk = 0;
        else
            bk = Float.parseFloat(bkr.getText().toString()) / 100;
    }
}