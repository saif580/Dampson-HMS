package com.hms.appointments.service;

import com.hms.appointments.dto.PaymentRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Captor
    private ArgumentCaptor<SessionCreateParams> paramsCaptor;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${payment.success.url}")
    private String successUrl;

    @Value("${payment.cancel.url}")
    private String cancelUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCheckoutSession_Success() throws StripeException {
        // Given
        PaymentRequest paymentRequest = new PaymentRequest(1000, "Appointment Fee");
        paymentRequest.setAppointmentId("12345");

        Session mockSession = mock(Session.class);
        when(mockSession.getId()).thenReturn("sess_123");
        try (MockedStatic<Session> mockedSession = mockStatic(Session.class)) {
            mockedSession.when(() -> Session.create(any(SessionCreateParams.class))).thenReturn(mockSession);

            // When
            Session createdSession = paymentService.createCheckoutSession(paymentRequest);

            // Then
            assertNotNull(createdSession);
            assertEquals("sess_123", createdSession.getId());

            mockedSession.verify(() -> Session.create(paramsCaptor.capture()));
            SessionCreateParams params = paramsCaptor.getValue();

            assertEquals("inr", params.getLineItems().get(0).getPriceData().getCurrency());
            assertEquals(Long.valueOf(1000), params.getLineItems().get(0).getPriceData().getUnitAmount());
            assertEquals("Appointment Fee", params.getLineItems().get(0).getPriceData().getProductData().getName());
            assertEquals("12345", params.getMetadata().get("appointmentId"));
        }
    }

    @Test
    void testGetCheckoutUrl() {
        // Given
        Session mockSession = mock(Session.class);
        when(mockSession.getId()).thenReturn("sess_123");
        when(mockSession.getUrl()).thenReturn("https://checkout.stripe.com/pay/sess_123");

        // When
        String checkoutUrl = paymentService.getCheckoutUrl(mockSession);

        // Then
        assertNotNull(checkoutUrl);
        assertEquals("https://checkout.stripe.com/pay/sess_123", checkoutUrl);
    }

    @Test
    void testInit() {
        // When
        paymentService.init();

        // Then
        assertEquals(stripeApiKey, Stripe.apiKey);
    }
}
