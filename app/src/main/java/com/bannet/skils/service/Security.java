/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bannet.skils.service;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.billingclient.api.Purchase;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Security-related methods. For a secure implementation, all of this code
 * should be implemented on a server that communicates with the
 * application on the device. For the sake of simplicity and clarity of this
 * example, this code is included here and is executed on the device. If you
 * must verify the purchases on the phone, you should obfuscate this code to
 * make it harder for an attacker to replace the code with stubs that treat all
 * purchases as verified.
 */
public class Security {
    private static final String TAG = Security.class.getSimpleName();

    private static final String KEY_FACTORY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    //This you will get from Services & API in your application console
   // public static String BASE_64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhTGmKLeLH7EsW60ArN82DrkB8Hp1k9ZPwr+t7WEWYrw38MuV6q3r/T/QIHqQm/nQH/FNQVl584aJzUEizY+zUAeq6oZlOI8tjGRkHyoBPuU4ePsE3ybRVkHXoHuob8LOuMJRUdFdmEEnBPYv6Dn+mgZ0UR7KcXBVxceShsxUT8/wkKe/X7pQdsz1UZ5TTCqxxE5mIuKBbBI4wA0PHfTi4okhGBX2wz6bdbY+XNmdpacEGVuvfSsODPwy71QkCLB4jiHEw20TEn3ELaw6F1BtWk0/B2mTSHUAgCryUyMNUtP7AIB/m0EjW0749IElKOMVaUyBNHSu0MiJOCdz4NBN1QIDAQAB";
    public static String BASE_64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkBtONhHbkvtiJO/oiZcW0r/mIMnQUYl2PaDrWMfQNs+YLp2tkMZmYYCyikM9aUIHsOhY5n2dCovaG2Ehms9QCeh0Dp6TUES6GM1RYt+zOWgIlbcIbkF3gH/pE3v9I7e/K5VlTtGenVDWzexl7osKPUDXsaW5nNY5YaomZMPjzhdu26LZsr4BCSuFNTp8JfJ10uwyxfqdBi8dckn0rIvAGtDTGGtIPm/DyKbkr3A48oDis4u+mUARcvfmf+tMiVGaRTH8vA5jT82cj5ZZlMFGHhnSDzDxBmkjMsJYydkWlUcSwncyXGTQNjE3MbCcDyu6MtkG7/Wli+D+LvOnunelyQIDAQAB";

    /**
     * Verifies that the data was signed with the given signature, and returns
     * the verified purchase. The data is in JSON format and signed
     * with a private key. The data also contains the {@link Purchase.PurchaseState}
     * and product ID of the purchase.
     * @param base64PublicKey the base64-encoded public key to use for verifying.
     * @param signedData the signed JSON string (signed, not encrypted)
     * @param signature the signature for the data, signed with the private key
     */
    public static boolean verifyPurchase(String base64PublicKey, String signedData, String signature) {
       /* if (TextUtils.isEmpty(signedData) || TextUtils.isEmpty(base64PublicKey) ||
                TextUtils.isEmpty(signature)) {
            Log.e(TAG, "Purchase verification failed: missing data.");
            return false;
        }

        PublicKey key = com.itrdonline.medicalquizzes.Security.generatePublicKey(base64PublicKey);
        return com.itrdonline.medicalquizzes.Security.verify(key, signedData, signature);*/
        if (signedData == null) {
            Log.e(TAG, "data is null");
            return false;
        }

        boolean verified = false;
        if (!TextUtils.isEmpty(signature)) {
            PublicKey key = Security.generatePublicKey(base64PublicKey);
            verified = Security.verify(key, signedData, signature);
            if (!verified) {
                Log.e(TAG, "signature does not match data.");
                return false;
            }
        }
        return true;
    }



    /**
     * Generates a PublicKey instance from a string containing the
     * Base64-encoded public key.
     *
     * @param encodedPublicKey Base64-encoded public key
     * @throws IllegalArgumentException if encodedPublicKey is invalid
     */
    private static PublicKey generatePublicKey(String encodedPublicKey) {
        try {
            byte[] decodedKey = Base64.decode(encodedPublicKey, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
            return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, "Invalid key specification.");
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Verifies that the signature from the server matches the computed
     * signature on the data.  Returns true if the data is correctly signed.
     *
     * @param publicKey public key associated with the developer account
     * @param signedData signed data from server
     * @param signature server signature
     * @return true if the data and signature match
     */
    private static boolean verify(PublicKey publicKey, String signedData, String signature) {
        byte[] signatureBytes;
        try {
            signatureBytes = Base64.decode(signature, Base64.DEFAULT);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Base64 decoding failed.");
            return false;
        }
        try {
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(publicKey);
            sig.update(signedData.getBytes());
            if (!sig.verify(signatureBytes)) {
                Log.e(TAG, "Signature verification failed.");
                return false;
            }
            return true;
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "NoSuchAlgorithmException.");
        } catch (InvalidKeyException e) {
            Log.e(TAG, "Invalid key specification.");
        } catch (SignatureException e) {
            Log.e(TAG, "Signature exception.");
        }
        return false;
    }
}
