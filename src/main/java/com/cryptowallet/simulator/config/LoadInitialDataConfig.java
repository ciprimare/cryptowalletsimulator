package com.cryptowallet.simulator.config;

import com.cryptowallet.simulator.model.cryptocompare.CryptoCurrencies;
import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.repository.CurrencyRepository;
import com.cryptowallet.simulator.repository.WalletMockDataGenerator;
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
class LoadInitialDataConfig {
    @Autowired
    CryptoCompareConfig cryptoCompareConfig;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    WalletMockDataGenerator walletMockDataGenerator;
    @Autowired
    Logger logger;

    @Bean
    public void startSync() {
        // Remove this call if scheduling politics changes
        final Set<String> supportedCryptoCurrencies = cryptoCurrenciesAutoSync();
        initWallets(supportedCryptoCurrencies);
    }

    private void initWallets(Set<String> supportedCryptoCurrencies) {
        for (Wallet wallet : walletMockDataGenerator.generateWallets()) {
            walletRepository.saveOrUpdateWallet(wallet, supportedCryptoCurrencies);
        }
    }

    @Scheduled(cron = "0 0 * * 7 ?")
    private Set<String> cryptoCurrenciesAutoSync() {
        logger.info("start syncing crypto cryptoCurrencies");
        final Set<String> cryptoCurrencies = getAllCryptoCurrencies();
        currencyRepository.saveSupportedCryptoCurrencies(cryptoCurrencies);
        return cryptoCurrencies;
    }

    private Set<String> getAllCryptoCurrencies() {
        final String url = cryptoCompareConfig.getUrl()
                + format(cryptoCompareConfig.getAllCurrencies(), cryptoCompareConfig.getApiKey());
        final CryptoCurrencies cryptoCurrencies = restTemplate.getForObject(url, CryptoCurrencies.class);
        return cryptoCurrencies.getCoins().keySet();
    }
}
