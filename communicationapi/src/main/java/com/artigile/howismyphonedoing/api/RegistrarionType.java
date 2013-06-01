package com.artigile.howismyphonedoing.api;

/**
 * User: ioanbsu
 * Date: 5/31/13
 * Time: 7:39 PM
 */
public enum RegistrarionType {
    REGISTER,
    UNREGISTER;

    public boolean of(String parameter) {
        if(parameter==null){
            return false;
        }
        return RegistrarionType.valueOf(parameter)==this;
    }

}
