package hello.productorderservice.payment;

import org.springframework.stereotype.Component;

@Component
public class ConsolePaymentGateway implements PaymentGateway {
    @Override
    public void execute(int price, String cardNumber) {
        System.out.println("price = " + price);
        System.out.println("cardNumber = " + cardNumber);
        System.out.println("결제 완료");
    }
}
