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
 * Default retry policy for requests.
 */
public class DefaultRetryPolicy implements RetryPolicy {
    /**
     * The current timeout in milliseconds.
     * 超时时间
     */
    private int mCurrentTimeoutMs;

    /**
     * The current retry count.
     * 重试次数
     */
    private int mCurrentRetryCount;

    /**
     * The maximum number of attempts.
     * 允许重试的最大次数
     */
    private final int mMaxNumRetries;

    /**
     * The backoff multiplier for the policy.
     * 一个乘数因子，每次超时时间的获取都需要乘上这个乘数因子
     */
    private final float mBackoffMultiplier;

    /**
     * The default socket timeout in milliseconds
     * 默认的超时时间2.5s((表示每一次重试耗费掉的时间的总和))
     */
    public static final int DEFAULT_TIMEOUT_MS = 2500;

    /**
     * The default number of retries
     * 默认的最大重试次数
     */
    public static final int DEFAULT_MAX_RETRIES = 0;

    /**
     * The default backoff multiplier
     * 默认的乘数因子
     */
    public static final float DEFAULT_BACKOFF_MULT = 1f;


    /**
     * Constructs a new retry policy using the default timeouts.
     */
    public DefaultRetryPolicy() {
        // 第一个代表超时时间：即超过DEFAULT_TIMEOUT_MSS认为超时，第三个参数代表最大重试次数，这里设置为1.0f代表如果超时，则不重试
        this(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
    }

    /**
     * Constructs a new retry policy.
     *
     * @param initialTimeoutMs  The initial timeout for the policy. 超时时间
     * @param maxNumRetries     The maximum number of retries. 最大重试次数
     * @param backoffMultiplier Backoff multiplier for the policy. 设置为1.0f代表如果超时，则不重试
     */
    public DefaultRetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier) {
        mCurrentTimeoutMs = initialTimeoutMs;
        mMaxNumRetries = maxNumRetries;
        mBackoffMultiplier = backoffMultiplier;
    }

    /**
     * Returns the current timeout.
     */
    @Override
    public int getCurrentTimeout() {
        return mCurrentTimeoutMs;
    }

    /**
     * Returns the current retry count.
     */
    @Override
    public int getCurrentRetryCount() {
        return mCurrentRetryCount;
    }

    /**
     * Returns the backoff multiplier for the policy.
     */
    public float getBackoffMultiplier() {
        return mBackoffMultiplier;
    }

    /**
     * Prepares for the next retry by applying a backoff to the timeout.
     *
     * @param error The error code of the last attempt.
     */
    @Override
    public void retry(VolleyError error) throws VolleyError {
        //记录超过了允许的次数
        mCurrentRetryCount++;
        //记录超时的时间
        mCurrentTimeoutMs += (mCurrentTimeoutMs * mBackoffMultiplier);

        if (!hasAttemptRemaining()) {
            throw error;
        }
    }

    /**
     * Returns true if this policy has attempts remaining, false otherwise.
     */
    protected boolean hasAttemptRemaining() {
        return mCurrentRetryCount <= mMaxNumRetries;
    }
}
