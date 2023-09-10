package com.NoviBackend.WalletWatch.stock;

import com.NoviBackend.WalletWatch.stock.mapper.StockMapper;
import com.NoviBackend.WalletWatch.stock.stockdto.StockDto;
import com.NoviBackend.WalletWatch.user.AbstractUsers;
import com.NoviBackend.WalletWatch.user.professional.ProfUserRepository;
import com.NoviBackend.WalletWatch.user.professional.ProfUserService;
import com.NoviBackend.WalletWatch.user.professional.ProfessionalUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUserRepository;
import com.NoviBackend.WalletWatch.user.regular.RegularUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final RegularUserRepository regularUserRepository;
    private final ProfUserRepository profUserRepository;
    private final RegularUserService regularUserService;
    private final ProfUserService profUserService;
    private final StockMapper stockMapper;

    public StockService(StockRepository stockRepository,
                        RegularUserRepository regularUserRepository,
                        ProfUserRepository profUserRepository,
                        RegularUserService regularUserService,
                        ProfUserService profUserService,
                        StockMapper stockMapper){
        this.stockRepository = stockRepository;
        this.regularUserRepository = regularUserRepository;
        this.profUserRepository = profUserRepository;
        this.regularUserService = regularUserService;
        this.profUserService = profUserService;
        this.stockMapper = stockMapper;
    }

    // methods
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

        return getStockFromWallet(stockId, user);
    }

    public Long addStock(String username, Collection<? extends GrantedAuthority> authorities, StockDto stockDto) {
        // map stockDto to stock
        Stock stock = stockMapper.convertStockDtoToStock(stockDto);
        AbstractUsers user = smallUserProfFactory(authorities, username);

        if(user == null){
            return null;
        }

        return addStockToWallet(stock, user);
    }

    public Long updateStock(Long stockId, StockDto stockDto, Authentication auth) {
        AbstractUsers user = smallUserProfFactory(auth.getAuthorities(), auth.getName());
        Stock oldStock = getStockFromWallet(stockId, user);

        if(oldStock == null){
            return null;
        }

        Stock newStock = stockMapper.convertStockDtoToStock(stockDto);
        newStock.setId(oldStock.getId());
        newStock.setWallet(oldStock.getWallet());
        return replaceStock(user, oldStock, newStock);
    }

    // functions
    private AbstractUsers smallUserProfFactory(Collection<? extends GrantedAuthority> authorities, String username){

        if(authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_USER"))){
            return regularUserService.findByUsername(username);
        } else if (authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_PROF"))) {
            return profUserService.findByUsername(username);
        }
        return null;
    }

    private AbstractUsers smallUserStockSaveFactory(AbstractUsers user){

        if(regularUserRepository.existsRegularUserByUsername(user.getUsername())){
            regularUserRepository.save((RegularUser) user);
        } else if (profUserRepository.existsProfessionalUserByUsername(user.getUsername())) {
            profUserRepository.save((ProfessionalUser) user);
        }

        return user;
    }

    private Long addStockToWallet(Stock stock, AbstractUsers user){
        // set stock wallet
        stock.setWallet(user.getPersonalWallet());
        stockRepository.save(stock);

        // add stock to personal wallet
        user.getPersonalWallet().addStock(stock);

        return stock.getId();
    }

    private Stock getStockFromWallet(Long stockId, AbstractUsers user){
        Predicate<? super Stock> predicate =
                stock -> stock.getId().equals(stockId);
        Optional<Stock> optionalStock = user.getPersonalWallet()
                .getStocks().stream()
                .filter(predicate)
                .findFirst();

        return optionalStock.orElse(null);
    }

    private Long replaceStock(AbstractUsers user, Stock oldStock, Stock newStock){
        user.getPersonalWallet().setStock(oldStock, newStock);
        user = smallUserStockSaveFactory(user);
        stockRepository.save(newStock);
        return newStock.getId();
    }
}

