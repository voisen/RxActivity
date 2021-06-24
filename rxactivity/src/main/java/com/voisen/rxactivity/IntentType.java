package com.voisen.rxactivity;

public enum IntentType {
    /**
     * Intent.setPackageName(String)
     */
    PackageName,

    /**
     * Intent.setData(Uri)
     */
    Data,

    /**
     * Intent.setComponent(ComponentName)
     */
    Component,

    /**
     * Intent.setType(String)
     */
    Type,

    /**
     * Intent.setAction(String)
     */
    Action,

    /**
     * Intent.setFlags(int)
     */
    Flags,

    /**
     * Intent.setIdentifier(String)
     */
    Identifier,

    None,
}
