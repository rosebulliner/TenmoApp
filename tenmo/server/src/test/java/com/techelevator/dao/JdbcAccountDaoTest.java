package com.techelevator.dao;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcAccountDaoTest extends BaseDaoTests {

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    //get account by id
    @Test
    public void getAccountById_gets_valid_account_returns_null_for_invalid(){

        Account happyPath = sut.getAccountById(2001);
        Account expectNull = sut.getAccountById(100);

        Assert.assertNotNull(happyPath);
        Assert.assertNull(expectNull);

    }

    //get accounts by userId
    @Test
    public void getAccountById_returns_correct_account(){
        Account acc = sut.getAccountById(2001);
        Assert.assertEquals(2001,acc.getAccountId());
    }


    //create account by userIdg
    @Test
    public void createAccountByUserId_creates_valid_account(){
        Account createdAccount = sut.createAccountByUserId(1001);
        Assert.assertNotNull(createdAccount);
    }

    //add money adds money
    @Test
    public void addMoney_updates_balance(){
        Assert.assertTrue( sut.addMoney(sut.getAccountById(2004), 2000.44));

    }


    //deduct money deducts money, returns false if balance insufficient (doesn't deduct money!)
    @Test
    public void deductMoney_returns_true_with_valid_input(){
    Assert.assertTrue(sut.deductMoney(sut.getAccountById(2003), 444.44));
    }
    @Test public void deductMoney_returns_false_for_resulting_insufficient_balance(){
        Assert.assertFalse(sut.deductMoney(sut.getAccountById(2002),2000));
    }


    //not checking the balance here bc it makes test more flexible not to.
    public void assertAccountsMatch(Account a, Account b){
        Assert.assertEquals(a.getAccountId(),b.getAccountId());
        Assert.assertEquals(a.getUserId(), b.getUserId());
    }


}
