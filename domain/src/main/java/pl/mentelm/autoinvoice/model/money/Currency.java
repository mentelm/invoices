package pl.mentelm.autoinvoice.model.money;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(staticName = "of")
public class Currency {

    @Pattern(regexp = "^[A-Z]{3}$")
    String code;
}
