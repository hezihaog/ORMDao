package com.hzh.orm.dao.core;

public class DaoLogger {
    private static Log sLog;

    public static void setLogger(Log log) {
        sLog = log;
    }

    public static void info(String msg) {
        if (sLog != null) {
            sLog.info(msg);
        }
    }

    public static void debug(String msg) {
        if (sLog != null) {
            sLog.debug(msg);
        }
    }

    public static void error(String msg) {
        if (sLog != null) {
            sLog.error(msg);
        }
    }

    public interface Log {
        /**
         * 普通日志
         */
        void info(String msg);

        /**
         * 调试日志
         */
        void debug(String msg);

        /**
         * 异常日志
         */
        void error(String msg);
    }
}