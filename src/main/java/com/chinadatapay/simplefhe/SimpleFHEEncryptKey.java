package com.chinadatapay.simplefhe;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SimpleFHEEncryptKey {

    private BigInteger p;

    private BigInteger n;

    private int hashcode = 0;

    public SimpleFHEEncryptKey(int modulusLength) throws SimpleFHEException {
        if (modulusLength >= 8 && modulusLength % 8 == 0) {
            int primeLength = modulusLength / 2;
            SecureRandom random = new SecureRandom();

            do {
                p = BigInteger.probablePrime(primeLength, random);

                /**
                 * The first prime number, {@code q} such that  {@code p*q = publicKey.modulus}.
                 */
                BigInteger q;
                do {
                    q = BigInteger.probablePrime(primeLength, random);
                } while(p.equals(q));

                n = p.multiply(q);
            } while(n.bitLength() != modulusLength);

        } else {
            throw new SimpleFHEException(SimpleFHEErrorCode.SIMPLE_FHE_CREATE_ENCRYPT_KEY_ERROR,
                    "modulusLength must be a multiple of 8");
        }
    }

    public BigInteger getPrivateKey() {
        return  p;
    }

    public BigInteger getPublicKey() {
        return n;
    }

    public SimpleFHEContext createSignedContext() {
        return new SimpleFHEContext(this);
    }

    @Override
    public int hashCode() {
        if (hashcode != 0) {
            return hashcode;
        }

        String sb = String.valueOf(p) + n;
        char[] charArr = sb.toCharArray();
        for(char c : charArr) {
            hashcode = hashcode * 17 + c;
        }
        return hashcode;
    }

    /*
        解密
        m = c mod n = c mod p = m

     */
    public BigInteger decrypt(SimpleFHEEncryptedNumber encrypted) throws SimpleFHEException {
        if (!n.equals(encrypted.getContext().getPublicKey())) {
            throw new SimpleFHEException(SimpleFHEErrorCode.SIMPLE_FHE_KEY_MISMATCH_ERROR, "Mismatched key");
        }

        return encrypted.getValue().mod(p);
    }
}
