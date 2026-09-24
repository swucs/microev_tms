package com.obigo.microev.tms.core.util;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ClientIpUtils {

    public static String getUserIP(HttpServletRequest request) {

        String ip = request.getHeader("X-FORWARDED-FOR");

        log.debug("ip : X-FORWARDED-FOR : {}", ip);
        if (ip != null) {
            String[] split = ip.split(",");
            ip = split[0];
            if (StringUtils.isNotBlank(ip)) {
                return ip;
            }
        }


        ip = request.getHeader("Proxy-Client-IP");
        log.debug("ip : Proxy-Client-IP : {}", ip);
        if (StringUtils.isNotBlank(ip)) {
            return ip;
        }



        ip = request.getHeader("WL-Proxy-Client-IP");
        log.debug("ip : WL-Proxy-Client-IP : {}", ip);
        if (StringUtils.isNotBlank(ip)) {
            return ip;
        }


        ip = request.getHeader("HTTP_CLIENT_IP");
        log.debug("ip : HTTP_CLIENT_IP : {}", ip);
        if (StringUtils.isNotBlank(ip)) {
            return ip;
        }

        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        log.debug("ip : HTTP_X_FORWARDED_FOR : {}", ip);
        if (StringUtils.isNotBlank(ip)) {
            return ip;
        }

        ip = request.getRemoteAddr();
        log.debug("ip : RemoteAddr : {}", ip);
        return ip;
    }

}
