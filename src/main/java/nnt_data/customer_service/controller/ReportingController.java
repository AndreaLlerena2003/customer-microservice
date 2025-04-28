package nnt_data.customer_service.controller;

import lombok.RequiredArgsConstructor;
import nnt_data.customer_service.api.ReportingCustomerApi;
import nnt_data.customer_service.domain.service.ReportingService;
import nnt_data.customer_service.entity.ReportingCustomerSummaryPostRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ReportingController implements ReportingCustomerApi {
    private final ReportingService reportingService;

    /**
     * POST /reporting-customer/summary : Generate product report
     * Generate a complete and general report for a specific bank product within a time interval specified by the user
     *
     * @param reportingCustomerSummaryPostRequest (required)
     * @param exchange
     * @return Reporte de productos generado exitosamente (status code 200)
     */
    @Override
    public Mono<ResponseEntity<Map<String, Object>>> reportingCustomerSummaryPost(Mono<ReportingCustomerSummaryPostRequest> reportingCustomerSummaryPostRequest, ServerWebExchange exchange) {
        return reportingCustomerSummaryPostRequest.flatMap(request ->
                reportingService.getReportingForProduct(request.getProductId(), request.getStartDate(), request.getEndDate())
                        .map(report -> {
                            return ResponseEntity.ok(report);
                        })
                        .onErrorResume(e -> {
                            Map<String, Object> errorResponse = new HashMap<>();
                            errorResponse.put("error", e.getMessage());
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                        }));
    }
}
