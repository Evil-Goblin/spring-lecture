package hello.productorderservice.payment;

interface PaymentGateway {
    void execute(int price, String cardNumber);
}
