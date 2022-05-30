package com.chinadatapay.simplefhe;

import java.math.BigInteger;

import static com.chinadatapay.utils.BigIntegerUtil.randomPositiveNumber;


public class SimpleFHEContext {

    private SimpleFHEEncryptKey encryptKey;

    /**
     * The maximum value that can be encoded and encrypted using the associated {@code publicKey}.
     */
    private final BigInteger maxSignificand;

    /**
     * The minimum value that can be encoded and encrypted using the associated {@code publicKey}.
     */
    private final BigInteger minSignificand;

    public SimpleFHEContext(SimpleFHEEncryptKey key) {
        encryptKey = key;

        maxSignificand = key.getPrivateKey().add(BigInteger.ONE).shiftRight(1).subtract(BigInteger.ONE);
        minSignificand = maxSignificand.negate();
    }


    public SimpleFHEEncryptedNumber encrypt(long value) throws SimpleFHEException {
        BigInteger m = BigInteger.valueOf(value);

        if (m.abs().compareTo(maxSignificand) >= 0) {
            throw new SimpleFHEException(SimpleFHEErrorCode.SIMPLE_FHE_ENCRYPT_ERROR,
                    "value " + m.toString() + " exceeds the max limit");
        }

        if (value < 0) {
            m = encryptKey.getPublicKey().add(m);
        }

        return encrypt(m);
    }

    /*
        加密
        m是明文，c是密文， r是一个随机数
        c = (m + p * r) mod n
     */
    public SimpleFHEEncryptedNumber encrypt(BigInteger m) throws SimpleFHEException {

        try {
            BigInteger p = encryptKey.getPrivateKey();

            BigInteger n = encryptKey.getPublicKey();

            BigInteger r = randomPositiveNumber(n);

            BigInteger c = (m.add(p.multiply(r))).mod(n);

            return new SimpleFHEEncryptedNumber(this, c);
        } catch (Exception e) {
            throw new SimpleFHEException(SimpleFHEErrorCode.SIMPLE_FHE_ENCRYPT_ERROR, e);
        }
    }

    /*
        假设有2密文， c1和c2
        c1 =（m1 + p * r1）mod n
        c2 =（m2 + p * r2）mod n

        则有：
        c3 = c1 + c2 =（m1 + m2 + p * (r1 + r2 )) mod n
                      =  (m1 * m2 + p * r3) mod n

     */
    public SimpleFHEEncryptedNumber add(SimpleFHEEncryptedNumber operand1, SimpleFHEEncryptedNumber operand2)
            throws SimpleFHEException {
        checkSameContext(operand1);
        checkSameContext(operand2);

        try {
            final BigInteger result = operand1.getValue().add(operand2.getValue());

            return new SimpleFHEEncryptedNumber(this, result);
        } catch (Exception e) {
            throw new SimpleFHEException(SimpleFHEErrorCode.SIMPLE_FHE_ADD_SUBTRACT_ERROR, e);
        }
    }

    public SimpleFHEEncryptedNumber subtract(SimpleFHEEncryptedNumber operand1, SimpleFHEEncryptedNumber operand2)
            throws SimpleFHEException {
        checkSameContext(operand1);
        checkSameContext(operand2);

        try {
            final BigInteger result = operand1.getValue().subtract(operand2.getValue());

            return new SimpleFHEEncryptedNumber(this, result);
        } catch (Exception e) {
            throw new SimpleFHEException(SimpleFHEErrorCode.SIMPLE_FHE_ADD_SUBTRACT_ERROR, e);
        }
    }


    /*
        假设有2密文， c1和c2
        c1 =（m1 + p * r1）mod n
        c2 =（m2 + p * r2）mod n

        则有：
        c3 = c1 * c2 =（m1 * m2 +  m1 * p * r2 + m2 * p * r1 + p * p * r1 * r2) mod n
                      =  (m1 * m2 + p * r3) mod n

     */
    public SimpleFHEEncryptedNumber multiply(SimpleFHEEncryptedNumber operand1, SimpleFHEEncryptedNumber operand2)
            throws SimpleFHEException {
        checkSameContext(operand1);
        checkSameContext(operand2);

        try {
            final BigInteger result = operand1.getValue().multiply(operand2.getValue());

            return new SimpleFHEEncryptedNumber(this, result);
        } catch (Exception e) {
            throw new SimpleFHEException(SimpleFHEErrorCode.SIMPLE_FHE_MULTIPLY_DIVIDE_ERROR, e);
        }
    }

    private SimpleFHEEncryptedNumber checkSameContext(SimpleFHEEncryptedNumber other)
            throws SimpleFHEException {
        checkSameContext(other.getContext());
        return other;
    }

    private void checkSameContext(SimpleFHEContext context) throws SimpleFHEException {
        if (this == context) {
            return;
        }

        if(this.encryptKey.hashCode() != context.encryptKey.hashCode()) {
            throw new SimpleFHEException(SimpleFHEErrorCode.SIMPLE_PHE_CONTEXT_MISMATCH_ERROR, "check context");
        }
    }

    BigInteger getPublicKey() {
        return encryptKey.getPublicKey();
    }

}
