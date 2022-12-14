package willi.boelke.service_discovery_demo.view.listAdapters;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import willi.boelke.service_discovery_demo.R;


public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice>
{

    private final LayoutInflater mLayoutInflater;
    private final ArrayList<BluetoothDevice> mDevices;
    private final int mViewResourceId;

    public DeviceListAdapter(Context context, int tvResourceId, ArrayList<BluetoothDevice> devices)
    {
        super(context, tvResourceId, devices);
        this.mDevices = devices;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = tvResourceId;
    }

    @SuppressLint("MissingPermission")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = mLayoutInflater.inflate(mViewResourceId, null);

        BluetoothDevice device = mDevices.get(position);

        //Setup the name TextView
        TextView name = convertView.findViewById(R.id.service_tv);
        name.setText(device.getName());

        TextView address = convertView.findViewById(R.id.host_tv);
        address.setText(device.getAddress());

        TextView bond_state = convertView.findViewById(R.id.bonded_state);
        String bondState;
        switch (device.getBondState())
        {
            case BluetoothDevice.BOND_BONDED:
                bondState = "Bonded";
                break;
            case BluetoothDevice.BOND_NONE:
                bondState = "-";
                break;
            case BluetoothDevice.BOND_BONDING:
                bondState = "Bonding";
                break;
            default:
                bondState = "?";
        }
        bond_state.setText(bondState);
        return convertView;
    }
}