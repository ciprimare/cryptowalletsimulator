package com.cryptowallet.simulator;

import com.cryptowallet.simulator.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    /**
     * all crypto currencies from https://min-api.cryptocompare.com/data/blockchain/list
     *
     * @return a list with all crypto currencies symbols
     */
    @GetMapping
    public ResponseEntity<Set<String>> listCryptoCurrencies() {
        return ResponseEntity
                .ok()
                .body(currencyService.getAllCryptoCurrencies());
    }
}
