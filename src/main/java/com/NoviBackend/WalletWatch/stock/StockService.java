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
    public Long addStock(String username, Collection<? extends GrantedAuthority> authorities, StockDto stockDto) {
        // map stockDto to stock
        Stock stock = stockMapper.convertStockDtoToStock(stockDto);
        AbstractUsers user = smallUserProfFactory(authorities, username);

        if(user == null){
            return null;
        }

        return addStockToWallet(stock, user);
    }

    public Long deleteStock(Long id, Authentication auth) {
        AbstractUsers user = smallUserProfFactory(auth.getAuthorities(), auth.getName());
        Stock stock = getStockFromWalletById(id, user);

        if(stock == null || deleteStockFromWallet(stock, user) == null){
            return null;
        }

        stockRepository.delete(stock);
        return stock.getId();
    }

    public Stock getStock(String username, Collection<? extends GrantedAuthority> authorities, Long stockId) {
        AbstractUsers user = smallUserProfFactory(authorities, username);

        if(user == null){
            return null;
        }

        return getStockFromWalletById(stockId, user);
    }

    public List<Stock> getYourStocks(String username, Collection<? extends GrantedAuthority> authorities) {
        // get regular or prof
        AbstractUsers user = smallUserProfFactory(authorities, username);

        if(user == null){
            return null;
        }

        return user.getPersonalWallet().getStocks();
    }

    public Long updateStock(Long stockId, StockDto stockDto, Authentication auth) {
        AbstractUsers user = smallUserProfFactory(auth.getAuthorities(), auth.getName());
        Stock oldStock = getStockFromWalletById(stockId, user);

        if(oldStock == null){
            return null;
        }

        Stock newStock = stockMapper.convertStockDtoToStock(stockDto);
        newStock.setId(oldStock.getId());
        newStock.setWallet(oldStock.getWallet());

        return replaceStock(user, oldStock, newStock);
    }

    // functions
    private Long addStockToWallet(Stock stock, AbstractUsers user){
        // set stock wallet
        stock.setWallet(user.getPersonalWallet());
        stockRepository.save(stock);

        // add stock to personal wallet
        user.getPersonalWallet().addStock(stock);

        return stock.getId();
    }

    private AbstractUsers deleteStockFromWallet(Stock stock, AbstractUsers user){
        boolean stockRemoved = user.getPersonalWallet().deleteStock(stock);

        if(!stockRemoved){
            return null;
        }

        return smallUserStockSaveFactory(user);
    }

    private Stock getStockFromWalletById(Long stockId, AbstractUsers user){
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
        smallUserStockSaveFactory(user);
        stockRepository.save(newStock);
        return newStock.getId();
    }

    private AbstractUsers smallUserProfFactory(Collection<? extends GrantedAuthority> authorities, String username){

        if(authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_USER"))){
            return regularUserService.findByUsername(username);
        } else if (authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_PROF"))) {
            return profUserService.findProfByUsername(username);
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
}

