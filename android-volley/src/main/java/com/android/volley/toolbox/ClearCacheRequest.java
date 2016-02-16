/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.volley.toolbox;

import android.os.Handler;
import android.os.Looper;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * A synthetic request used for clearing the cache.
 * 一个模拟的用来清理缓存的请求
 */
public class ClearCacheRequest extends Request<Object> {
    /**
     * 需要清理的缓存
     */
    private final Cache mCache;
    /**
     * 缓存清理完后在主线程中被调用的回调接口。
     */
    private final Runnable mCallback;

    /**
     * Creates a synthetic request for clearing the cache.
     *
     * @param cache    Cache to clear
     * @param callback Callback to make on the main thread once the cache is clear,
     *                 or null for none
     */
    public ClearCacheRequest(Cache cache, Runnable callback) {
        super(Method.GET, null, null);
        mCache = cache;
        mCallback = callback;
    }

    @Override
    public boolean isCanceled() {
        // This is a little bit of a hack, but hey, why not.
        mCache.clear();
        if (mCallback != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postAtFrontOfQueue(mCallback);
        }
        return true;
    }

    @Override
    public Priority getPriority() {
        //优先级是最高的
        return Priority.IMMEDIATE;
    }

    @Override
    protected Response<Object> parseNetworkResponse(NetworkResponse response) {
        return null;
    }

    @Override
    protected void deliverResponse(Object response) {
    }
}
