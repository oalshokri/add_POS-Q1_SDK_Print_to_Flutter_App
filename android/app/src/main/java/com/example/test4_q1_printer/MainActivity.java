package com.example.test4_q1_printer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import io.flutter.embedding.android.FlutterActivity;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

import com.iposprinter.iposprinterservice.IPosPrinterCallback;
import com.iposprinter.iposprinterservice.IPosPrinterService;


public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "samples.flutter.dev/q1Printer";

    private IPosPrinterService mIPosPrinterService;
    private IPosPrinterCallback callback = null;

    private int printerStatus = 0;
    private final int PRINTER_NORMAL = 0;


    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            // Note: this method is invoked on the main thread.
                            // Note: this method is invoked on the main thread.
                            if (call.method.equals("goPrint")) {
                                int resultValue = goPrint();

                                if (resultValue != -1) {
                                    result.success(resultValue);
                                } else {
                                    result.error("UNAVAILABLE", "printer not available.", null);
                                }
                            } else {
                                result.notImplemented();
                            }
                        }
                );
    }

    private int goPrint() {
        int resultValue = -1;

        if (getPrinterStatus() == PRINTER_NORMAL){
            printText();
            resultValue = 1;
        }


        return resultValue;
    }

    private ServiceConnection connectService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIPosPrinterService = IPosPrinterService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIPosPrinterService = null;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.setPackage("com.iposprinter.iposprinterservice");
        intent.setAction("com.iposprinter.iposprinterservice.IPosPrintService");
        //startService(intent);
        bindService(intent, connectService, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connectService);
    }

    public int getPrinterStatus() {

        try {
            printerStatus = mIPosPrinterService.getPrinterStatus();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return printerStatus;
    }

    public void printText() {
        try{
            mIPosPrinterService.printText("test printing",callback);
        }catch (RemoteException e) {
            e.printStackTrace();
        }


//        ThreadPoolManager.getInstance().executeTask(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    mIPosPrinterService.printSpecifiedTypeText("    智能POS机\n", "ST", 48, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("    智能POS机数据终端\n", "ST", 32, callback);
//                    mIPosPrinterService.printBlankLines(1, 8, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("      欢迎使智能POS机数据终端\n", "ST", 24, callback);
//                    mIPosPrinterService.printBlankLines(1, 8, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("智能POS 数据终端 智能POS\n", "ST", 32, callback);
//                    mIPosPrinterService.printBlankLines(1, 8, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("#POS POS ipos POS POS POS POS ipos POS POS ipos#\n", "ST", 16, callback);
//                    mIPosPrinterService.printBlankLines(1, 16, callback);
//                    mIPosPrinterService.printBlankLines(1, 16, callback);
//                    mIPosPrinterService.PrintSpecFormatText("开启打印测试\n", "ST", 32, 1, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("********************************", "ST", 24, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("这是一行16号字体\n", "ST", 16, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("这是一行24号字体\n", "ST", 24, callback);
//                    mIPosPrinterService.PrintSpecFormatText("这是一行24号字体\n", "ST", 24, 2, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("这是一行32号字体\n", "ST", 32, callback);
//                    mIPosPrinterService.PrintSpecFormatText("这是一行32号字体\n", "ST", 32, 2, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("这是一行48号字体\n", "ST", 48, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("ABCDEFGHIJKLMNOPQRSTUVWXYZ\nabcdefghijklmnopqrstuvwxyz\n0123456789\n", "ST", 16, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("ABCDEFGHIJKLMNOPQRSTUVWXYZ\nabcdefghijklmnopqrstuvwxyz\n0123456789\n", "ST", 24, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("ABCDEFGHIJKLMNOPQRSTUVWXYZ\nabcdefghijklmnopqrstuvwxyz\n0123456789\n", "ST", 32, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("ABCDEFGHIJKLMNOPQRSTUVWXYZ\nabcdefghijklmnopqrstuvwxyz\n0123456789\n", "ST", 48, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("κρχκμνκλρκνκνμρτυφ\n", "ST", 24, callback);
//                    mIPosPrinterService.setPrinterPrintAlignment(0, callback);
//                    mIPosPrinterService.printQRCode("http://www.baidu.com\n", 10, 1, callback);
//                    mIPosPrinterService.printBlankLines(1, 16, callback);
//                    mIPosPrinterService.printBlankLines(1, 16, callback);
//
//                    mIPosPrinterService.PrintSpecFormatText("打印测试完成\n", "ST", 32, 1, callback);
//                    mIPosPrinterService.printSpecifiedTypeText("**********END***********\n\n", "ST", 32, callback);
//
//                    mIPosPrinterService.printerPerformPrint(160, callback);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
