package com.cryptowallet.simulator.deserializer;

import com.cryptowallet.simulator.model.cryptocompare.CryptoMultiPrice;
import com.cryptowallet.simulator.model.cryptocompare.CryptoPrice;
import com.cryptowallet.simulator.model.cryptocompare.CryptoPriceNode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;


public class CryptoMultiPriceDeserializer extends JsonDeserializer<CryptoMultiPrice> {
    @Override
    public CryptoMultiPrice deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        final CryptoMultiPrice cryptoPrices = new CryptoMultiPrice();
        final List<CryptoPriceNode> elements = new ArrayList<>();
        final JsonNode root = parser.getCodec().readTree(parser);

        final Stream<Map.Entry<String, JsonNode>> nodes = createStreamFromIterator(root.fields());
        nodes.forEach(node -> elements.add(createCryptoPriceDataElement(node.getKey(), node.getValue())));

        cryptoPrices.setPrices(elements);
        return cryptoPrices;
    }

    private Stream<Map.Entry<String, JsonNode>> createStreamFromIterator(Iterator<Map.Entry<String, JsonNode>> iterator) {
        return stream(spliteratorUnknownSize(iterator, ORDERED), false);
    }

    private CryptoPriceNode createCryptoPriceDataElement(String key, JsonNode value) {
        final CryptoPriceNode cryptoPriceNode = new CryptoPriceNode();
        cryptoPriceNode.setName(key);
        cryptoPriceNode.setPrices(createAllPricesForElement(value));
        return cryptoPriceNode;
    }

    private List<CryptoPrice> createAllPricesForElement(JsonNode jsonNode) {
        final List<CryptoPrice> prices = new ArrayList<>();
        final Stream<Map.Entry<String, JsonNode>> pricesStream = createStreamFromIterator(jsonNode.fields());
        pricesStream.forEach(node -> prices.add(createCryptoPriceElement(node.getKey(), node.getValue().doubleValue())));
        return prices;
    }

    private CryptoPrice createCryptoPriceElement(String name, double priceValue) {
        final CryptoPrice price = new CryptoPrice();
        price.setName(name);
        price.setPrice(priceValue);
        return price;
    }
}
