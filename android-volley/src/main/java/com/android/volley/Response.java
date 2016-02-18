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

package com.android.volley;

/**
 * Encapsulates a parsed response for delivery.
 * 分发响应解析响应的数据
 *
 * @param <T> Parsed type of this response
 */
public class Response<T> {

    /**
     * Callback interface for delivering parsed responses.
     * 当一个请求——相应成功后的监听
     */
    public interface Listener<T> {
        /**
         * Called when a response is received.
         */
        public void onResponse(T response);
    }

    /**
     * Callback interface for delivering error responses.
     * 当请求出现了错误时,需要监听错误
     */
    public interface ErrorListener {
        /**
         * Callback method that an error has been occurred with the
         * provided error code and optional user-readable message.
         */
        public void onErrorResponse(VolleyError error);
    }

    /**
     * Returns a successful response containing the parsed result.
     * 当请求被解析成功时调用的方法
     */
    public static <T> Response<T> success(T result, Cache.Entry cacheEntry) {
        return new Response<T>(result, cacheEntry);
    }

    /**
     * Returns a failed response containing the given error code and an optional
     * localized message displayed to the user.
     * 如果解析请求时失败需要封装错误
     */
    public static <T> Response<T> error(VolleyError error) {
        return new Response<T>(error);
    }

    /**
     * Parsed response, or null in the case of error.
     * 响应返回的结果
     */
    public final T result;

    /**
     * Cache metadata for this response, or null in the case of error.
     * 缓存
     */
    public final Cache.Entry cacheEntry;

    /**
     * Detailed error information if <code>errorCode != OK</code>.
     * 记录错误的生成
     */
    public final VolleyError error;

    /**
     * True if this response was a soft-expired one and a second one MAY be coming.
     * 判断一个请求是否失效
     */
    public boolean intermediate = false;

    /**
     * Returns whether this response is considered successful.
     */
    public boolean isSuccess() {
        //如果成功就没有错误传递
        return error == null;
    }


    private Response(T result, Cache.Entry cacheEntry) {
        this.result = result;
        this.cacheEntry = cacheEntry;
        this.error = null;
    }

    private Response(VolleyError error) {
        this.result = null;
        this.cacheEntry = null;
        this.error = error;
    }
}
