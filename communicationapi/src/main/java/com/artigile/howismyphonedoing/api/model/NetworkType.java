package com.artigile.howismyphonedoing.api.model;

/**
 * User: ioanbsu
 * Date: 6/25/13
 * Time: 1:32 PM
 */
public enum NetworkType {
    NETWORK_TYPE_UNKNOWN ,
    /** Current network is GPRS */
    NETWORK_TYPE_GPRS ,
    /** Current network is EDGE */
    NETWORK_TYPE_EDGE,
    /** Current network is UMTS */
    NETWORK_TYPE_UMTS,
    /** Current network is CDMA: Either IS95A or IS95B*/
    NETWORK_TYPE_CDMA,
    /** Current network is EVDO revision 0*/
    NETWORK_TYPE_EVDO_0,
    /** Current network is EVDO revision A*/
    NETWORK_TYPE_EVDO_A,
    /** Current network is 1xRTT*/
    NETWORK_TYPE_1xRTT,
    /** Current network is HSDPA */
    NETWORK_TYPE_HSDPA,
    /** Current network is HSUPA */
    NETWORK_TYPE_HSUPA,
    /** Current network is HSPA */
    NETWORK_TYPE_HSPA,
    /** Current network is iDen */
    NETWORK_TYPE_IDEN,
    /** Current network is EVDO revision B*/
    NETWORK_TYPE_EVDO_B,
    /** Current network is LTE */
    NETWORK_TYPE_LTE,
    /** Current network is eHRPD */
    NETWORK_TYPE_EHRPD,
    /** Current network is HSPA+ */
    NETWORK_TYPE_HSPAP,
}
