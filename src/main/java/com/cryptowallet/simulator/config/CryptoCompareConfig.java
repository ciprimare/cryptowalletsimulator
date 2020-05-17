package com.cryptowallet.simulator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "crypto.compare")
public class CryptoCompareConfig {
    private String url;
    private String apiKey;
    private String allCurrencies;
    private String price;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }


    public String getAllCurrencies() {
        return allCurrencies;
    }

    public void setAllCurrencies(String allCurrencies) {
        this.allCurrencies = allCurrencies;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
