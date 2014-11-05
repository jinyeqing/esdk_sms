package com.huawei.esdk.tcp.base;

import java.util.List;

import com.huawei.esdk.tcp.base.pdu.PDU;

public interface ByteDataProcessor
{
    List<PDU> processByteData(byte[] byteData);
}
