package com.example.ShopAppEcomere.util;

import com.example.ShopAppEcomere.config.VNPayConfig;
import jakarta.xml.bind.DatatypeConverter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class VNPayUtil {

    public static String createVNPayUrl(String txnRef, long amount, String ipAddr, VNPayConfig config)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Map<String, String> params = new HashMap<>();

        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", config.getTmnCode());
        params.put("vnp_Amount", String.valueOf(amount * 100)); // nhân 100 theo yêu cầu VNPay
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", "Thanh toan don hang " + txnRef); // KHÔNG được chứa dấu tiếng Việt
        params.put("vnp_OrderType", "billpayment");
        params.put("vnp_ReturnUrl", config.getReturnUrl());
        params.put("vnp_IpAddr", ipAddr);
        params.put("vnp_Locale", "vn");
        params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        System.out.println("VNPay Params: " + params);
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String name : fieldNames) {
            String value = params.get(name);
            hashData.append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
            query.append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
            if (!name.equals(fieldNames.get(fieldNames.size() - 1))) {
                hashData.append('&');
                query.append('&');
            }
        }

        String secureHash = hmacSHA512(config.getHashSecret(), hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        return config.getPayUrl() + "?" + query.toString();
    }

    private static String hmacSHA512(String key, String data) throws NoSuchAlgorithmException {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKey);
            byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(hash).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("HMAC SHA512 error", e);
        }
    }
    public static boolean verifySignature(Map<String, String> fields, String secretKey) {
        String receivedHash = fields.remove("vnp_SecureHash");

        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder data = new StringBuilder();
        try {
            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                String fieldValue = fields.get(fieldName);
                // Bao gồm cả trường có giá trị rỗng
                data.append(fieldName).append("=").append(URLEncoder.encode(fieldValue != null ? fieldValue : "", StandardCharsets.US_ASCII.toString()));

                if (i < fieldNames.size() - 1) {
                    data.append("&");
                }
            }
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error encoding URL for VNPAY signature verification: " + e.getMessage());
            return false;
        }

        System.out.println("VNPay Verify Data String: " + data.toString()); // Log chuỗi dữ liệu để băm

        try {
            String computedHash = hmacSHA512(secretKey, data.toString());
            System.out.println("VNPay Received Hash: " + receivedHash); // Log hash nhận được
            System.out.println("VNPay Computed Hash: " + computedHash); // Log hash tính toán được
            return computedHash.equals(receivedHash != null ? receivedHash.toUpperCase() : null);
        } catch (Exception e) {
            System.err.println("HMAC SHA512 error during VNPAY signature verification: " + e.getMessage());
            return false;
        }
    }
}