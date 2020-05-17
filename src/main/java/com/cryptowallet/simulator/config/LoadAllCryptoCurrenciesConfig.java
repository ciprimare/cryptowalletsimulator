package com.cryptowallet.simulator.config;

import com.cryptowallet.simulator.model.cryptocompare.CryptoCurrencies;
import com.cryptowallet.simulator.repository.WalletRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

import static java.text.MessageFormat.format;

@Configuration
@EnableScheduling
class LoadAllCryptoCurrenciesConfig {
    @Autowired
    CryptoCompareConfig cryptoCompareConfig;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    WalletRepository walletDao;
    @Autowired
    Logger logger;

    @Bean
    public void startSync() {
        // Remove this call if scheduling politics changes
        cryptoCurrenciesAutoSync();
    }

    @Scheduled(cron = "0 0 * * 7 ?")
    private void cryptoCurrenciesAutoSync() {
        logger.info("start syncing crypto cryptoCurrencies");
        final Set<String> cryptoCurrencies = getAllCryptoCurrencies();
        walletDao.saveCryptoCurrencies(cryptoCurrencies);
    }

    private Set<String> getAllCryptoCurrencies() {
        final String url = cryptoCompareConfig.getUrl()
                + format(cryptoCompareConfig.getAllCurrencies(), cryptoCompareConfig.getApiKey());
        final CryptoCurrencies cryptoCurrencies = restTemplate.getForObject(url, CryptoCurrencies.class);
        return cryptoCurrencies.getCoins().keySet();
    }
}
