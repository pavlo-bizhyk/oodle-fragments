package org.example.reassembly;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReassemblyController {

    private final ReassemblyService reassemblyService;

    public ReassemblyController(ReassemblyService reassemblyService) {
        this.reassemblyService = reassemblyService;
    }

    @PostMapping(
            value = "/reassemble",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String reassemble(@RequestBody String input) {
        return reassemblyService.reassemble(input);
    }
}
