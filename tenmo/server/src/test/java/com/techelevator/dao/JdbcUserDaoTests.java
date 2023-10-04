package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcUserDaoTests extends BaseDaoTests{

    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }

    //findByUserName returns User
    @Test
    public void findByUsername_returns_correct_user(){
        Assert.assertEquals(1007, sut.findByUsername("user").getId());
        Assert.assertEquals(1004, sut.findByUsername("Mr_Peanutbutter").getId());
    }

    //findIdByUsername returns (correct) int
    @Test
    public void findIdByUsername_returns_correct_ID(){
        Assert.assertEquals(1007, sut.findIdByUsername("user"));
        Assert.assertEquals(1004, sut.findIdByUsername("Mr_Peanutbutter"));

    }

    //list all usernames returns a list of strings
    @Test
    public void listAllUserNames_returns_correct_list(){
        Assert.assertEquals(7,sut.listAllUserNames().size());
        Assert.assertTrue(sut.listAllUserNames().contains("bob"));
        Assert.assertTrue(sut.listAllUserNames().contains("user"));
        Assert.assertTrue(sut.listAllUserNames().contains("chavez_todd"));

    }
}
