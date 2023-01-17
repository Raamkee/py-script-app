package com.example.py_script_app;


import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;

import androidx.annotation.NonNull;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;


public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "samples.flutter.dev/battery";

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler((call, result) -> {
            if (call.method.equals("getBatteryLevel")) {
                int batteryLevel = getBatteryLevel(call.arguments.toString());
                if (batteryLevel != -1) {
                    result.success(batteryLevel);
                } else {
                    result.error("UNAVAILABLE", "Battery level not available.", null);
                }
            } else {
                result.notImplemented();
            }
        });
    }

    private int getBatteryLevel(String data) {
        Log.i("getBatteryLevel", "getBatteryLevel: | " + data);

        processScript(data);
        int batteryLevel = -1;
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } else {
            Intent intent = new ContextWrapper(getApplicationContext()).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }
        return batteryLevel;
    }


    public void processScript(String code) {
        Log.i("processScript", "starting...");
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("myscript");
        PyObject obj = pyobj.callAttr("main");
        Log.i("processScript", obj.toString());
    }

    public void processScriptFromString(String code) {
        Log.i("processScript", "starting...");
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("script_string");
        PyObject obj = pyobj.callAttr("mainTextCode", code);
        Log.i("processScript", obj.toString());
    }

    public void processScriptForConsoleOutput(String code) {
        Log.i("processScript", "starting...");
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("script_string");
        PyObject obj = null;
        PyObject _sys = py.getModule("sys");
        PyObject _io = py.getModule("io");
        try {
            PyObject _textOutputStream = _io.callAttr("StringIO");
//                    _sys["stdout"]=_textOutputStream;
            obj = pyobj.callAttrThrows("mainTextCode", code);
            String _returnOutput = _textOutputStream.callAttr("getvalue").toString();
            Log.i("processScript", _returnOutput);
        } catch (Throwable e) {
            Log.i("processScript", "catch | error" + e);
            e.printStackTrace();
        }
    }

}