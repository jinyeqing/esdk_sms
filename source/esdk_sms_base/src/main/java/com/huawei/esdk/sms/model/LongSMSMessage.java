package com.huawei.esdk.sms.model;

import java.util.Arrays;

public class LongSMSMessage extends SMSMessage implements
        Comparable<LongSMSMessage>
{
    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1L;

    private final int random;

    private final int count;

    private final int sequence;

    public LongSMSMessage(int random, int count, int sequence)
    {
        this.random = random;
        this.count = count;
        this.sequence = sequence;
    }

    public int getRandom()
    {
        return random;
    }

    public int getCount()
    {
        return count;
    }

    public int getSequence()
    {
        return sequence;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + count;
        result = prime * result + random;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof LongSMSMessage))
        {
            return false;
        }
        LongSMSMessage other = (LongSMSMessage) obj;
        if (count != other.count)
        {
            return false;
        }
        if (random != other.random)
        {
            return false;
        }
        if (!getSrcId().equals(other.getSrcId()))
        {
            return false;
        }
        if (!Arrays.equals(getDestId(), other.getDestId()))
        {
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(LongSMSMessage o)
    {
        if (this.sequence > o.getSequence())
        {
            return 1;
        }
        else if (this.sequence == o.getSequence())
        {
            return 0;
        }
        else
        {
            return -1;
        }
    }

    public String getSMS4Logging()
    {
        return "id=" + getId() + ", From=" + getSrcId() + ", To="
                + getDestIdAsString() + ", ReceivedTime=" + getMsgTime()
                + ", MessageLength=" + getContent().length() + ",Random="
                + random + ", Count=" + count + ", Sequence=" + sequence;
    }
}
