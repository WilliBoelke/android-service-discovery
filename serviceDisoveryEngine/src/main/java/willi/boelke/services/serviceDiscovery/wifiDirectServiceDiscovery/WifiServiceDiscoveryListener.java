package willi.boelke.services.serviceDiscovery.wifiDirectServiceDiscovery;

import android.net.wifi.p2p.WifiP2pDevice;

import willi.boelke.services.serviceDiscovery.ServiceDescription;
import willi.boelke.services.serviceDiscovery.bluetoothServiceDiscovery.BluetoothServiceDiscoveryVOne;

/**
 * Listener interface to get notified when
 * the {@link WifiDirectServiceDiscoveryEngine} found a
 * new device or service on a device.
 *
 * @author WilliBolke
 */
public interface WifiServiceDiscoveryListener
{
    /**
     * Called whenever a service was discovered which has been looked for
     * through {@link WifiDirectServiceDiscoveryEngine#startDiscoveryForService(ServiceDescription)}
     * <p>
     * Provides the remote host device and the service description to identify the service
     *
     * @param host
     *         The BluetoothDevice hosting / advertising the service
     * @param description
     *         The service description as provided in {@link BluetoothServiceDiscoveryVOne#startDiscoveryForService(ServiceDescription)}
     *         which was resolved through matching UUIDs
     */
    void onServiceDiscovered(WifiP2pDevice host, ServiceDescription description);
}
