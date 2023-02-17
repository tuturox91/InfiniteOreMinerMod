package com.sniklz.infiniteminerblock.client;

import com.sniklz.infiniteminerblock.util.Observed;
import com.sniklz.infiniteminerblock.util.Observer;
import com.sniklz.infiniteminerblock.util.ObserverImpl;

public class ClientData extends ObserverImpl<Integer> {
    private static int OreSize;

    public static int getOreSize() {
        return OreSize;
    }

    public static void setOreSize(int oreSize) {
        OreSize = oreSize;
    }
}
