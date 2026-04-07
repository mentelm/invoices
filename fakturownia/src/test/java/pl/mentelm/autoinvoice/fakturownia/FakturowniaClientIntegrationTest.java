package pl.mentelm.autoinvoice.fakturownia;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

class FakturowniaClientIntegrationTest extends FakturowniaIntegrationTest {

    @Autowired
    private FakturowniaClient client;

    @Test
    void shouldGetInvoices() {
        // Given
        wm.register(get(urlPathEqualTo("/invoices.json"))
                .withQueryParam("api_token", equalTo("test-token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("get_invoices_response.json")));

        // When & Then
        client.getInvoices("test-token", null)
                .as(StepVerifier::create)
                .assertNext(invoices -> {
                    assertThat(invoices).hasSize(1);
                    assertThat(invoices.get(0).getId()).isEqualTo(1L);
                    assertThat(invoices.get(0).getNumber()).isEqualTo("FV/1");
                })
                .verifyComplete();
    }

    @Test
    void shouldCreateInvoice() {
        // Given
        var invoiceDto = FakturowniaTestData.dummyInvoiceDto();
        var request = InvoiceRequest.of("test-token", invoiceDto);

        wm.register(post(urlPathEqualTo("/invoices.json"))
                .withRequestBody(matchingJsonPath("$.api_token", equalTo(request.getApiToken())))
                .withRequestBody(matchingJsonPath("$.invoice.kind", equalTo(invoiceDto.getKind())))
                .withRequestBody(matchingJsonPath("$.invoice.number", equalTo(invoiceDto.getNumber())))
                .withRequestBody(matchingJsonPath("$.invoice.sell_date", equalTo(invoiceDto.getSellDate().toString())))
                .withRequestBody(matchingJsonPath("$.invoice.issue_date", equalTo(invoiceDto.getIssueDate().toString())))
                .withRequestBody(matchingJsonPath("$.invoice.payment_to", equalTo(invoiceDto.getPaymentTo().toString())))
                .withRequestBody(matchingJsonPath("$.invoice.seller_name", equalTo(invoiceDto.getSellerName())))
                .withRequestBody(matchingJsonPath("$.invoice.seller_tax_no", equalTo(invoiceDto.getSellerTaxNo())))
                .withRequestBody(matchingJsonPath("$.invoice.buyer_name", equalTo(invoiceDto.getBuyerName())))
                .withRequestBody(matchingJsonPath("$.invoice.buyer_tax_no", equalTo(invoiceDto.getBuyerTaxNo())))
                .withRequestBody(matchingJsonPath("$.invoice.buyer_email", equalTo(invoiceDto.getBuyerEmail())))
                .withRequestBody(matchingJsonPath("$.invoice.positions[0].name", equalTo(invoiceDto.getPositions().get(0).getName())))
                .withRequestBody(matchingJsonPath("$.invoice.positions[0].tax", equalTo(invoiceDto.getPositions().get(0).getTax())))
                .withRequestBody(matchingJsonPath("$.invoice.positions[0].total_price_gross", equalTo(invoiceDto.getPositions().get(0).getTotalPriceGross().toString())))
                .withRequestBody(matchingJsonPath("$.invoice.positions[0].quantity", equalTo(invoiceDto.getPositions().get(0).getQuantity().toString())))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("create_invoice_response.json")
                        .withTransformers("response-template")
                        .withTransformerParameters(Map.of(
                                "invoiceNumber", request.getInvoice().getNumber()
                        ))
                ));

        // When & Then
        client.createInvoice(request)
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertThat(response.getId()).isEqualTo(2L);
                    assertThat(response.getNumber()).isEqualTo(invoiceDto.getNumber());
                })
                .verifyComplete();
    }

    @Test
    void shouldGetInvoicePdf() {
        // Given
        byte[] pdfContent = "fake pdf".getBytes();
        wm.register(get(urlPathEqualTo("/invoices/1.pdf"))
                .withQueryParam("api_token", equalTo("test-token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/pdf")
                        .withBody(pdfContent)));

        // When & Then
        client.getInvoicePdf(1L, "test-token")
                .as(StepVerifier::create)
                .expectNextMatches(bytes -> new String(bytes).equals("fake pdf"))
                .verifyComplete();
    }
}
