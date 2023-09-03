package com.NoviBackend.WalletWatch.stock.mapper;

import com.NoviBackend.WalletWatch.stock.Stock;
import com.NoviBackend.WalletWatch.stock.stockdto.StockDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {
    private final ModelMapper modelMapper;

    public StockMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public Stock convertStockDtoToStock(StockDto stockDto){
        return modelMapper.map(stockDto, Stock.class);
    }
}
