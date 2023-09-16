package com.NoviBackend.WalletWatch.unittests.wallet;

import com.NoviBackend.WalletWatch.request.RequestShareWallet;
import com.NoviBackend.WalletWatch.stock.Stock;
import com.NoviBackend.WalletWatch.user.professional.ProfUserService;
import com.NoviBackend.WalletWatch.user.professional.ProfessionalUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUserService;
import com.NoviBackend.WalletWatch.wallet.Wallet;
import com.NoviBackend.WalletWatch.wallet.WalletRepository;
import com.NoviBackend.WalletWatch.wallet.WalletService;
import com.NoviBackend.WalletWatch.wallet.dto.ProfPersonalWalletDto;
import com.NoviBackend.WalletWatch.wallet.dto.RegularPersonalWalletDto;
import com.NoviBackend.WalletWatch.wallet.dto.WalletDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
class WalletServiceTest {

    @Autowired
    private WalletService walletService;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private RegularUserService regularUserService;

    @MockBean
    private ProfUserService profUserService;

    @Captor
    private ArgumentCaptor<Wallet> walletArgumentCaptor;

    @Test
    void createWallet() {
        // Arrange & act
        Wallet wallet = walletService.createWallet();

        // Assert
        Mockito.verify(walletRepository, Mockito.times(1)).save(any(Wallet.class));
        assertEquals(false, wallet.getShared());
    }

    @Test
    void findWalletById(){
        // Arrange
        Wallet wallet = new Wallet();
        Mockito.when(walletRepository.findById(wallet.getId()))
                .thenReturn(Optional.ofNullable(wallet));
        // Act
        Wallet wallet2 = walletService.findWalletById(wallet.getId());

        // Assert
        assertEquals(wallet, wallet2 );
    }

    @Test
    void findWalletByIdNoWallet(){
        // Arrange
        Wallet wallet = new Wallet();
        Mockito.when(walletRepository.findById(wallet.getId()))
                .thenReturn(Optional.ofNullable(wallet));

        // Act
        Wallet wallet2 = walletService.findWalletById(2l);


        // Assert
        assertEquals(null, wallet2);
    }

    @Test
    void findWalletByIdMinus(){
        // Arrange
        Wallet wallet = new Wallet();
        Mockito.when(walletRepository.findById(wallet.getId()))
                .thenReturn(Optional.ofNullable(wallet));

        // Act
        Wallet wallt2 = walletService.findWalletById(-1l);

        // Assert
        assertEquals(null, wallt2);
    }

    @Test
    void getAllWallets(){
        // Arrange
        List<Wallet> walletList = createWalletList();
        Mockito.when(walletRepository.findAll())
                .thenReturn(walletList);


        // Act
        List<WalletDto> walletDtoList = walletService.getAllWallets();

        // Assert
        assertEquals(3, walletDtoList.size());
    }

    @Test
    void getAllWalletsEmpty(){
        // Arrange
        List<Wallet> emptyWalletList = new ArrayList<>();
        Mockito.when(walletRepository.findAll())
                .thenReturn(emptyWalletList);

        // Act
        List<WalletDto> walletDtoList = walletService.getAllWallets();

        // Assert
        Mockito.verify(walletRepository, Mockito.times(1)).findAll();
        assertEquals(null, walletDtoList);
    }

    @Test
    void getRegularPersonalWalletDto(){
        // Arrange
        Wallet wallet = createFilledWallet(false);

        RegularUser regularUser = new RegularUser("Wouter",
                "whomi",
                "string",
                "testdadta@gmail.com");
        regularUser.setPersonalWallet(wallet);

        Mockito.when(regularUserService.findByUsername(regularUser.getUsername()))
                .thenReturn(regularUser);

        // Act
        RegularPersonalWalletDto regularPersonalWalletDto = walletService.
                getRegularPersonalWalletDto(regularUser.getUsername());

        // Assert
        assertEquals("novi", regularPersonalWalletDto.getStocks().get(0).getStockName());
        assertEquals(1l, regularPersonalWalletDto.getQuantity());
        assertEquals(BigDecimal.valueOf(20), regularPersonalWalletDto.getValue());
    }

    @Test
    void getRegularPersonalWalletDto_UserNull(){
        // Arrange
        String testname = "testname";
        Mockito.when(regularUserService.findByUsername(testname))
                .thenReturn(null);

        // Act
        RegularPersonalWalletDto regularPersonalWalletDto = walletService.getRegularPersonalWalletDto(testname);

        assertEquals(null, regularPersonalWalletDto);
    }

    @Test
    void getProfPersonalWalletDto(){
        // Arrange
        Wallet wallet = createFilledWallet(true);
        ProfessionalUser prof = new ProfessionalUser("Novi",
                "Nova",
                "Novo",
                "NovaNovo@novi.com",
                "NoviCollege",
                "I do work here, i think..");
        prof.setPersonalWallet(wallet);

        Mockito.when(profUserService.findProfByUsername(prof.getUsername()))
                .thenReturn(prof);

        // there is 1 stock in this wallet with a quantity of 20.
        BigDecimal value = wallet.getStocks().get(0).getValue()
                .multiply(
                        BigDecimal.valueOf(
                                wallet.getStocks().get(0).getQuantity()));

        // Act
        ProfPersonalWalletDto profWalletDto = walletService.getProfPersonalWalletDto(prof.getUsername());

        // Assert
        assertEquals(true, profWalletDto.getShared());
        assertEquals(value, profWalletDto.getValue());
        assertEquals(wallet.getStocks().size(), profWalletDto.getQuantity());
    }

