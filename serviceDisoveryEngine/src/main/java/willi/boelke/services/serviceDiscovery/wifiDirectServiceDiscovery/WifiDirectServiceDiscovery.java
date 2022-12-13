package willi.boelke.services.serviceDiscovery.wifiDirectServiceDiscovery;

import android.content.Context;

import willi.boelke.services.serviceDiscovery.IServiceDiscoveryEngine;
import willi.boelke.services.serviceDiscovery.ServiceDescription;

public interface WifiDirectServiceDiscovery extends IServiceDiscoveryEngine
{

    @Override
    boolean start(Context context);

    @Override
    void stop();

    void startDiscovery();

    void stopDiscovery();

    @Override
    void startDiscoveryForService(ServiceDescription description);

    @Override
    void stopDiscoveryForService(ServiceDescription description);

    void startService(ServiceDescription description);

    void stopService(ServiceDescription description);

    void registerDiscoverListener(WifiServiceDiscoveryListener listener);

    void unregisterDiscoveryListener(WifiServiceDiscoveryListener listener);

    @Override
    void notifyAboutAllServices(boolean all);
}
