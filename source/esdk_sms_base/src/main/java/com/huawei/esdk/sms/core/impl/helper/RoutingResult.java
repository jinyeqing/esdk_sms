package com.huawei.esdk.sms.core.impl.helper;

import java.util.ArrayList;
import java.util.List;

import com.huawei.esdk.sms.core.ISMSAdapter;

public class RoutingResult
{
    private List<ISMSAdapter> adapters = new ArrayList<ISMSAdapter>();
    
    private List<List<String>> adaptersMappingNumbers = new ArrayList<List<String>>();
    
    private List<String> unMatchedMobileNumbers = new ArrayList<String>();

    public List<ISMSAdapter> getAdapters()
    {
        return adapters;
    }

    public void setAdapters(List<ISMSAdapter> adapters)
    {
        this.adapters = adapters;
    }

    public List<List<String>> getAdaptersMappingNumbers()
    {
        return adaptersMappingNumbers;
    }

    public void setAdaptersMappingNumbers(List<List<String>> adaptersMappingNumbers)
    {
        this.adaptersMappingNumbers = adaptersMappingNumbers;
    }

    public List<String> getUnMatchedMobileNumbers()
    {
        return unMatchedMobileNumbers;
    }

    public void setUnMatchedMobileNumbers(List<String> unMatchedMobileNumbers)
    {
        this.unMatchedMobileNumbers = unMatchedMobileNumbers;
    }
}
