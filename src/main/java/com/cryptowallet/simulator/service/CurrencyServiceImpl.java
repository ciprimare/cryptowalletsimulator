package com.cryptowallet.simulator.service;

import com.cryptowallet.simulator.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public Set<String> getAllCryptoCurrencies() {
        return currencyRepository.getSupportedCryptoCurrencies();
    }
}
