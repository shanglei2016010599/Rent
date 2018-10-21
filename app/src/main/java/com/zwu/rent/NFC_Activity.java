package com.zwu.rent;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class NFC_Activity extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    EditText NoteRead;
    PendingIntent NfcPendingIntent;
    IntentFilter[] NdefExchangeFilters;
    private String[][] techListsArray;// ++
    String ex_id = "", types = "";

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, NfcPendingIntent, NdefExchangeFilters,
                techListsArray);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
        if (nfcAdapter != null ){
            nfcAdapter.disableForegroundNdefPush(this);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        NoteRead = findViewById(R.id.noteRead);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "设备不支持NFC！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "请在系统设置中先启用NFC功能！", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            finish();
            return;
        }

        NfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this,
                        getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("*/*");    //text/plain
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        IntentFilter td = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ttech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        NdefExchangeFilters = new IntentFilter[]{ndefDetected, ttech, td};

        techListsArray = new String[][]{
                new String[]{NfcF.class.getName()},
                new String[]{NfcA.class.getName()},
                new String[]{NfcB.class.getName()},
                new String[]{NfcV.class.getName()},
                new String[]{Ndef.class.getName()},
                new String[]{NdefFormatable.class.getName()},
                new String[]{IsoDep.class.getName()},
                new String[]{MifareClassic.class.getName()},
                new String[]{MifareUltralight.class.getName()}
        };
    }

    private NfcB nfcbTag;

    void getresult(Tag tag) {
        ArrayList<String> list = new ArrayList<String>();
        types = "";
        for (String string : tag.getTechList()) {
            list.add(string);
            types += string.substring(string.lastIndexOf(".") + 1, string.length()) + ",";
        }
        types = types.substring(0, types.length() - 1);
        if (list.contains("android.nfc.tech.MifareUltralight")) {
//            String str = readTagUltralight(tag);
//            setNoteBody(str);
//        } else if (list.contains("android.nfc.tech.MifareClassic")) {
//            String str = readTagClassic(tag);
//            setNoteBody(str);
//        } else if (list.contains("android.nfc.tech.IsoDep")) {
//            try {
//                //Get an instance of the type A card from this TAG
//	               /*IsoDep isodep = IsoDep.get(tag);
//	               isodep.connect();
//	               if (isodep.isConnected()) {
//	               //select the card manager applet
//	            	  byte[] cmd = { (byte) 0x00, // CLA Class
//                (byte) 0xB4, // INS Instruction
//                (byte) 0x04, // P1 Parameter 1
//                (byte) 0x00, // P2 Parameter 2
//                (byte) 0x00, // Le
//                };
//	            	byte[] command = BuildSelectApdu(SAMPLE_LOYALTY_CARD_AID);
//	               byte[] balanceRsp = isodep.transceive(command);
//	               isodep.close(); */
//                IsoDep isodep = IsoDep.get(tagFromIntent);
//                isodep.connect();
//                //select the card manager applet
//                byte[] mf = {(byte) '1', (byte) 'P',
//                        (byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S', (byte) 'Y',
//                        (byte) 'S', (byte) '.', (byte) 'D', (byte) 'D', (byte) 'F',
//                        (byte) '0', (byte) '1',};
//                String result = "";
//                byte[] mfRsp = isodep.transceive(getSelectCommand(mf));
//                Log.d(TAG, "mfRsp:" + HexToString(mfRsp));
//                //select Main Application
//                byte[] wht = {(byte) 0x41, (byte) 0x50,//此处以武汉通为例，其它的卡片参考对应的命令，网上可以查到
//                        (byte) 0x31, (byte) 0x2E, (byte) 0x57, (byte) 0x48, (byte) 0x43,
//                        (byte) 0x54, (byte) 0x43,};
//                byte[] sztRsp = isodep.transceive(getSelectCommand(wht));
//
//                byte[] balance = {(byte) 0x80, (byte) 0x5C, 0x00, 0x02, 0x04};
//                byte[] balanceRsp = isodep.transceive(balance);
//                Log.d(TAG, "balanceRsp:" + HexToString(balanceRsp));
//                if (balanceRsp != null && balanceRsp.length > 4) {
//                    int cash = byteToInt(balanceRsp, 4);
//                    float ba = cash / 100.0f;
//                    result += "  余额：" + String.valueOf(ba);
//
//                }
//                setNoteBody(result);
//                isodep.close();
//
//
//            } catch (Exception e) {
//                Log.e(TAG, "ERROR:" + e.getMessage());
//            }
        } else if (list.contains("android.nfc.tech.NfcB")) {
            nfcbTag = NfcB.get(tag);
            try {
                nfcbTag.connect();
                if (nfcbTag.isConnected()) {
                    System.out.println("已连接");
                    Toast.makeText(NFC_Activity.this, "身份证已连接",
                            Toast.LENGTH_SHORT).show();
//                        new CommandAsyncTask().execute();

                }
                // nfcbTag.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//        } else if (list.contains("android.nfc.tech.NfcA")) {
//            toast("NfcA");
//            NfcA nfca = NfcA.get(tagFromIntent);
//            try {
//                nfca.connect();
//                if (nfca.isConnected()) {//NTAG216的芯片
//                    byte[] SELECT = {
//                            (byte) 0x30,
//                            (byte) 5 & 0x0ff,//0x05
//                    };
//                    byte[] response = nfca.transceive(SELECT);
//                    nfca.close();
//                    if (response != null) {
//                        setNoteBody(new String(response, Charset.forName("utf-8")));
//                    }
//                }
//            } catch (Exception e) {
//            }
//        } else if (list.contains("android.nfc.tech.NfcF")) {//此处代码没有标签测试
//            NfcF nfc = NfcF.get(tag);
//            try {
//                nfc.connect();
//                byte[] felicaIDm = new byte[]{0};
//                byte[] req = readWithoutEncryption(felicaIDm, 10);
//                byte[] res = nfc.transceive(req);
//                nfc.close();
//                setNoteBody(ByteArrayToHexString(res));
//            } catch (Exception e) {
//                Log.e(TAG, e.getMessage(), e);
//            }
//        } else if (list.contains("android.nfc.tech.NfcV")) {//完成
//            NfcV tech = NfcV.get(tag);
//            if (tech != null) {
//                try {
//                    tech.connect();
//                    if (tech.isConnected()) {
//                        byte[] tagUid = tag.getId();  // store tag UID for use in addressed commands
//
//                        int blockAddress = 0;
//                        int blocknum = 4;
//                        byte[] cmd = new byte[]{
//                                (byte) 0x22,  // FLAGS
//                                (byte) 0x23,  // 20-READ_SINGLE_BLOCK,23-所有块
//                                0, 0, 0, 0, 0, 0, 0, 0,
//                                (byte) (blockAddress & 0x0ff), (byte) (blocknum - 1 & 0x0ff)
//                        };
//                        System.arraycopy(tagUid, 0, cmd, 2, tagUid.length);  // paste tag UID into command
//
//                        byte[] response = tech.transceive(cmd);
//                        tech.close();
//                        if (response != null) {
//                            setNoteBody(new String(response, Charset.forName("utf-8")));
//                        }
//                    }
//                } catch (IOException e) {
//
//                }
//            }
//        } else if (list.contains("android.nfc.tech.Ndef")) {
//            NdefMessage[] messages = getNdefMessages(getIntent());
//            byte[] payload = messages[0].getRecords()[0].getPayload();
//            setNoteBody(new String(payload));
//        } else if (list.contains("android.nfc.tech.NdefFormatable")) {
//            NdefMessage[] messages = getNdefMessages(getIntent());
//            byte[] payload = messages[0].getRecords()[0].getPayload();
//            setNoteBody(new String(payload));
        }
    }
}
