package API;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Token;

import java.util.HashMap;
import java.util.Map;

public class StripeAPI {
    public static Token createToken(Long cardNumber, int exp_month, int exp_year, int cvc, String cardHolder){
        Stripe.apiKey = "pk_test_JKSO32WMKKnFfNYDvE13cGCB";
        Token token=new Token();
        Map<String, Object> tokenParams = new HashMap<>();
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put("number", cardNumber);
        cardParams.put("exp_month", exp_month);
        cardParams.put("exp_year", exp_year);
        cardParams.put("cvc", cvc);
        cardParams.put("name", cardHolder);
        tokenParams.put("card", cardParams);

        try {
            token= Token.create(tokenParams);
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
        return token;
    }
}
