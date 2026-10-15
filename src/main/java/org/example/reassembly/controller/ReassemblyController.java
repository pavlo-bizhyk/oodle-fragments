package org.example.reassembly.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.reassembly.service.ReassemblyService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ReassemblyController {

    private static final Logger log =
            LoggerFactory.getLogger(ReassemblyController.class);

    private final ReassemblyService reassemblyService;

    public ReassemblyController(ReassemblyService reassemblyService) {
        this.reassemblyService = reassemblyService;
    }

    @Operation(
            summary = "Reassemble document fragments",
            description = "Reconstructs a document from semicolon-separated fragments."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Document successfully reassembled",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(type = "string")
            )
    )
    @PostMapping(
            value = "/reassemble",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String reassemble(@RequestBody String input) {
        log.info("Received reassembly request ({} characters)", input.length());

        String result = reassemblyService.reassemble(input);

        log.info("Reassembly request completed ({} characters)", result.length());
        return result;
    }
}
