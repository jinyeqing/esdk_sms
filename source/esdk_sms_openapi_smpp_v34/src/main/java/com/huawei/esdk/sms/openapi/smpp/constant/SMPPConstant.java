package com.huawei.esdk.sms.openapi.smpp.constant;

public interface SMPPConstant
{
    int Generic_Nack_Command_Id = 0;
    
    int Bind_Receiver_Command_Id = 1;
    
    int Bind_Receiver_Rep_Command_Id = -2147483647;
    
    int Bind_Transmitter_Command_Id = 2;
    
    int Bind_Transmitter_Rep_Command_Id = -2147483646;
    
    int Bind_Transceiver_Command_Id = 9;
    
    int Bind_Transceiver_Rep_Command_Id = -2147483639;
    
    int Submit_Command_Id = 4;
    
    int Submit_Rep_Command_Id = -2147483644;
    
    int Deliver_Command_Id = 5;
    
    int Deliver_Rep_Command_Id = -2147483643;
    
    int Unbind_Command_Id = 6;
    
    int Unbind_Rep_Command_Id = -2147483642;
    
    int Enquire_Link_Command_Id = 21;
    
    int Enquire_Link_Rep_Command_Id = -2147483627;
    
    int Submit_Multi_Id = 33;
    
    int Submit_Multi_Resp_Id = -2147483615;
}
