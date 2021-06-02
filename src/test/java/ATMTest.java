
import com.mycompany.atm.Main;
import com.mycompany.atm.custom.exception.InsufficientBalanceException;
import com.mycompany.atm.custom.exception.InvalidAccountException;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.domain.Transaction;
import com.mycompany.atm.domain.TransactionFundTransfer;
import com.mycompany.atm.enums.TransactionType;
import com.mycompany.atm.repository.AccountRepository;
import com.mycompany.atm.service.TransactionService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Achmad_ST761
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = 
  SpringBootTest.WebEnvironment.MOCK,
  classes = Main.class)
@AutoConfigureMockMvc
@TestPropertySource(
  locations = "classpath:application-test.properties")
@Transactional
public class ATMTest {
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    TransactionService transactionService;
    
    Account newAccount, newAccount2;
    
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        newAccount = new Account();
        newAccount.setAccountName("test");
        newAccount.setBalance(new BigDecimal(660));
        newAccount.setPin("123456");
        newAccount.setAccountNumber("112233");
        newAccount.setTransactionHistory(new ArrayList<>());
        newAccount2 = new Account();
        newAccount2.setAccountName("test2");
        newAccount2.setBalance(new BigDecimal(10));
        newAccount2.setPin("123456");
        newAccount2.setAccountNumber("112234");
        newAccount2.setTransactionHistory(new ArrayList<>());
        accountRepository.saveAccount(newAccount);
        accountRepository.saveAccount(newAccount2);
    }
    
    @Test
    public void testIsAccountAvailable() {
        Account retrievedAccount = accountRepository.findAccount(newAccount.getAccountNumber());
        assertEquals(retrievedAccount.getAccountNumber(), newAccount.getAccountNumber());
    }
    
    @Test
    public void testIsAccountAvailableFromListAccount() { 
        List<Account> retrievedAccounts = accountRepository.findAllAccount();
        assertEquals(2 , retrievedAccounts.size());
        assertEquals(retrievedAccounts.get(0).getAccountNumber(), newAccount.getAccountNumber());
    }
    
    @Test
    public void tesWithdraw() {
        int beforeAmount = newAccount.getBalance().intValue();
        transactionService.withdraw(newAccount, 10);
        assertEquals(beforeAmount-10, newAccount.getBalance().intValue());
    }
    
    @Test
    public void testWithdrawErrorBalance() {
        exceptionRule.expect(InsufficientBalanceException.class);
        transactionService.withdraw(newAccount, 690);
    }
    
    @Test
    public void testFundTransfer() {
        int beforeAmountAcc1 = newAccount.getBalance().intValue();
        int beforeAmountAcc2 = newAccount2.getBalance().intValue();
        TransactionFundTransfer fundTransfer = new TransactionFundTransfer(LocalDateTime.now(), 10, "112234", transactionService.getRandomRefNum());
        transactionService.fundTransfer(newAccount, fundTransfer);
        newAccount2 = transactionService.refreshAccount(newAccount2);
        assertEquals(beforeAmountAcc1-10, newAccount.getBalance().intValue());
        assertEquals(beforeAmountAcc2+10, newAccount2.getBalance().intValue());
    }
    
    @Test
    public void testFundTransferErrorBalance() {
        exceptionRule.expect(InsufficientBalanceException.class);
        TransactionFundTransfer fundTransfer = new TransactionFundTransfer(LocalDateTime.now(), 690, "112234", transactionService.getRandomRefNum());
        transactionService.fundTransfer(newAccount, fundTransfer);
    }
   
    @Test
    public void testFundTransferErrorNotFound() {
        exceptionRule.expect(InvalidAccountException.class);
        transactionService.checkAccountAvailability("112255");
    }
    
    @Test
    public void testTransactionHistoryWithSingleTransaction() {
        transactionService.withdraw(newAccount, 10);
        List<Transaction> listHistory = transactionService.getLast10Transaction(newAccount, LocalDate.now());
        assertEquals(listHistory.get(0).getAmount(), new Integer(-10));
        assertEquals(listHistory.get(0).getTransactionType(), TransactionType.WITHDRAW.toString());
    }
    
    @Test
    public void testTransactionHistoryWithTwoTransaction() {
        transactionService.withdraw(newAccount, 10);
        transactionService.withdraw(newAccount, 20);
        List<Transaction> listHistory = transactionService.getLast10Transaction(newAccount, LocalDate.now());      
        assertEquals(listHistory.get(0).getAmount(), new Integer(-20));
        assertEquals(listHistory.get(0).getTransactionType(), TransactionType.WITHDRAW.toString());
        assertEquals(listHistory.get(1).getAmount(), new Integer(-10));
        assertEquals(listHistory.get(1).getTransactionType(), TransactionType.WITHDRAW.toString());
    }
    
    @Test
    public void testTransactionHistoryMoreThan10Transaction() {  
        transactionService.withdraw(newAccount, 10);
        transactionService.withdraw(newAccount, 20);
        transactionService.withdraw(newAccount, 30);
        transactionService.withdraw(newAccount, 40);
        transactionService.withdraw(newAccount, 50);
        transactionService.withdraw(newAccount, 60);
        transactionService.withdraw(newAccount, 70);
        transactionService.withdraw(newAccount, 80);
        transactionService.withdraw(newAccount, 90);
        transactionService.withdraw(newAccount, 100);
        transactionService.withdraw(newAccount, 110);
        List<Transaction> listHistory = transactionService.getLast10Transaction(newAccount, LocalDate.now());
        assertEquals(listHistory.get(0).getAmount(), new Integer(-110));
        assertEquals(listHistory.get(0).getTransactionType(), TransactionType.WITHDRAW.toString());
        assertEquals(listHistory.get(1).getAmount(), new Integer(-100));
        assertEquals(listHistory.get(1).getTransactionType(), TransactionType.WITHDRAW.toString());
        assertEquals(listHistory.get(2).getAmount(), new Integer(-90));
        assertEquals(listHistory.get(2).getTransactionType(), TransactionType.WITHDRAW.toString());
        assertEquals(listHistory.get(3).getAmount(), new Integer(-80));
        assertEquals(listHistory.get(3).getTransactionType(), TransactionType.WITHDRAW.toString());
        assertEquals(listHistory.get(4).getAmount(), new Integer(-70));
        assertEquals(listHistory.get(4).getTransactionType(), TransactionType.WITHDRAW.toString());
        assertEquals(listHistory.get(5).getAmount(), new Integer(-60));
        assertEquals(listHistory.get(5).getTransactionType(), TransactionType.WITHDRAW.toString());
        assertEquals(listHistory.get(6).getAmount(), new Integer(-50));
        assertEquals(listHistory.get(6).getTransactionType(), TransactionType.WITHDRAW.toString());
        assertEquals(listHistory.get(7).getAmount(), new Integer(-40));
        assertEquals(listHistory.get(7).getTransactionType(), TransactionType.WITHDRAW.toString());
        assertEquals(listHistory.get(8).getAmount(), new Integer(-30));
        assertEquals(listHistory.get(8).getTransactionType(), TransactionType.WITHDRAW.toString());
        assertEquals(listHistory.get(9).getAmount(), new Integer(-20));
        assertEquals(listHistory.get(9).getTransactionType(), TransactionType.WITHDRAW.toString());
    }
    
    
    
}
