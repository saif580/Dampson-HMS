package com.hms.appointments.service;

import com.hms.appointments.dto.PaymentRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${payment.success.url}")
    private String successUrl;

    @Value("${payment.cancel.url}")
    private String cancelUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
        logger.debug("Stripe API Key initialized.");
    }

    public Session createCheckoutSession(PaymentRequest paymentRequest) throws StripeException {
        logger.debug("Creating checkout session for payment request: {}", paymentRequest);

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(cancelUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("inr")
                                                .setUnitAmount(paymentRequest.getAmount())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(paymentRequest.getDescription())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                );

        // Add metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("appointmentId", paymentRequest.getAppointmentId());
        String appointmentId = paymentRequest.getAppointmentId();
        logger.debug("Received appointmentId from PaymentRequest: {}", appointmentId);
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            paramsBuilder.putMetadata(entry.getKey(), entry.getValue());
        }

        Session session = Session.create(paramsBuilder.build());
        logger.debug("Created Stripe session: {}", session.getId());
        return session;
    }

    public String getCheckoutUrl(Session session) {
        logger.debug("Getting checkout URL for session: {}", session.getId());
        return session.getUrl();
    }
}
