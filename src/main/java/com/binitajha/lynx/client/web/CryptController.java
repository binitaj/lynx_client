package com.binitajha.lynx.client.web;

import com.binitajha.lynx.client.model.Secret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
public class CryptController {

    static Long l = Long.valueOf(System.currentTimeMillis() / 100000l - 17273062l);

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping(path = "/encrypt")
    public ResponseEntity<String> encrypt(@RequestParam String q, @RequestParam String k) {
        Secret s = getSecret(k, q, false);
//        System.out.print("Attempting to encrypt the string: '" + q);
        ResponseEntity<Secret> resp = restTemplate.postForEntity("https://localhost:8443/encrypt", s, Secret.class);
        if(resp.hasBody()) {
        String respStr = resp.getBody().encrypted;
//        System.out.println("', ciphertext: '"  + respStr + "'");
        return ResponseEntity.ok(respStr);
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping(path = "/decrypt")
    public ResponseEntity<String> decrypt(@RequestParam String q) {
        Secret s = getSecret(q, true);
//        System.out.print("Attempting to decrypt the ciphertext: '" + q);
        ResponseEntity<Secret> resp = restTemplate.postForEntity("https://localhost:8443/decrypt", s, Secret.class);
        if(resp.hasBody()) {
        String respStr = resp.getBody().data;
//        System.out.println("', retrieved: '"  + respStr + "'");
        return ResponseEntity.ok(respStr);
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping(path = "/store")
    public ResponseEntity<String> store(@RequestParam String q, @RequestParam String k) {
        Secret s = getSecret(k, q, false);
        System.out.print("Attempting to store the string: '" + q);
        ResponseEntity<Secret> resp = restTemplate.postForEntity("https://localhost:8443/store", s, Secret.class);
        if(resp.hasBody()) {
            String respStr = resp.getBody().encrypted;
            System.out.println("', ciphertext: '" + respStr + "'");
            return ResponseEntity.ok(respStr);
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }


    @GetMapping(path = "/retrieve")
    public ResponseEntity<String> retrieve(@RequestParam String k) {
        Secret s = getSecretWithKey(k);
        System.out.print("Attempting to retrieve data for the key: '" + k);
        String respStr = null;
        try {
            ResponseEntity<Secret> resp = restTemplate.postForEntity("https://localhost:8443/retrieve", s, Secret.class);
            if (resp.hasBody()) respStr = resp.getBody().data;
            else respStr = String.format("No data for key '%s' found", k);
        } catch(HttpClientErrorException hcee) {
            respStr = String.format("No data for key '%s' found", k);
        }
        System.out.println("', ciphertext: '"  + respStr + "'");
        return ResponseEntity.ok(respStr);
    }

    private Secret getSecretWithKey(String k) {
        Secret s = new Secret();
        s.id = l.toString();
        l ++;
        s.key = k;
        return s;
    }

    private Secret getSecret(String key, String rawtext, boolean isEncrypted) {
        Secret s = new Secret();
        s.id = l.toString();
        s.key = key;
        if (isEncrypted) s.encrypted = rawtext; else s.data = rawtext;
        return s;
    }

    private Secret getSecret(String rawtext, boolean isEncrypted) {
        Secret s = new Secret();
        s.id = l.toString();
        s.key = "key";
        if (isEncrypted) s.encrypted = rawtext; else s.data = rawtext;
        return s;
    }
}
