package com.example.ShopAppEcomere.repository.httpclient;

import com.example.ShopAppEcomere.dto.request.ExchangeTokenRequest;
import com.example.ShopAppEcomere.dto.response.auth.ExchangeTokenResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "outbound-identity", url = "https://oauth2.googleapis.com")
public interface OutboundShopAppClient{
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenResponse exchangeToken(@RequestBody ExchangeTokenRequest request);

}
