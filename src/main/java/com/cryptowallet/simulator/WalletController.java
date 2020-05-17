package com.cryptowallet.simulator;

import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    /**
     * all crypto currencies from https://min-api.cryptocompare.com/data/blockchain/list
     *
     * @return a list with all crypto currencies symbols
     */
    @GetMapping(value = "/currencies")
    public ResponseEntity<Set<String>> listCryptoCurrencies() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(walletService.getAllCryptoCurrencies());
    }

    /**
     * Create a new wallet with desired entries
     *
     * @param wallet - wallet to be created
     * @return return the new created wallet
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> createWallet(@RequestBody Wallet wallet) {
        return ResponseEntity
                .status(CREATED)
                .body(walletService.createWallet(wallet));
    }

    /**
     * return the list with all the existent wallets and corresponding entries in the wallet
     *
     * @return a set with all the wallets and the entries
     */
    @GetMapping(value = "/list")
    public ResponseEntity<Set<Wallet>> listWallets() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(walletService.getAllWallets());
    }

    /**
     * GET a wallet specifying the uuid
     *
     * @param uuid - uuid for the wallet we want to get
     * @return - the wallet which has the provided uuid
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<Wallet> getWallet(@PathVariable String uuid) {
        return ResponseEntity
                .status(OK)
                .body(walletService.getWallet(uuid));
    }

    /**
     * Update wallet with the new data provided
     *
     * @param wallet - wallet to be update
     * @return return the wallet with the new values
     */
    @PutMapping
    @ResponseBody
    public ResponseEntity<?> updateWallet(@RequestBody Wallet wallet) {
        return ResponseEntity
                .status(OK)
                .body(walletService.updateWallet(wallet));
    }

    /**
     * DELETE a wallet based on provided uuid
     *
     * @param uuid - wallet uuid to delete
     * @return success if wallet has been successfully deleted
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteWallet(@PathVariable String uuid) {
        walletService.deleteWallet(uuid);
        return ResponseEntity
                .status(OK)
                .body("Success");
    }

    // buy a currency
    /*
       Input: to wallet: from currency with amount i want to buy a currency
     */

    // transfer currencies between two wallets
    /*
       Input: from wallet: amount and currency, to wallet: currency
     */
}
