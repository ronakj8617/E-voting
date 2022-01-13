package com.example.sharekhancalc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Check_Fingerprint#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Check_Fingerprint extends Fragment implements MFS100Event, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<byte[]> al1, al;

    byte[] caught, c1;
    Firebase_Fingerprrint f2, f1;
    Button btnInit;
    Button btnUninit;
    Button btnStopCapture;
    Button btnMatchISOTemplate;
    Button btnExtractISOImage;
    Button btnExtractAnsi;
    Button btnClearLog;
    Button btnExtractWSQImage;
    TextView lblMessage;
    Button btnSyncCapture;
    EditText txtEventLog;
    ImageView imgFinger;
    Button match;
    CheckBox cbFastDetection;
    private static long mLastClkTime = 0;
    private static long Threshold = 1500;
    Uri path;

    @Override
    public void onClick(View v) {

    }

    private enum ScannerAction {
        Capture, Verify
    }

    byte[] Enroll_Template;
    byte[] Verify_Template;
    private FingerData lastCapFingerData = null;
    Check_Fingerprint.ScannerAction scannerAction = Check_Fingerprint.ScannerAction.Capture;

    int timeout = 10000;
    DatabaseReference rf;
    MFS100 mfs100 = null;
    Bitmap bitmap;
    private boolean isCaptureRunning = false;
    long[] a1, a2;
