package com.obigo.microev.tms.api.infrastructure.threadLocal;

/**
 * 현재 사용자 정보를 담고 있는 ThreadLocal
 */
public class CurrentUserContextHolder {

    private static final ThreadLocal<CurrentUser> currentUser = new ThreadLocal<>();

    public static void set(CurrentUser currentUser) {
        CurrentUserContextHolder.currentUser.set(currentUser);
    }

    public static CurrentUser get() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }
}
