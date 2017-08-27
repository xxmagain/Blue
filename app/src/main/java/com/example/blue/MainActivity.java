package com.example.blue;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private MyAdapter mleAdapter=null;
    private BluetoothAdapter mAdapter;
    private BroadcastReceiver bluetoothReceiver = null;

    @Override
    public void onCreate(Bundle savedInstanaceState) {
        super.onCreate(savedInstanaceState);
        setContentView(R.layout.activity_main);
        Button open = (Button) findViewById(R.id.open);
        Button search = (Button) findViewById(R.id.search);
        ListView lv = (ListView)findViewById(R.id.list);
        mleAdapter = new MyAdapter(this);
        lv.setAdapter(mleAdapter);


        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "你的手机不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mAdapter = bluetoothManager.getAdapter();
        if (mAdapter == null) {
            Toast.makeText(this, "你的手机不支持蓝牙", Toast.LENGTH_SHORT).show();
        }
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(intent);
            }
        });
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        bluetoothReceiver = new BluetoothReceiver();
        registerReceiver(bluetoothReceiver, intentFilter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.startDiscovery();
            }
        });

    }

    private class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.d("设备：", device.getName() + ";" + device.getAddress());
            mleAdapter.addDevice(device);
            mleAdapter.notifyDataSetChanged();


        }
    }

}