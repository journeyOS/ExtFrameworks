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

package com.android.server.wm;

import com.journeyOS.server.vrr.VRRManager;

import system.ext.utils.JosLog;

public class HookWindowStateImpl implements HookWindowState {
    private static final String TAG = HookWindowStateImpl.class.getSimpleName();
    private static final boolean DEBUG = false;
    private static HookWindowStateImpl sInstance;
    /**
     * This is the frame rate which is passed to SurfaceFlinger if the window set a
     * preferredDisplayModeId or is part of the high refresh rate deny list.
     * The variable is cached, so we do not send too many updates to SF.
     */
    private float mAppPreferredFrameRate = 0f;

    private int mPid;

    public HookWindowStateImpl() {
    }

    public static HookWindowStateImpl getInstance() {
        synchronized (HookWindowStateImpl.class) {
            if (sInstance == null) {
                sInstance = new HookWindowStateImpl();
            }
            return sInstance;
        }
    }

    @Override
    public int getPid() {
        if (DEBUG) {
            JosLog.d(VRRManager.VRR_TAG, TAG, "get pid = [" + mPid + "]");
        }
        return mPid;
    }

    @Override
    public float getPreferredRefreshRate() {
        if (DEBUG) {
            JosLog.d(VRRManager.VRR_TAG, TAG, "get preferred refresh rate = [" + mAppPreferredFrameRate + "]");
        }
        return mAppPreferredFrameRate;
    }

    @Override
    public void setPreferredRefreshRate(int pid, float refreshRate) {
        mAppPreferredFrameRate = refreshRate;
        if (DEBUG) {
            JosLog.d(VRRManager.VRR_TAG, TAG, "set preferred refresh rate = [" + mAppPreferredFrameRate + "]");
        }
    }
}
