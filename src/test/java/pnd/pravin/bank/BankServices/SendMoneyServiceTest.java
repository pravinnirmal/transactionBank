package pnd.pravin.bank.BankServices;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pnd.pravin.bank.BankConstants.TestConstants;
import pnd.pravin.bank.BankEntitites.SendMoneyEntity;
import pnd.pravin.bank.BankRepositories.PersonalAccountRepository;
import pnd.pravin.bank.BankRepositories.TransactionStatementRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
public class SendMoneyServiceTest {

    @Autowired
    private PersonalAccountRepository personalAccountRepository;

    @Autowired
    private SendMoneyService sendMoneyService;

    @Autowired
    private TransactionStatementRepository transactionStatementRepository;

    @BeforeEach
    public void setup(){
        sendMoneyService = new SendMoneyService(personalAccountRepository, transactionStatementRepository);
    }

    @Test
    public void sendMoneyToUserTest(){

        double existingMoney = personalAccountRepository.findById(TestConstants.TEST_USER_ONE).get().getMoney();
        double existingMoneyForUser2 = personalAccountRepository.findById(TestConstants.TEST_USER_TWO).get().getMoney();

        sendMoneyService.sendMoneyToUser(new SendMoneyEntity(TestConstants.TEST_USER_ONE, TestConstants.MONEY_TO_SEND),
                TestConstants.TEST_USER_TWO);
        double expectedMoney = existingMoney + TestConstants.MONEY_TO_SEND;
        String expectedResult = Double.toString(expectedMoney);
        log.info("Validate the expected money in user 1");
        assertEquals(expectedResult, personalAccountRepository.findById(TestConstants.TEST_USER_ONE).get().getMoney().toString());

        log.info("Validate the money deducted from User 2");
        double expectedDeductedMoneyForUser2 = existingMoneyForUser2 - TestConstants.MONEY_TO_SEND;
        assertEquals(expectedDeductedMoneyForUser2, personalAccountRepository.findById(TestConstants.TEST_USER_TWO).get().getMoney());

    }
}
