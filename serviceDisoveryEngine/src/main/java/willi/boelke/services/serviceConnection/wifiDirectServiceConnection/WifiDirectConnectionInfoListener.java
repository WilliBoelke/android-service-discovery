package willi.boelke.services.serviceConnection.wifiDirectServiceConnection;

import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import willi.boelke.services.serviceConnection.wifiDirectServiceConnection.tcp.TCPChannelMaker;

/**
 * This is an implementation of the
 * WifiP2pManager.ConnectionInfoListener which will be given as a
 * callback when requesting the connection info from the
 * WiFiP2pManager.
 *
 * If the connection state changed this will:
 *
 * 1. determine whether we became the group owner or not
 * 2. Notify the engine accordingly
 * 3. Depending on which roll we got start a TcpChannelMaker
 *    either as a server or a client.
 * 4. Give the TCPChannelMaker to the engine, for it to wait to finish and get the
 *    connected Socket.
 *
 * Though {@link #establishConnection} or {@link #establishConnections(boolean)}
 * respectively this can be disabled by the engine.
 * This then wont start any new TCPChannelMaker or try to establish connections.
 * This is used for the clients who, when a GO connection is established, shouldn't
 * establish other connections anymore.
 *
 * @author WilliBoelk
 *
 */
class WifiDirectConnectionInfoListener implements WifiP2pManager.ConnectionInfoListener
{
    //
    //  ----------  instance variables ----------
    //

    /**
     * Classname for logging
     */
    private final String TAG = this.getClass().getSimpleName();

    /**
     * The WifiDirectServiceConnectionEngine to notify about
     * assigned roles (GO or Client) and about started
     * TCPChannelMaker
     */
    private final WifiDirectServiceConnectionEngine wifiDirectServiceConnectionEngine;

    /**
     * The Server ChannelMaker which will
     * accept TCp conventions an a TCP server socket.
     */
    private TCPChannelMaker serverChannelCreator = null;

    /**
     * Determine whether or not new connections should be attempted
     * can be set through {@link #establishConnections(boolean)}
     */
    private boolean establishConnection = true;

    //
    //  ----------  constructor and initialisation ----------
    //

    public WifiDirectConnectionInfoListener(WifiDirectServiceConnectionEngine wifiDirectServiceConnectionEngine)
    {
        this.wifiDirectServiceConnectionEngine = wifiDirectServiceConnectionEngine;
    }

    //
    //  ----------  connection info listener ----------
    //

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info)
    {
        //----------------------------------
        // NOTE : apparently this method will also be called
        // for the GO, when a client leaves the group and other
        // clients are connected. That's because in the  WifiDirectStateChangeReceiver,
        // the condition `isConnected()` is still true (second device is still connected).
        //
        // The GO will try to establish a connection to the given device,
        // and this ended in a infinite loop in the TCPServer.
        // To prevent this there is now a maximum of retires in the TCP
        // server.
        //
        // Though there should be a way to distinguish the two cases prior..
        // i could not ind anything so far.
        //----------------------------------

        Log.d(TAG, "onConnectionInfoAvailable: received connection info");
        Log.d(TAG, "onConnectionInfoAvailable: " + info);

        if (!establishConnection)
        {
            Log.e(TAG, "onConnectionInfoAvailable: should not establish connections");
            return;
        }
        TCPChannelMaker.max_connection_loops = 10;
        TCPChannelMaker channelCreator = null;
        if (info.isGroupOwner)
        {
            //--- creating server channel - just once as group owner ---//

            if (this.serverChannelCreator == null)
            {
                Log.d(TAG, "onConnectionInfoAvailable: start server channel");
                this.serverChannelCreator = TCPChannelMaker.getTCPServerCreator(wifiDirectServiceConnectionEngine.getPort(), true);
            }
            else
            {
                Log.d(TAG, "onConnectionInfoAvailable: Server channel already exists");
            }
            channelCreator = this.serverChannelCreator;
            wifiDirectServiceConnectionEngine.onBecameGroupOwner();
        }
        else
        {
            //----------------------------------
            // NOTE : Okay so once one one device
            // i received a null pointer exception
            // here - from the info or info.groupOwner
            // Address being null i wont change
            // anything here just now, but lets
            // see if that happens again and under
            // which conditions I will log here:
            //----------------------------------
            Log.d(TAG, "onConnectionInfoAvailable: " + info);
            // okay- it happened again and again only on that one device.
            // apparently it happens as GO and when other devices disconnect
            // i will but in a break here to hot-fix the issue for the time being
            if(info.groupOwnerAddress == null){
                Log.e(TAG, "onConnectionInfoAvailable: the group owner address was null - stop");
                return;
            }

            String hostAddress = info.groupOwnerAddress.getHostAddress();


            Log.d(TAG, "onConnectionInfoAvailable: local peer client, group owner = " + hostAddress);
            channelCreator = TCPChannelMaker.getTCPClientCreator(hostAddress, wifiDirectServiceConnectionEngine.getPort());
            wifiDirectServiceConnectionEngine.onBecameClient();
        }
        Log.e(TAG, "onConnectionInfoAvailable: channel creator is null = " + (channelCreator == null));
        this.wifiDirectServiceConnectionEngine.onSocketConnectionStarted(channelCreator);
    }

    /**
     * This can be used to prevent the listener from establish connections as soon as
     * the are received.
     * <p>
     * In many cases this behavior isn't (for example when connections are already established)
     *
     * @param shouldEstablish
     *         boolean determining whether a connection should be attempted
     */
    protected void establishConnections(boolean shouldEstablish)
    {
        Log.e(TAG, "establishConnections: SHOULD CONNECT = " + shouldEstablish);
        this.establishConnection = shouldEstablish;
    }
}
