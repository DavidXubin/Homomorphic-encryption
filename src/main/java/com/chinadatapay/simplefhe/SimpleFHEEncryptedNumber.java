package com.chinadatapay.simplefhe;

import java.math.BigInteger;

public class SimpleFHEEncryptedNumber implements Comparable {

    private BigInteger value;

    protected final SimpleFHEContext context;

    public SimpleFHEEncryptedNumber() {
        this.context = null;
    }

    public SimpleFHEEncryptedNumber(SimpleFHEContext context, BigInteger value) {
        this.context = context;

        this.value = value;
    }

    public SimpleFHEEncryptedNumber add(SimpleFHEEncryptedNumber other) throws SimpleFHEException {
        assert context != null;
        return context.add(this, other);
    }


    public SimpleFHEEncryptedNumber add(long other) throws SimpleFHEException {

        assert context != null;
        return context.add(this, context.encrypt(other));
    }

    public SimpleFHEEncryptedNumber subtract(SimpleFHEEncryptedNumber other) throws SimpleFHEException {

        assert context != null;
        return context.subtract(this, other);
    }

    public SimpleFHEEncryptedNumber subtract(long other) throws SimpleFHEException {

        assert context != null;
        return context.subtract(this, context.encrypt(other));
    }

    public SimpleFHEEncryptedNumber multiply(SimpleFHEEncryptedNumber other) throws SimpleFHEException {

        assert context != null;
        return context.multiply(this, other);
    }

    public SimpleFHEEncryptedNumber multiply(long other) throws SimpleFHEException {

        assert context != null;
        return context.multiply(this, context.encrypt(other));
    }

    public BigInteger getValue() {
        return value;
    }

    public SimpleFHEContext getContext() {
        return context;
    }

    public long decryptAsLong(SimpleFHEEncryptKey key) throws SimpleFHEException {
        BigInteger value = key.decrypt(this);

        BigInteger valueRange = key.getPrivateKey().shiftRight(1);
        if (valueRange.compareTo(value) < 0) {
            value = value.subtract(key.getPrivateKey());
        }

        return value.longValue();
    }

    public int decryptAsInt(SimpleFHEEncryptKey key) throws SimpleFHEException {
        BigInteger value = key.decrypt(this);

        BigInteger valueRange = key.getPrivateKey().shiftRight(1);
        if (valueRange.compareTo(value) < 0) {
            value = value.subtract(key.getPrivateKey());
        }

        return value.intValue();
    }

    @Override
    public String toString() {
        return "value = " + value;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof SimpleFHEEncryptedNumber) {

            SimpleFHEEncryptedNumber other = (SimpleFHEEncryptedNumber)o;

            return this.value.compareTo(other.value);
        }

        return 0;
    }
}
