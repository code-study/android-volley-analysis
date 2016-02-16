package com.android.volley;

/**
 * Indicates that there was a redirection.
 * 重定向错误
 */
public class RedirectError extends VolleyError {

    public RedirectError() {
    }

    public RedirectError(final Throwable cause) {
        super(cause);
    }

    public RedirectError(final NetworkResponse response) {
        super(response);
    }
}
