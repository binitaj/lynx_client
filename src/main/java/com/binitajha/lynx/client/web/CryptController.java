package com.binitajha.lynx.client.web;

import com.binitajha.lynx.client.model.Secret;
import jakarta.websocket.server.PathParam;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class CryptController {

    static Long l = Long.valueOf(System.currentTimeMillis() / 100000l - 17273062l);

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping(path = "/encrypt")
    public ResponseEntity<String> encrypt(@RequestParam String q) {
        Secret s = getSecret(q, false);
        System.out.print("Attempting to encrypt the string: '" + q);
        ResponseEntity<Secret> resp = restTemplate.postForEntity("https://localhost:8443/encrypt", s, Secret.class);
        String respStr = resp.getBody().encrypted;
        System.out.println("', ciphertext: '"  + respStr + "'");
        return ResponseEntity.ok(respStr);
    }

    @GetMapping(path = "/decrypt")
    public ResponseEntity<String> decrypt(@RequestParam String q) {
        Secret s = getSecret(q, true);
        System.out.print("Attempting to decrypt the ciphertext: '" + q);
        ResponseEntity<Secret> resp = restTemplate.postForEntity("https://localhost:8443/decrypt", s, Secret.class);
        String respStr = resp.getBody().data;
        System.out.println("', retrieved: '"  + respStr + "'");
        return ResponseEntity.ok(respStr);
    }

    private Secret getSecret(String rawtext, boolean isEncrypted) {
        Secret s = new Secret();
        s.id = l.toString();
        s.key = "key";
        if (isEncrypted) s.encrypted = rawtext; else s.data = rawtext;
        return s;
    }
}
