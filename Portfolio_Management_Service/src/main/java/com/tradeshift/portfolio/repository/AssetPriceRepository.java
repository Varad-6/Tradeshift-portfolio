package com.tradeshift.portfolio.repository;

import com.tradeshift.portfolio.model.AssetPrice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetPriceRepository extends MongoRepository<AssetPrice, String> {

    // Find the latest price for a given asset symbol
    AssetPrice findByAssetSymbol(String assetSymbol);

    // Find all asset prices of a specific type (STOCK, CRYPTO, MUTUAL_FUND)
    List<AssetPrice> findByAssetType(String assetType);

    // Find multiple asset prices by a list of symbols
    List<AssetPrice> findByAssetSymbolIn(List<String> assetSymbols);
}
