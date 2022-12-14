package willi.boelke.service_discovery_demo.view.listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import willi.boelke.service_discovery_demo.R;
import willi.boelke.services.serviceConnection.wifiDirectServiceConnection.WifiConnection;


public class WifiConnectionListAdapter extends ArrayAdapter<WifiConnection>
{

    private final LayoutInflater mLayoutInflater;
    private final ArrayList<WifiConnection> connections;
    private final int mViewResourceId;

    public WifiConnectionListAdapter(Context context, int tvResourceId, ArrayList<WifiConnection> devices)
    {
        super(context, tvResourceId, devices);
        this.connections = devices;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = tvResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = mLayoutInflater.inflate(mViewResourceId, null);

        WifiConnection connection = connections.get(position);

        //Setup the name TextView
        TextView name = convertView.findViewById(R.id.service_tv);
        TextView serviceName = convertView.findViewById(R.id.service_name_tv);
        TextView description = convertView.findViewById(R.id.description_tv);
        TextView peerAddress = convertView.findViewById(R.id.address_tv);
        TextView uuid = convertView.findViewById(R.id.uuid_tv);

        String srvInstance = connection.getServiceDescription().getInstanceName() + "." + connection.getServiceDescription().getServiceType();
        name.setText(srvInstance);
        description.setText(connection.getServiceDescription().getTxtRecord().get("info"));
        serviceName.setText(connection.getServiceDescription().getTxtRecord().get("name"));
        uuid.setText(connection.getServiceDescription().getServiceUuid().toString());
        peerAddress.setText(connection.getRemoteDeviceAddress());


        return convertView;
    }
}