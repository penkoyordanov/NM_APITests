package tests;

import API.StripeAPI;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PaymentTests {
//    private static Token token;
    @BeforeClass
    public static void setUp(){
        Stripe.apiKey = "sk_test_BQokikJOvBiI2HlWgH4olfQ2";
    }

    @Test
    public void test01_createCharge(){
        Token token= StripeAPI.createToken(4242424242424242L,2,2018,314,"Penko");

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", 2000);
        chargeParams.put("currency", "usd");
        chargeParams.put("description", "Charge for alexander.martinez@example.com");
        chargeParams.put("source", token.getId());
// ^ obtained with Stripe.js
        try {
            Charge charge=Charge.create(chargeParams);
            Assert.assertThat(charge.getStatus(),equalTo("succeeded"));
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test02_ChargeSucceedWithRiskLevel(){
        Token token=StripeAPI.createToken(4000056655665556L,2,2018,314,"Penko");

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", 2000);
        chargeParams.put("currency", "usd");
        chargeParams.put("description", "Charge for alexander.martinez@example.com");
        chargeParams.put("source", token.getId());
// ^ obtained with Stripe.js
        try {
            Charge charge=Charge.create(chargeParams);
            Assert.assertThat(charge.getStatus(),equalTo("succeeded"));
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void test03_ChargeDeclined(){
        Token token=StripeAPI.createToken(4000000000000002L,2,2018,314,"Penko");

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", 2000);
        chargeParams.put("currency", "usd");
        chargeParams.put("description", "Charge for alexander.martinez@example.com");
        chargeParams.put("source", token.getId());
// ^ obtained with Stripe.js
        try {
            Charge.create(chargeParams);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
            Assert.assertThat(e.getLocalizedMessage(),equalTo("Your card was declined."));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test04_ChargeDeclinedDueToIncorrectCVC(){
        Token token=StripeAPI.createToken(4000000000000127L,2,2018,314,"Penko");

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", 2000);
        chargeParams.put("currency", "usd");
        chargeParams.put("description", "Charge for alexander.martinez@example.com");
        chargeParams.put("source", token.getId());
// ^ obtained with Stripe.js
        try {
            Charge.create(chargeParams);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
            Assert.assertThat(e.getLocalizedMessage(),equalTo("Your card's security code is incorrect."));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test05_ChargeDeclinedInvalidCardNumber(){
        Token token=StripeAPI.createToken(4000000000450127L,2,2018,314,"Penko");

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", 2000);
        chargeParams.put("currency", "usd");
        chargeParams.put("description", "Charge for alexander.martinez@example.com");
        chargeParams.put("source", token.getId());
// ^ obtained with Stripe.js
        try {
            Charge.create(chargeParams);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
            Assert.assertThat(e.getLocalizedMessage(),equalTo("Your card number is incorrect."));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test06_ChargeDeclinedDueToExpiredCard(){
        Token token=StripeAPI.createToken(4000000000000069L,2,2018,314,"Penko");

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", 2000);
        chargeParams.put("currency", "usd");
        chargeParams.put("description", "Charge for alexander.martinez@example.com");
        chargeParams.put("source", token.getId());
// ^ obtained with Stripe.js
        try {
            Charge.create(chargeParams);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
            Assert.assertThat(e.getLocalizedMessage(),equalTo("Your card has expired."));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test07_ChargeDeclinedDueToProcessingError(){
        Token token=StripeAPI.createToken(4000000000000119L,2,2018,314,"Penko");

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", 2000);
        chargeParams.put("currency", "usd");
        chargeParams.put("description", "Charge for alexander.martinez@example.com");
        chargeParams.put("source", token.getId());
// ^ obtained with Stripe.js
        try {
            Charge.create(chargeParams);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
            Assert.assertThat(e.getLocalizedMessage(),equalTo("An error occurred while processing your card. Try again in a little bit."));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test08_ChargeDeclinedDueToFraudulentReason(){
        Token token=StripeAPI.createToken(4100000000000019L,2,2018,314,"Penko");

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", 2000);
        chargeParams.put("currency", "usd");
        chargeParams.put("description", "Charge for alexander.martinez@example.com");
        chargeParams.put("source", token.getId());
// ^ obtained with Stripe.js
        try {
            Charge.create(chargeParams);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
            Assert.assertThat(e.getLocalizedMessage(),equalTo("Your card was declined."));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test08_ChargeDeclinedDueToinsufficientfunds(){
        Token token=StripeAPI.createToken(000222222227L,2,2018,314,"Penko");

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", 2000);
        chargeParams.put("currency", "usd");
        chargeParams.put("description", "Charge for alexander.martinez@example.com");
        chargeParams.put("source", token.getId());
// ^ obtained with Stripe.js
        try {
            Charge.create(chargeParams);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
            Assert.assertThat(e.getLocalizedMessage(),equalTo("Your card was declined."));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }


}