    @Test
    void getProfPersonalWalletDto_ProfNull(){
        // Arrange
        Mockito.when(profUserService.findProfByUsername("name"))
                .thenReturn(null);

        // Act
        ProfPersonalWalletDto profPersonalWalletDto = walletService.getProfPersonalWalletDto("name");

        // assert
        assertEquals(null, profPersonalWalletDto);
    }

    @Test
    void shareOrUnshareProfWallet_Share(){
        // Arrange
        Wallet wallet = createFilledWallet(false);
        ProfessionalUser prof = new ProfessionalUser("Novi",
                "Nova",
                "Novo",
                "NovaNovo@novi.com",
                "NoviCollege",
                "I do work here, i think..");
        prof.setPersonalWallet(wallet);

        Mockito.when(profUserService.findProfByUsername(prof.getUsername()))
                .thenReturn(prof);

        // Act
        boolean shared = walletService.shareOrUnshareProfWallet(
                prof.getUsername(),
                createAuthority("ROLE_PROF"),
                createRequestShareWallet(true));

        verify(walletRepository).save(walletArgumentCaptor.capture());
        Wallet savedWallet = walletArgumentCaptor.getValue();

        // Assert
        Mockito.verify(walletRepository, Mockito.times(1)).save(any(Wallet.class));
        assertEquals(wallet, savedWallet);
        assertEquals(true, shared);

    }

    @Test
    void shareOrUnshareProfWallet_UnShare(){
        // Arrange
        Wallet wallet = createFilledWallet(true);
        ProfessionalUser prof = new ProfessionalUser("Novi",
                "Nova",
                "Novo",
                "NovaNovo@novi.com",
                "NoviCollege",
                "I do work here, i think..");
        prof.setPersonalWallet(wallet);

        Mockito.when(profUserService.findProfByUsername(prof.getUsername()))
                .thenReturn(prof);

        // Act
        boolean shared = walletService.shareOrUnshareProfWallet(
                prof.getUsername(),
                createAuthority("ROLE_PROF"),
                createRequestShareWallet(false));

        verify(walletRepository).save(walletArgumentCaptor.capture());
        Wallet savedWallet = walletArgumentCaptor.getValue();

        // Assert
        Mockito.verify(walletRepository, Mockito.times(1)).save(any(Wallet.class));
        assertEquals(wallet, savedWallet);
        assertEquals(false, shared);
    }

    @Test
    void shareOrUnshareProfWallet_Unshare_UnsharedWallet(){
        // Arrange
        Wallet wallet = createFilledWallet(false);
        ProfessionalUser prof = new ProfessionalUser("Novi",
                "Nova",
                "Novo",
                "NovaNovo@novi.com",
                "NoviCollege",
                "I do work here, i think..");
        prof.setPersonalWallet(wallet);

        Mockito.when(profUserService.findProfByUsername(prof.getUsername()))
                .thenReturn(prof);

        // Act
        boolean shared = walletService.shareOrUnshareProfWallet(
                prof.getUsername(),
                createAuthority("ROLE_PROF"),
                createRequestShareWallet(false));

        verify(walletRepository).save(walletArgumentCaptor.capture());
        Wallet savedWallet = walletArgumentCaptor.getValue();

        // Assert
        Mockito.verify(walletRepository, Mockito.times(1)).save(any(Wallet.class));
        assertEquals(wallet, savedWallet);
        assertEquals(false, shared);
    }

    @Test
    void shareOrUnshareProfWallet_WrongAuthority(){
        // Arrange
        Wallet wallet = createFilledWallet(false);
        ProfessionalUser prof = new ProfessionalUser("Novi",
                "Nova",
                "Novo",
                "NovaNovo@novi.com",
                "NoviCollege",
                "I do work here, i think..");
        prof.setPersonalWallet(wallet);

        Mockito.when(profUserService.findProfByUsername(prof.getUsername()))
                .thenReturn(prof);

        // Act
        boolean shared = walletService.shareOrUnshareProfWallet(
                prof.getUsername(),
                createAuthority("ROLE_USER"),
                createRequestShareWallet(true));

        // Assert
        Mockito.verify(walletRepository, Mockito.times(0)).save(any(Wallet.class));
        assertEquals(false, shared);
    }



    private List<Wallet> createWalletList(){
        Wallet wallet1 = new Wallet();
        Wallet wallet2 = new Wallet();
        Wallet wallet3 = new Wallet();


        return new ArrayList<>(Arrays.asList(wallet1, wallet2, wallet3));
    }

    private Wallet createFilledWallet(boolean isShared){
        Wallet wallet = new Wallet();
        wallet.setShared(isShared);

        Stock stock = new Stock("novi",
                BigDecimal.valueOf(1),
                20l,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(20),
                "Good Stock",
                BigDecimal.valueOf(3),
                "buy");

        wallet.addStock(stock);

        return wallet;
    }

    private Collection<? extends GrantedAuthority> createAuthority(String role){
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        return Collections.singleton(authority);
    }

    private RequestShareWallet createRequestShareWallet(boolean share){
        RequestShareWallet requestShareWallet = new RequestShareWallet();
        requestShareWallet.setShareWallet(share);

        return requestShareWallet;
    }
}