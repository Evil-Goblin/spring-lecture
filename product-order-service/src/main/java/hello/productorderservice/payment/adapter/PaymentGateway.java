package hello.productorderservice.payment.adapter;

interface PaymentGateway {
    void execute(int price, String cardNumber);
}
