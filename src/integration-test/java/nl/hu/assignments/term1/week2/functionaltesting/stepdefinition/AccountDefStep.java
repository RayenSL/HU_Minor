package nl.hu.assignments.term1.week2.functionaltesting.stepdefinition;

import io.cucumber.java.DataTableType;
import io.cucumber.java.en.*;
import nl.hu.assignments.term1.week2.functionaltesting.web.dto.HolderDto;
import org.apache.logging.log4j.util.Strings;
import org.iban4j.Iban;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountDefStep extends AbstractStepDef {

    @DataTableType
    public HolderDto defineHolderDto(Map<String, String> entry) {
        final var id = entry.get("id");
        final var uuid = !Strings.isBlank(id) ? UUID.fromString(id) : null;
        return new HolderDto(uuid, entry.get("name"));
    }

    @Given("I have an Iban code {string}")
    public void iHaveAnIbanCode(String ibanCode) {
        testContext().reset();
        final var iban = Iban.valueOf(ibanCode);
        testContext().setPayload(iban);
    }

    @When("I try and retrieve the holders of the account")
    public void iTryAndRetrieveTheHoldersOfTheAccountWith() {
        final var iban = testContext().getPayload(Iban.class);
        String url = "/rekeningen/" + iban + "/houder";
        executeGet(url);
    }

    @Then("I get the expected holder names")
    public void iGetTheTheHoldersOfTheAccount(List<String> holders) {
        final var response = testContext().getResponse()
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath()
                .getList(".", HolderDto.class);
        assertThat(response)
                .extracting(HolderDto::getName)
                .containsExactlyElementsOf(holders);
    }
}
