package com.NoviBackend.WalletWatch.stock;

import com.NoviBackend.WalletWatch.stock.mapper.StockMapper;
import com.NoviBackend.WalletWatch.stock.stockdto.StockDto;
import com.NoviBackend.WalletWatch.user.AbstractUsers;
import com.NoviBackend.WalletWatch.user.professional.ProfUserService;
import com.NoviBackend.WalletWatch.user.professional.ProfessionalUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUserService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

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
        AbstractUsers user = userOrProf(authorities, username);

        if(user == null){
            return null;
        }

        return user.getPersonalWallet().getStocks();
    }

    public Long addStock(String username, Collection<? extends GrantedAuthority> authorities, StockDto stockDto) {
        // map stockDto to stock
        Stock stock = stockMapper.convertStockDtoToStock(stockDto);
        AbstractUsers user = userOrProf(authorities, username);

        // set stock wallet
        stock.setWallet(user.getPersonalWallet());
        stockRepository.save(stock);

        // add stock to personal wallet
        user.getPersonalWallet().addStock(stock);

        return stock.getId();
    }

    public AbstractUsers userOrProf(Collection<? extends GrantedAuthority> authorities, String username){

        if(authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_USER"))){
            return regularUserService.findByUsername(username);
        } else if (authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_PROF"))) {
            return profUserService.findByUsername(username);
        }
        return null;
    }
}

