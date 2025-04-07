package com.skcc.ra.bap.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class SysUtils extends StringUtils {

    public static void setSystemProperties() {
        // Pod별 로그파일 분리 설정(Pod HostName 설정)
        String podHostName = System.getenv("HOSTNAME");
        if (StringUtils.isEmpty(podHostName)) {
            podHostName = "local";
        }
        System.setProperty("pod.host.name", podHostName);

    }

}
