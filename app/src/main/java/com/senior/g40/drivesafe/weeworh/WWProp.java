package com.senior.g40.drivesafe.weeworh;

/**
 * Created by PNattawut on 16-Mar-17.
 */

public class WWProp {
//        public static final String WEEWORH_HOST = "wwh.nata8ify.me/";
    public static final String WEEWORH_HOST = "http://wwh.nata8ify.me:8080/WeeWorh-1.0-SNAPSHOT/";

    public class URI {
        public static final String LOGIN = WEEWORH_HOST + "DriverIn?opt=login&utyp=M";
        public static final String CRASH_HIT = WEEWORH_HOST + "DriverIn?opt=acchit";
        public static final String INCIDENT_REPORT = WEEWORH_HOST + "ReporterAppIn?opt=report";
        public static final String SYS_FALSE_CRASH = WEEWORH_HOST + "ReporterAppIn?opt=sys_accfalse";
        public static final String USR_FALSE_CRASH = WEEWORH_HOST + "ReporterAppIn?opt=usr_accfalse";
        public static final String GET_UPDATE_REPORTED_INCIDENT = WEEWORH_HOST + "ReporterAppIn?opt=update_accident_info";
    }
    public class PARAM {
        /* Login Parameters */
        public static final String USRN = "usrn";
        public static final String PSWD = "pswd";
        public static final String UTYP = "utyp";

        /* Request for Rescue Parameters */
        public static final String LAT = "lat"; // Latitude
        public static final String LNG = "lng"; // Longitude
        public static final String FDT = "fdt"; // Force Detect
        public static final String SDT = "sdt"; // Speed Detect
        public static final String USRID = "usrid"; // UserID
        public static final String USERID = "userId"; // UserID
        public static final String ACCCO = "accc"; // Accident Code
        public static final String ACTYP = "acctype"; // Accident Type
        public static final String ACCID = "accid"; // Accident ID
    }

    /* LOG TAG */
    public class TAG{
        public static final String RESULT_IS = "Result Is $_ ";
    }


}
