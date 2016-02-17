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

import java.util.Collections;
import java.util.Map;

/**
 * An interface for a cache keyed by a String with a byte array as data.
 * 保存缓存数据
 */
public interface Cache {
    /**
     * Retrieves an entry from the cache.
     *
     * @param key Cache key
     * @return An {@link Entry} or null in the event of a cache miss
     */
    public Entry get(String key);

    /**
     * Adds or replaces an entry to the cache.
     *
     * @param key   Cache key
     * @param entry Data to store and metadata for cache coherency, TTL, etc.
     */
    public void put(String key, Entry entry);

    /**
     * Performs any potentially long-running actions needed to initialize the cache;
     * will be called from a worker thread.
     * 缓存的初始化
     */
    public void initialize();

    /**
     * Invalidates an entry in the cache.
     * 使Entry对象在缓存中无效
     *
     * @param key        Cache key
     * @param fullExpire True to fully expire the entry, false to soft expire
     */
    public void invalidate(String key, boolean fullExpire);

    /**
     * Removes an entry from the cache.
     *
     * @param key Cache key
     */
    public void remove(String key);

    /**
     * Empties the cache.
     */
    public void clear();

    /**
     * Data and metadata for an entry returned by the cache.
     * 用于保存缓存的一些属性，比如说过期时间，失效时间...何时需要刷新等等...以及缓存数据...
     */
    public static class Entry {
        /**
         * The data returned from cache.
         * 保存Body实体中的数据
         */
        public byte[] data;

        /**
         * ETag for cache coherency.
         * 用于缓存的新鲜度验证
         */
        public String etag;

        /**
         * Date of this response as reported by the server.
         * 整个请求-响应的过程花费的时间
         */
        public long serverDate;

        /**
         * The last modified date for the requested object.
         */
        public long lastModified;

        /**
         * TTL for this record.
         * 缓存过期的时间
         */
        public long ttl;

        /**
         * Soft TTL for this record.
         * 缓存新鲜时间
         */
        public long softTtl;

        /**
         * Immutable response headers as received from server; must be non-null.
         * 用于保存请求的url和数据
         */
        public Map<String, String> responseHeaders = Collections.emptyMap();

        /**
         * True if the entry is expired.
         * 判断是否新鲜(失效)
         * <p/>
         * 客户端虽然提交过这次请求，并且请求获取的数据报中的数据也已经被保存在了缓存当中，
         * 但是服务器端发生了改变..也就是说服务器数据发生了变化，那么就会导致这次请求对应的数据已经有所变化了，
         * 但是客户端缓存保存的仍然是没有改变的数据，因此即使缓存命中也无法获取到正确的数据信息，
         * 因此需要重新提交新的网络请求
         */
        public boolean isExpired() {
            return this.ttl < System.currentTimeMillis();
        }

        /**
         * True if a refresh is needed from the original data source.
         * 判断缓存是否需要刷新
         */
        public boolean refreshNeeded() {
            return this.softTtl < System.currentTimeMillis();
        }
    }

}
