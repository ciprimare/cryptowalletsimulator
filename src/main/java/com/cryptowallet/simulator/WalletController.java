package com.cryptowallet.simulator;

import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.model.wallet.WalletEntryTransaction;
import com.cryptowallet.simulator.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<?> createWallet(@Valid @RequestBody Wallet wallet) {
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
        return ResponseEntity
                .status(OK)
                .body((walletService.deleteWallet(uuid)) ? "Success" : "Failed");
    }

    /**
     * Exchange currency using CryptoCompare conversion rate and transfer a in the same or in a different wallet
     * If tow is request param is missing than we will consider exchanging the currency in the same wallet
     *
     * @param fromWalletId           - wallet from where we take the currency and amount to be exchanged
     * @param toWalletId             - destination wallet if missing will be the current wallet and will be just a simple exchange currency
     * @param walletEntryTransaction - has two wallet entries from wallet entry and destination wallet entry
     * @return - the wallet where the new entry has been added
     */
    @PutMapping("/{fromWalletId}/exchange")
    public ResponseEntity<Wallet> exchangeCurrency(@PathVariable(name = "fromWalletId") String fromWalletId,
                                                   @RequestParam(name = "toWalletId", required = false) String toWalletId,
                                                   @RequestBody @Valid WalletEntryTransaction walletEntryTransaction) {
        return ResponseEntity
                .status(OK)
                .body(walletService.exchange(fromWalletId, toWalletId, walletEntryTransaction));
    }
}
