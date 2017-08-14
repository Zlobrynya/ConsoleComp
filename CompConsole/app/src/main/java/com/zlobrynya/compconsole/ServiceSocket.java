package com.zlobrynya.compconsole;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ServiceSocket extends Service {

    public ServiceSocket() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
