
package org.lpro.control;

import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

public class KeyManagement {
     public Key generateKey() {
        String keyString = "gIpE";
        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
        return key;
    }
}
