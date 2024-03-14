package com.ecommerce.springboot.service;

import com.ecommerce.springboot.dao.CustomerRepository;
import com.ecommerce.springboot.dto.PaymentInfo;
import com.ecommerce.springboot.dto.Purchase;
import com.ecommerce.springboot.dto.PurchaseResponse;
import com.ecommerce.springboot.entity.Customer;
import com.ecommerce.springboot.entity.Order;
import com.ecommerce.springboot.entity.OrderItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CheckoutServiceImpl implements CheckoutService{

    private CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository, @Value("${stripe.key.secret}") String secretKey){

        this.customerRepository = customerRepository;

        //initialize Strips API with secret key
        Stripe.apiKey = secretKey;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        //retrieve the order info from dto
        Order order = purchase.getOrder();

        //generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        //populate order with orderItems
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        //populate order with billingAddress and shippingAddress
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        //populate customer with order
        Customer customer = purchase.getCustomer();

        //check if this is an existing customer
        String theEmail = customer.getEmail();

        Customer customerFromDB = customerRepository.findByEmail(theEmail);

        if(customerFromDB != null){
            //we found them.. let's assign them accordingly
            customer = customerFromDB;
        }

        customer.add(order);

        //save to the database
        customerRepository.save(customer);

        //return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        List<String> paymentMethodType = new ArrayList<>();
        paymentMethodType.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodType);
        params.put("description","Ecommerce purchase");
        params.put("receipt_email", paymentInfo.getReceiptEmail());
        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {

        //generate a random UUID number (UUDI version-4)
        return UUID.randomUUID().toString();
    }
}
