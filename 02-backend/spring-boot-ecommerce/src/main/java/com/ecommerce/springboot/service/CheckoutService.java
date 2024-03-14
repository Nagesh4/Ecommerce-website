package com.ecommerce.springboot.service;

import com.ecommerce.springboot.dto.PaymentInfo;
import com.ecommerce.springboot.dto.Purchase;
import com.ecommerce.springboot.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
