package pl.mentelm.autoinvoice.fakturownia;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mentelm.autoinvoice.domain.IssueInvoice;
import pl.mentelm.autoinvoice.model.Invoice;
import pl.mentelm.autoinvoice.model.InvoiceId;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

class FakturowniaIssueInvoiceIntegrationTest extends FakturowniaIntegrationTest {

    @Autowired
    private FakturowniaIssueInvoice issueInvoice;

    @Test
    void shouldIssueInvoiceSuccessfully() {
        // Given
        wm.register(post(urlPathEqualTo("/invoices.json"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("issue_invoice_success_response.json")));

        Invoice invoice = Invoice.builder()
                .invoiceId(new InvoiceId("FV/2026/01"))
                .build();

        // When & Then
        issueInvoice.issue(invoice)
                .as(StepVerifier::create)
                .assertNext(result -> {
                    assertThat(result).isInstanceOf(IssueInvoice.IssueInvoiceResult.Success.class);
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnFailureWhenApiReturnsError() {
        // Given
        wm.register(post(urlPathEqualTo("/invoices.json"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("issue_invoice_failure_response.json")));

        Invoice invoice = Invoice.builder().build();

        // When & Then
        issueInvoice.issue(invoice)
                .as(StepVerifier::create)
                .assertNext(result -> {
                    assertThat(result).isInstanceOf(IssueInvoice.IssueInvoiceResult.Failure.class);
                    var failure = (IssueInvoice.IssueInvoiceResult.Failure) result;
                    assertThat(failure.reason()).contains("400");
                })
                .verifyComplete();
    }
}
