package com.NoviBackend.WalletWatch.stock;

import com.NoviBackend.WalletWatch.stock.mapper.StockMapper;
import com.NoviBackend.WalletWatch.stock.stockdto.StockDto;
import com.NoviBackend.WalletWatch.user.AbstractUsers;
import com.NoviBackend.WalletWatch.user.professional.ProfUserService;
import com.NoviBackend.WalletWatch.user.regular.RegularUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final RegularUserService regularUserService;
    private final ProfUserService profUserService;
    private final StockMapper stockMapper;

    public StockService(StockRepository stockRepository,
                        RegularUserService regularUserService,
                        ProfUserService profUserService,
                        StockMapper stockMapper){
        this.stockRepository = stockRepository;
        this.regularUserService = regularUserService;
        this.profUserService = profUserService;
        this.stockMapper = stockMapper;
    }

    public List<Stock> getYourStocks(String username, Collection<? extends GrantedAuthority> authorities) {
        // get regular or prof
        AbstractUsers user = smallUserProfFactory(authorities, username);

        if(user == null){
            return null;
        }

        return user.getPersonalWallet().getStocks();
    }

    public Stock getStock(String username, Collection<? extends GrantedAuthority> authorities, Long stockId) {
        AbstractUsers user = smallUserProfFactory(authorities, username);

        if(user == null){
            return null;
        }

        Predicate<? super Stock> predicate =
                stock -> stock.getId().equals(stockId);
        Optional<Stock> optionalStock = user.getPersonalWallet()
                .getStocks().stream()
                .filter(predicate)
                .findFirst();

        if(optionalStock.isEmpty()){
            return null;
        }

        return optionalStock.get();
    }

    public Long addStock(String username, Collection<? extends GrantedAuthority> authorities, StockDto stockDto) {
        // map stockDto to stock
        Stock stock = stockMapper.convertStockDtoToStock(stockDto);
        AbstractUsers user = smallUserProfFactory(authorities, username);

        if(user == null){
            return null;
        }

        // set stock wallet
        stock.setWallet(user.getPersonalWallet());
        stockRepository.save(stock);

        // add stock to personal wallet
        user.getPersonalWallet().addStock(stock);

        return stock.getId();
    }

    public AbstractUsers smallUserProfFactory(Collection<? extends GrantedAuthority> authorities, String username){

        if(authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_USER"))){
            return regularUserService.findByUsername(username);
        } else if (authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_PROF"))) {
            return profUserService.findByUsername(username);
        }
        return null;
    }
}

