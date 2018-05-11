package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial;

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

public interface MpiAttributes {

    RequestBuilder requestBuilder = new RequestBuilder("");

    // Mpi Params
    default MpiAttributes setCavv(String cavv) {
        requestBuilder.addElement("cavv", cavv);
        return this;
    }

    default MpiAttributes setEci(String eci) {
        requestBuilder.addElement("eci", eci);
        return this;
    }

    default MpiAttributes setXid(String xid) {
        requestBuilder.addElement("xid", xid);
        return this;
    }

    default RequestBuilder buildMpiParams() {
        return requestBuilder;
    }
}