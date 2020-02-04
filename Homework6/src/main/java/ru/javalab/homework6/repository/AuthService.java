package ru.javalab.homework6.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.javalab.homework6.models.User;
import org.apache.commons.codec.binary.Hex;
import sun.plugin.javascript.navig.Array;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AuthService {

    public String getToken(User user) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            Header header = new Header();
            header.algorithm = "HS256";
            header.type = "jwt";
            Payload payload = new Payload();
            payload.id = user.getId();
            payload.isAdmin = RoleRepositoryImpl.getInstance().getPermissionByUserId(payload.id);
            payload.login = user.getLogin();
            Base64.Encoder encoder = Base64.getEncoder();
            String headerEncoded = encoder.encodeToString(mapper.writeValueAsString(header).getBytes());
            String payloadEncoded = encoder.encodeToString(mapper.writeValueAsString(payload).getBytes());
            StringBuilder token = new StringBuilder();
            token.append(headerEncoded)
                    .append("\\.")
                    .append(payloadEncoded)
                    .append("\\.")
                    .append(encode("JOTAROTSUGIWAKISAMADA", headerEncoded + "\\." + payloadEncoded));
            return token.toString();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public User decodeToken(String token) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String[] strings = token.split("\\.");
            Base64.Decoder decoder = Base64.getMimeDecoder();
            Base64.Encoder encoder = Base64.getEncoder();
            String headerString = new String(decoder.decode(strings[0]), StandardCharsets.UTF_8);
            String payloadStr = new String(decoder.decode(strings[1]), StandardCharsets.UTF_8);
            Header header = mapper.readValue(headerString, Header.class);
            Payload payload = mapper.readValue(payloadStr, Payload.class);
            String headerEncoded = encoder.encodeToString(mapper.writeValueAsString(header).getBytes());
            String payloadEncoded = encoder.encodeToString(mapper.writeValueAsString(payload).getBytes());
            if (verify(strings[2], headerEncoded + "\\." + payloadEncoded)) {
                return new User(payload.login, payload.id, payload.isAdmin);
            } else
                return null;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private boolean verify(String obtainedSignature, String data) {
        String signature = encode("JOTAROTSUGIWAKISAMADA", data);
        return obtainedSignature.equals(signature);
    }

    private static String encode(String key, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return new String(Hex.encodeHex(sha256_HMAC.doFinal(data.getBytes("UTF-8"))));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private class JsonJWT {
        Object header;
        Object payload;

        public Object getHeader() {
            return header;
        }

        public void setHeader(Object header) {
            this.header = header;
        }

        public Object getPayload() {
            return payload;
        }

        public void setPayload(Object payload) {
            this.payload = payload;
        }
    }

    private static class Payload {
        private int id;
        private boolean isAdmin;
        private String login;

        public Payload() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public void setAdmin(boolean admin) {
            isAdmin = admin;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }
    }

    private static class Header {
        private String algorithm;
        private String type;

        public Header() {
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public void setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