String user;

    public Check_Fingerprint() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Check_Fingerprint.
     */
    // TODO: Rename and change types and number of parameters
    public static Check_Fingerprint newInstance(String param1, String param2) {
        Check_Fingerprint fragment = new Check_Fingerprint();
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

            SharedPreferences sp = getActivity().getSharedPreferences("epic", Context.MODE_PRIVATE);
            user = sp.getString("epic", "");

            match = getActivity().findViewById(R.id.match);

            f1 = new Firebase_Fingerprrint();
            rf = FirebaseDatabase.getInstance().getReference("Fingerprints");
            a1 = new long[312];
            match.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int c = 0;

                    al = new ArrayList<>();

                    assert al != null;
                    rf.child(user).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            for (DataSnapshot dsp : snapshot.getChildren()) {
                                int i = 0;

                                al = (ArrayList<byte[]>) snapshot.getValue();

                                //  f1.gggggggggg=al;
                                //        HashMap<Integer,Object> h1= (HashMap<Integer, Object>) dsp.getValue();
                                // while(i<snapshot.getChildrenCount()) {
                                ///   a1[i] = (long) snapshot.getValue();
                                //break;
                                //}

                                // i++;
                                //    a1[i]= (int) h1.get(i);
                            }
                            Object[] o;
                            o = al.toArray();

                            byte[] yourBytes = new byte[o.length];
                            try {
                                for (int i = 0; i < o.length; i++) {
                                    int k = Integer.parseInt(o[i].toString());
                                    byte b = (byte) k;
                                    yourBytes[i] = b;

                                }
                            } catch (Exception d) {
                                Toast.makeText(getContext(), String.valueOf(d), Toast.LENGTH_SHORT).show();
                            }


                            byte[] b1 = new byte[o.length];
                            for (int i = 0; i < o.length; i++) {
                                if (yourBytes[i] <= 0) {
                                    b1[i] = (byte) (yourBytes[i] & 0xff);
                                } else
                                    b1[i] = (byte) (yourBytes[i]);
                            }
                            int ret = mfs100.MatchISO(yourBytes, caught);

                            if (ret < 0  ||  ret<100) {
                                Toast.makeText(getContext(),"Fingerprints do not match",Toast.LENGTH_SHORT).show();

                            } else if (ret > 100) {
                                Toast.makeText(getContext(),"Fingerprints matched",Toast.LENGTH_SHORT).show();
                            }

                            // backup(al);

                            //  for(int i=0;i<312;i++)
                            {
                                //   byte[] bytes = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(al.get(0)).array();

                                // for(Byte b:al.get(0).)
                                {

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    // int r=mfs100.MatchISO(caught,c1);
                    ///   Toast.makeText(getApplicationContext(), String.valueOf(r), Toast.LENGTH_SHORT).show();

                }
            });
            FindFormControls();
            try {
                this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }

            try {
                mfs100 = new MFS100(this);
                mfs100.SetApplicationContext(getContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check__fingerprint, container, false);
    }
    @Override
    public void onStart() {
        try {
            if (mfs100 == null) {
                mfs100 = new MFS100(this);
                mfs100.SetApplicationContext(getContext());
            } else {
                InitScanner();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStart();
    }

    public void onStop() {
        try {
            if (isCaptureRunning) {
                int ret = mfs100.StopAutoCapture();
            }
            Thread.sleep(500);
            //            UnInitScanner();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onStop();
    }


    @Override
    public void onDestroy() {
        try {
            if (mfs100 != null) {
                mfs100.Dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public void FindFormControls() {
        try {
            btnInit = (Button) getActivity().findViewById(R.id.btnInit);
            btnUninit = (Button) getActivity().findViewById(R.id.btnUninit);

            lblMessage = (TextView) getActivity().findViewById(R.id.lblMessage);
            txtEventLog = (EditText) getActivity().findViewById(R.id.txtEventLog);
            imgFinger = (ImageView) getActivity().findViewById(R.id.imgFinger);
            btnSyncCapture = (Button) getActivity().findViewById(R.id.btnSyncCapture);
            btnStopCapture = (Button) getActivity().findViewById(R.id.btnStopCapture);

            btnInit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InitScanner();
                    //onControlClicked(v);
                }
            });
            btnUninit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onControlClicked(v);
                }
            });
            btnSyncCapture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scannerAction = Check_Fingerprint.ScannerAction.Capture;
                    if (!isCaptureRunning) {
                        StartSyncCapture();
                    }
                }
            });
            btnStopCapture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onControlClicked(v);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onControlClicked(View v) {
        if (SystemClock.elapsedRealtime() - mLastClkTime < Threshold) {
            return;
        }
        mLastClkTime = SystemClock.elapsedRealtime();
        try {
            switch (v.getId()) {
                case R.id.btnInit:
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select"), 234);
                    InitScanner();
                    break;
                case R.id.btnUninit:
                    UnInitScanner();
                    break;
                case R.id.btnSyncCapture:
                    scannerAction = Check_Fingerprint.ScannerAction.Capture;
                    if (!isCaptureRunning) {
                        StartSyncCapture();
                    }
                    break;
                case R.id.btnStopCapture:
                    StopCapture();
                    break;


                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getActivity().setContentView(R.layout.activity_device);
        FindFormControls();
        try {
            if (mfs100 == null) {
                mfs100 = new MFS100(this);
                mfs100.SetApplicationContext(getContext());
            } /*else {
                InitScanner();
            }*/
            if (isCaptureRunning) {
                if (mfs100 != null) {
                    mfs100.StopAutoCapture();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitScanner() {
        try {
            int ret = mfs100.Init();
            if (ret != 0) {
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {

                SetTextOnUIThread("Init success");
                String info = "Serial: " + mfs100.GetDeviceInfo().SerialNo()
                        + " Make: " + mfs100.GetDeviceInfo().Make()
                        + " Model: " + mfs100.GetDeviceInfo().Model()
                        + "\nCertificate: " + mfs100.GetCertification();
                SetLogOnUIThread(info);
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), "Init failed, unhandled exception",
                    Toast.LENGTH_LONG).show();
            SetTextOnUIThread("Init failed, unhandled exception");
        }
    }

    private void StartSyncCapture() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SetTextOnUIThread("");
                isCaptureRunning = true;
                try {
                    FingerData fingerData = new FingerData();

                    int ret = mfs100.AutoCapture(fingerData, timeout, cbFastDetection.isChecked());
                    Log.e("StartSyncCapture.RET", "" + ret);
                    if (ret != 0) {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        lastCapFingerData = fingerData;

                        bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0,
                                fingerData.FingerImage().length);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                imgFinger.setImageBitmap(bitmap);

                            }
                        });
                        File save = new File(getActivity().getObbDir(), "bmp1" + ".bmp");
                        try {

                            FileOutputStream os = null;
                            os = new FileOutputStream(save);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                            os.flush();
                            os.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        Log.e("RawImage", Base64.encodeToString(fingerData.RawData(), Base64.DEFAULT));
//                        Log.e("FingerISOTemplate", Base64.encodeToString(fingerData.ISOTemplate(), Base64.DEFAULT));
                        SetTextOnUIThread("Capture Success");
                        String log = "\nQuality: " + fingerData.Quality()
                                + "\nNFIQ: " + fingerData.Nfiq()
                                + "\nWSQ Compress Ratio: "
                                + fingerData.WSQCompressRatio()
                                + "\nImage Dimensions (inch): "
                                + fingerData.InWidth() + "\" X "
                                + fingerData.InHeight() + "\""
                                + "\nImage Area (inch): " + fingerData.InArea()
                                + "\"" + "\nResolution (dpi/ppi): "
                                + fingerData.Resolution() + "\nGray Scale: "
                                + fingerData.GrayScale() + "\nBits Per Pixal: "
                                + fingerData.Bpp() + "\nWSQ Info: "
                                + fingerData.WSQInfo();
                        SetLogOnUIThread(log);
                        HashMap hashMap = new HashMap();
                        for (int i = 0; i < fingerData.ISOTemplate().length; i++) {
                            hashMap.put(String.valueOf(i), fingerData.ISOTemplate()[i]);
                        }
                        Firebase_Fingerprrint n1 = new Firebase_Fingerprrint();
                        //  n1.setF(hashMap);

                        Firebase_Constituencies g1 = new Firebase_Constituencies();
                        g1.setConstituency(",sjdfhl");

                        rf.child("jgkug").setValue(g1);


                        SetData2(fingerData);
                    }

                } catch (Exception ex) {
                    SetTextOnUIThread("Error");
                } finally {
                    isCaptureRunning = false;
                }
            }
        }).start();
    }

    private void StopCapture() {
        try {
            mfs100.StopAutoCapture();
        } catch (Exception e) {
            SetTextOnUIThread("Error");
        }
    }

    private void ExtractANSITemplate() {
        try {
            if (lastCapFingerData == null) {
                SetTextOnUIThread("Finger not capture");
                return;
            }
            byte[] tempData = new byte[2000]; // length 2000 is mandatory
            byte[] ansiTemplate;
            int dataLen = mfs100.ExtractANSITemplate(lastCapFingerData.RawData(), tempData);
            if (dataLen <= 0) {
                if (dataLen == 0) {
                    SetTextOnUIThread("Failed to extract ANSI Template");
                } else {
                    SetTextOnUIThread(mfs100.GetErrorMsg(dataLen));
                }
            } else {
                ansiTemplate = new byte[dataLen];
                System.arraycopy(tempData, 0, ansiTemplate, 0, dataLen);
                WriteFile("ANSITemplate.ansi", ansiTemplate);
                SetTextOnUIThread("Extract ANSI Template Success");
            }
        } catch (Exception e) {
            Log.e("Error", "Extract ANSI Template Error", e);
        }
    }

    private void ExtractISOImage() {
        try {
            if (lastCapFingerData == null) {
                SetTextOnUIThread("Finger not capture");
                return;
            }
            byte[] tempData = new byte[(mfs100.GetDeviceInfo().Width() * mfs100.GetDeviceInfo().Height()) + 1078];
            byte[] isoImage;

            // ISOType 1 == Regular ISO Image
            // 2 == WSQ Compression ISO Image
            int dataLen = mfs100.ExtractISOImage(lastCapFingerData.RawData(), tempData, 2);
            if (dataLen <= 0) {
                if (dataLen == 0) {
                    SetTextOnUIThread("Failed to extract ISO Image");
                } else {
                    SetTextOnUIThread(mfs100.GetErrorMsg(dataLen));
                }
            } else {
                isoImage = new byte[dataLen];
                System.arraycopy(tempData, 0, isoImage, 0, dataLen);
                WriteFile("ISOImage.iso", isoImage);
                SetTextOnUIThread("Extract ISO Image Success");
            }
        } catch (Exception e) {
            Log.e("Error", "Extract ISO Image Error", e);
        }
    }

    private void ExtractWSQImage() {
        try {
            if (lastCapFingerData == null) {
                SetTextOnUIThread("Finger not capture");
                return;
            }
            byte[] tempData = new byte[(mfs100.GetDeviceInfo().Width() * mfs100.GetDeviceInfo().Height()) + 1078];
            byte[] wsqImage;
            int dataLen = mfs100.ExtractWSQImage(lastCapFingerData.RawData(), tempData);
            if (dataLen <= 0) {
                if (dataLen == 0) {
                    SetTextOnUIThread("Failed to extract WSQ Image");
                } else {
                    SetTextOnUIThread(mfs100.GetErrorMsg(dataLen));
                }
            } else {
                wsqImage = new byte[dataLen];
                System.arraycopy(tempData, 0, wsqImage, 0, dataLen);
                WriteFile("WSQ.wsq", wsqImage);
                SetTextOnUIThread("Extract WSQ Image Success");
            }
        } catch (Exception e) {
            Log.e("Error", "Extract WSQ Image Error", e);
        }
    }

    private void UnInitScanner() {
        try {
            int ret = mfs100.UnInit();
            if (ret != 0) {
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                SetLogOnUIThread("Uninit Success");
                SetTextOnUIThread("Uninit Success");
                lastCapFingerData = null;
            }
        } catch (Exception e) {
            Log.e("UnInitScanner.EX", e.toString());
        }
    }

    private void WriteFile(String filename, byte[] bytes) {
        try {
            String path = Environment.getExternalStorageDirectory()
                    + "//FingerData";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            path = path + "//" + filename;
            file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(path);
            stream.write(bytes);
            stream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void WriteFileString(String filename, String data) {
        try {
            String path = Environment.getExternalStorageDirectory()
                    + "//FingerData";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            path = path + "//" + filename;
            file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(path);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(data);
            writer.flush();
            writer.close();
            stream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void ClearLog() {
        txtEventLog.post(new Runnable() {
            public void run() {
                try {
                    txtEventLog.setText("", TextView.BufferType.EDITABLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void SetTextOnUIThread(final String str) {
        lblMessage.post(new Runnable() {
            public void run() {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void SetLogOnUIThread(final String str) {

        txtEventLog.post(new Runnable() {
            public void run() {
                try {
                    txtEventLog.append("\n" + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void SetData2(FingerData fingerData) {
        try {
            if (scannerAction.equals(Check_Fingerprint.ScannerAction.Capture)) {
                Enroll_Template = new byte[fingerData.ISOTemplate().length];
                caught = new byte[fingerData.ISOTemplate().length];
                for (int i = 0; i < fingerData.ISOTemplate().length; i++) {
                    int k = Integer.parseInt(String.valueOf(fingerData.ISOTemplate()[i]));
                    byte b = (byte) k;
                    caught[i] = b;
                }
                System.arraycopy(fingerData.ISOTemplate(), 0, Enroll_Template, 0,
                        fingerData.ISOTemplate().length);

            } else if (scannerAction.equals(Check_Fingerprint.ScannerAction.Verify)) {
                if (Enroll_Template == null) {
                    return;
                }

                for (int i = 0; i < fingerData.ISOTemplate().length; i++) {
                    //    caught[i]=fingerData.ISOTemplate();
                }
                //  f2.val=caught;
                File f1 = new File(path.getPath());

                int size = (int) f1.length();
                byte[] bytes = new byte[size];

                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f1));
                DataInputStream dis = new DataInputStream(bis);
                dis.readFully(bytes);

                Verify_Template = new byte[fingerData.ISOTemplate().length];
                System.arraycopy(bytes, 0, Verify_Template, 0,
                        fingerData.ISOTemplate().length);

                int ret = mfs100.MatchISO(Enroll_Template, Verify_Template);
                if (ret < 0) {
                    SetTextOnUIThread("Error: " + ret + "(" + mfs100.GetErrorMsg(ret) + ")");
                } else {
                    if (ret >= 96) {
                        SetTextOnUIThread("Finger matched with score: " + ret);
                    } else {
                        SetTextOnUIThread("Finger not matched, score: " + ret);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            WriteFile("Raw.raw", fingerData.RawData());
            WriteFile("Bitmap.bmp", fingerData.FingerImage());
            WriteFile("ISOTemplate.iso", fingerData.ISOTemplate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long mLastAttTime = 0l;

    @Override
    public void OnDeviceAttached(int vid, int pid, boolean hasPermission) {

        if (SystemClock.elapsedRealtime() - mLastAttTime < Threshold) {
            return;
        }
        mLastAttTime = SystemClock.elapsedRealtime();
        int ret;
        if (!hasPermission) {
            SetTextOnUIThread("Permission denied");
            return;
        }
        try {
            if (vid == 1204 || vid == 11279) {
                if (pid == 34323) {
                    ret = mfs100.LoadFirmware();
                    if (ret != 0) {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        SetTextOnUIThread("Load firmware success");
                    }
                } else if (pid == 4101) {
                    String key = "Without Key";
                    ret = mfs100.Init();
                    if (ret == 0) {
                        showSuccessLog(key);
                    } else {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSuccessLog(String key) {
        try {
            SetTextOnUIThread("Init success");
            String info = "\nKey: " + key + "\nSerial: "
                    + mfs100.GetDeviceInfo().SerialNo() + " Make: "
                    + mfs100.GetDeviceInfo().Make() + " Model: "
                    + mfs100.GetDeviceInfo().Model()
                    + "\nCertificate: " + mfs100.GetCertification();
            SetLogOnUIThread(info);
        } catch (Exception e) {
        }
    }

    long mLastDttTime = 0l;

    @Override
    public void OnDeviceDetached() {
        try {

            if (SystemClock.elapsedRealtime() - mLastDttTime < Threshold) {
                return;
            }
            mLastDttTime = SystemClock.elapsedRealtime();
            UnInitScanner();

            SetTextOnUIThread("Device removed");
        } catch (Exception e) {
        }
    }

    @Override
    public void OnHostCheckFailed(String err) {
        try {
            SetLogOnUIThread(err);
            Toast.makeText(getContext(), err, Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 234 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            path = data.getData();
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                //iv.setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}