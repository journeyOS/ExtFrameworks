/*
 * Copyright (c) 2022 anqi.huang@outlook.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.journeyOS.server.vrr;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Singleton;

import system.ext.utils.JosLog;

public class VrrSurfaceFlinger {
    private static final String TAG = VrrSurfaceFlinger.class.getSimpleName();
    private static final int TRANSACT_CODE_SET_REFRESH_RATE = 1035;

    private static final Singleton<VrrSurfaceFlinger> gDefault = new Singleton<VrrSurfaceFlinger>() {
        @Override
        protected VrrSurfaceFlinger create() {
            return new VrrSurfaceFlinger();
        }
    };

    public VrrSurfaceFlinger() {
    }

    public static VrrSurfaceFlinger getDefault() {
        return gDefault.get();
    }

    public void setRefreshRate(float refreshRate) {
        int modeId = VrrSurfaceControlProxy.getDefault().findDefaultModeIdByRefreshRate(refreshRate);
        JosLog.i(VRRManager.VRR_TAG, TAG, "set refresh rate, mode id = [" + modeId + "]");
        IBinder mSurfaceFlinger = ServiceManager.getService("SurfaceFlinger");
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken("android.ui.ISurfaceComposer");
        data.writeInt(modeId);
        try {
            try {
                mSurfaceFlinger.transact(TRANSACT_CODE_SET_REFRESH_RATE, data, null, 0);
//                mSurfaceFlinger.transact(TRANSACT_CODE_SET_REFRESH_RATE, data, reply, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } finally {
            data.recycle();
            reply.readException();
            reply.recycle();
        }
    }

}
