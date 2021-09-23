package com.hu.assignment;

import com.hu.assignment.web.dto.*;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AssignmentApplicationTests {

    private static final String TEST_HOLDER = "987654321";
    private static final String TEST_ACCOUNT = "NL51INGB0000123456";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private RestTemplate patchRestTemplate;

    @BeforeEach
    void configureApacheHttpComponents() {
        this.patchRestTemplate = restTemplate.getRestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    @Test
    void whenHolderIsCreatedThenHolderIsAvailable() {
        //Given
        // Genereer een nieuwe unieke bsn zodat meerdere testruns elkaar niet beïnvloeden
        final var rand = new Random();
        final var bsn = String.format("%09d", rand.nextInt(1000000000));
        final var holderUri = "http://localhost:" + port + "/holders/" + bsn;
        final var holderRequest = new AccountHolderRequestDto(null, "Jan Jansen");

        //When
        HttpEntity<AccountHolderRequestDto> requestUpdate = new HttpEntity<>(holderRequest);
        var response = this.restTemplate.exchange(holderUri, HttpMethod.PUT, requestUpdate, AccountHolderDto.class);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        //Then
        var holder = this.restTemplate.getForEntity(holderUri, AccountHolderDto.class);
        assertThat(holder.hasBody()).isTrue();
        assertThat(holder.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(holder.getBody().getBsn()).isEqualTo(bsn);
        assertThat(holder.getBody().getName()).isEqualTo(holderRequest.getName());
    }

    @Test
    void givenHolderIsCreatedWhenHolderIsUpdatedThenNewHolderIsAvailable() {
        //Given
        // Genereer een nieuwe unieke bsn zodat meerdere testruns elkaar niet beïnvloeden
        final var rand = new Random();
        final var bsn = String.format("%09d", rand.nextInt(1000000000));
        final var holderUri = "http://localhost:" + port + "/holders/" + bsn;
        final var holderRequestCreate = new AccountHolderRequestDto(null, "Jan Jansen");
        final var holderRequestUpdate = new AccountHolderRequestDto(null, "Nick de Vries");
        // Maak de holder aan
        var requestCreate = new HttpEntity<>(holderRequestCreate);
        var response1 = this.restTemplate.exchange(holderUri, HttpMethod.PUT, requestCreate, AccountHolderDto.class);
        assertThat(response1.hasBody()).isTrue();
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        // holderRequestUpdate.setVersion(response1.getBody().getVersion());

        //When
        var requestUpdate2 = new HttpEntity<>(holderRequestUpdate);
        var response2 = this.restTemplate.exchange(holderUri, HttpMethod.PUT, requestUpdate2, AccountHolderDto.class);
        assertThat(response2.hasBody()).isTrue();
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);

        //Then
        var holder = this.restTemplate.getForEntity(holderUri, AccountHolderDto.class);
        assertThat(holder.hasBody()).isTrue();
        assertThat(holder.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(holder.getBody().getBsn()).isEqualTo(bsn);
        assertThat(holder.getBody().getName()).isEqualTo(holderRequestUpdate.getName());
    }

    @Test
    void givenHolderIsCreatedWhenHolderIsDeletedThenHolderIsNotAvailable() {
        //Given
        // Genereer een nieuwe unieke bsn zodat meerdere testruns elkaar niet beïnvloeden
        final var rand = new Random();
        final var bsn = String.format("%09d", rand.nextInt(1000000000));
        final var holderUri = "http://localhost:" + port + "/holders/" + bsn;
        final var holderRequestCreate = new AccountHolderRequestDto(null, "Jan Jansen");
        // Maak de holder aan
        var requestUpdate1 = new HttpEntity<>(holderRequestCreate);
        var response1 = this.restTemplate.exchange(holderUri, HttpMethod.PUT, requestUpdate1, AccountHolderDto.class);
        assertThat(response1.hasBody()).isTrue();
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);

        //When
        var response2 = this.restTemplate.exchange(holderUri, HttpMethod.DELETE, null, Void.class);
        assertThat(response2.hasBody()).isFalse();
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);

        //Then
        var response3 = this.restTemplate.getForEntity(holderUri, String.class);
        assertThat(response3.hasBody()).isTrue();
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void givenHolderIsAvailableWhenAccountIsCreatedThenAccountIsAvailable() {
        //Given
        final var accountCreateUri = "http://localhost:" + port + "/holders/" + TEST_HOLDER + "/accounts/";
        final var accountRequestDto = new AccountRequestDto(null, BigDecimal.valueOf(100.00));

        //When
        HttpEntity<AccountRequestDto> requestUpdate = new HttpEntity<>(accountRequestDto);
        var response1 = this.restTemplate.exchange(accountCreateUri, HttpMethod.POST, requestUpdate, AccountDto.class);
        assertThat(response1.hasBody()).isTrue();
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response1.getHeaders().containsKey("Location")).isTrue();
        final var accountUri = response1.getHeaders().get("Location").get(0);

        //Then
        var response2 = this.restTemplate.getForEntity(accountUri, AccountDto.class);
        assertThat(response2.hasBody()).isTrue();
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getBody().getIban()).isNotNull();
        assertThat(response2.getBody().getBalance()).isEqualByComparingTo(accountRequestDto.getBalance());
        assertThat(response2.getBody().isActive()).isEqualTo(true);
    }

    @Test
    void givenHolderIsAvailableAndAccountIsCreatedWhenAccountIsUpdatedThenNewAccountIsAvailable() {
        //Given
        final var accountCreateUri = "http://localhost:" + port + "/holders/" + TEST_HOLDER + "/accounts/";
        final var accountRequestCreate = new AccountRequestDto(null, BigDecimal.valueOf(100.00));
        final var accountRequestUpdate = new AccountRequestDto(null, BigDecimal.valueOf(200.00));
        // Maak het account aan
        var requestCreate = new HttpEntity<>(accountRequestCreate);
        var response1 = this.restTemplate.exchange(accountCreateUri, HttpMethod.POST, requestCreate, AccountDto.class);
        assertThat(response1.hasBody()).isTrue();
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response1.getHeaders().containsKey("Location")).isTrue();
        final var accountUri = response1.getHeaders().get("Location").get(0);
        // accountRequestUpdate.setVersion(response1.getBody().getVersion());

        //When
        var requestUpdate = new HttpEntity<>(accountRequestUpdate);
        var response2 = this.restTemplate.exchange(accountUri, HttpMethod.PUT, requestUpdate, AccountDto.class);
        assertThat(response2.hasBody()).isTrue();
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);

        //Then
        var response3 = this.restTemplate.getForEntity(accountUri, AccountDto.class);
        assertThat(response3.hasBody()).isTrue();
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response3.getBody().getIban()).isNotNull();
        assertThat(response3.getBody().getBalance()).isEqualByComparingTo(accountRequestUpdate.getBalance());
        assertThat(response2.getBody().isActive()).isEqualTo(true);
    }

    @Test
    void givenHolderIsAvailableAccountIsCreatedWhenAccountIsDeletedThenAccountIsNotAvailable() {
        //Given
        final var accountCreateUri = "http://localhost:" + port + "/holders/" + TEST_HOLDER + "/accounts/";
        final var accountRequestCreate = new AccountRequestDto(null, BigDecimal.valueOf(100.00));
        var accountRequestUpdate = new AccountRequestDto(null, BigDecimal.valueOf(200.00));
        // Maak het account aan
        var requestCreate = new HttpEntity<>(accountRequestCreate);
        var response1 = this.restTemplate.exchange(accountCreateUri, HttpMethod.POST, requestCreate, AccountDto.class);
        assertThat(response1.hasBody()).isTrue();
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response1.getHeaders().containsKey("Location")).isTrue();
        final var accountUri = response1.getHeaders().get("Location").get(0);
        //accountRequestUpdate.setVersion(response1.getBody().getVersion());

        //When
        var response2 = this.restTemplate.exchange(accountUri, HttpMethod.DELETE, null, Void.class);
        assertThat(response2.hasBody()).isFalse();
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);

        //Then
        var response3 = this.restTemplate.getForEntity(accountUri, String.class);
        assertThat(response3.hasBody()).isTrue();
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void givenHolderIsAvailableAndAccountIsCreatedWhenAccountIsDeactivatedThenNewAccountIsDeactivated() {
        //Given
        final var accountCreateUri = "http://localhost:" + port + "/holders/" + TEST_HOLDER + "/accounts/";
        final var accountRequestCreate = new AccountRequestDto(null, BigDecimal.valueOf(100.00));
        final var accountRequestUpdate = AccountPatchDto.builder().active(false).build();
        // Maak het account aan
        var requestCreate = new HttpEntity<>(accountRequestCreate);
        var response1 = this.restTemplate.exchange(accountCreateUri, HttpMethod.POST, requestCreate, AccountDto.class);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response1.getHeaders().containsKey("Location")).isTrue();
        assertThat(response1.hasBody()).isTrue();
        assertThat(response1.getBody().isActive()).isTrue();
        final var accountUri = response1.getHeaders().get("Location").get(0);
        //accountRequestUpdate.setVersion(response1.getBody().getVersion());

        //When
        var requestUpdate = new HttpEntity<>(accountRequestUpdate);
        var response2 = this.patchRestTemplate.exchange(accountUri, HttpMethod.PATCH, requestUpdate, AccountDto.class);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.hasBody()).isTrue();

        //Then
        var response3 = this.restTemplate.getForEntity(accountUri, AccountDto.class);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response3.hasBody()).isTrue();
        assertThat(response3.getBody().getIban()).isNotNull();
        assertThat(response2.getBody().isActive()).isFalse();
    }

    @Test
    void givenHoldersAndAccountsAreAvailableWhenHoldersOfAccountIsObtainedThenAllHoldersOfAccountAreGiven() {
        //Given
        final var accountUri = "http://localhost:" + port + "/accounts/" + TEST_ACCOUNT + "/holders/";

        //When
        var response2 = this.patchRestTemplate.getForEntity(accountUri, AccountHolderDto[].class);

        //Then
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.hasBody()).isTrue();
        assertThat(response2.getBody().length).isEqualTo(2);
        assertThat(Arrays.stream(response2.getBody())
                .map(AccountHolderDto::getBsn)
                .collect(Collectors.toList()))
                .containsAll(Arrays.asList("987654321", "123456789"));
    }

    @Test
    void givenHoldersAndAccountsAreAvailableWhenAccountsOfAnHolderIsObtainedThenAllAccountsOfHolderAreGiven() {
        //Given
        final var accountUri = "http://localhost:" + port + "/holders/" + TEST_HOLDER + "/accounts/";

        //When
        var response2 = this.restTemplate.getForEntity(accountUri, AccountDto[].class);

        //Then
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.hasBody()).isTrue();
        assertThat(response2.getBody().length).isEqualTo(3);
//        assertThat(Arrays.stream(response2.getBody())
//                .map(AccountDto::getIban)
//                .collect(Collectors.toList()))
//                .containsAll(Arrays.asList("NL51INGB0000123456", "NL37ABNA9999888877", "BG64BPMH909272JOQYEVJT"));
    }

    @Test
    void givenHoldersAreCreatedAndAccountIsCreatedWhenHolderIsAddedThenHoldersAreAvailable() {
        //Given
        // Genereer een nieuwe unieke bsn zodat meerdere testruns elkaar niet beïnvloeden
        final var rand = new Random();
        final var bsn1 = String.format("%09d", rand.nextInt(1000000000));
        final var holderUri1 = "http://localhost:" + port + "/holders/" + bsn1;
        final var holderRequestCreate1 = new AccountHolderRequestDto(null, "Jan Jansen");
        // Maak de holder 1 aan
        var requestUpdate1 = new HttpEntity<>(holderRequestCreate1);
        var response1 = this.restTemplate.exchange(holderUri1, HttpMethod.PUT, requestUpdate1, AccountHolderDto.class);
        assertThat(response1.hasBody()).isTrue();
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Holder 2
        final var bsn2 = String.format("%09d", rand.nextInt(1000000000));
        final var holderUri2 = "http://localhost:" + port + "/holders/" + bsn2;
        final var holderRequestCreate2 = new AccountHolderRequestDto(null, "Jan Jansen");
        // Maak de holder 2 aan
        var requestUpdate2 = new HttpEntity<>(holderRequestCreate2);
        var response2 = this.restTemplate.exchange(holderUri2, HttpMethod.PUT, requestUpdate2, AccountHolderDto.class);
        assertThat(response2.hasBody()).isTrue();
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Account
        final var accountCreateUri = "http://localhost:" + port + "/holders/" + bsn1 + "/accounts/";
        final var accountRequestCreate = new AccountRequestDto(null, BigDecimal.valueOf(100.00));
        // Maak het account aan
        var requestCreate = new HttpEntity<>(accountRequestCreate);
        var response3 = this.restTemplate.exchange(accountCreateUri, HttpMethod.POST, requestCreate, AccountDto.class);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response3.getHeaders().containsKey("Location")).isTrue();
        assertThat(response3.hasBody()).isTrue();
        assertThat(response3.getBody().isActive()).isTrue();
        final var accountUri = response3.getHeaders().get("Location").get(0);
        final var addHolderUri = accountUri + "/holders/" + bsn2;
        final var gettHoldersUri = accountUri + "/holders/";

        //When
        var response4 = this.patchRestTemplate.exchange(addHolderUri, HttpMethod.PUT, null, AccountDto.class);
        assertThat(response4.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response4.hasBody()).isFalse();

        //Then
        var response5 = this.restTemplate.getForEntity(gettHoldersUri, AccountHolderDto[].class);
        assertThat(response5.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response5.hasBody()).isTrue();
        assertThat(response5.getBody().length).isEqualTo(2);
        assertThat(Arrays.stream(response5.getBody())
                .map(AccountHolderDto::getBsn)
                .collect(Collectors.toList()))
                .containsAll(Arrays.asList(bsn1, bsn2));
    }

    @Test
    void givenHoldersAreCreatedAndAccountIsCreatedAndHolderisAddedWhenHolderRemovedThenHolderIsNotAvailable() {
        //Given
        // Genereer een nieuwe unieke bsn zodat meerdere testruns elkaar niet beïnvloeden
        final var rand = new Random();
        final var bsn1 = String.format("%09d", rand.nextInt(1000000000));
        final var holderUri1 = "http://localhost:" + port + "/holders/" + bsn1;
        final var holderRequestCreate1 = new AccountHolderRequestDto(null, "Jan Jansen");
        // Maak de holder 1 aan
        var requestUpdate1 = new HttpEntity<>(holderRequestCreate1);
        var response1 = this.restTemplate.exchange(holderUri1, HttpMethod.PUT, requestUpdate1, AccountHolderDto.class);
        assertThat(response1.hasBody()).isTrue();
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Holder 2
        final var bsn2 = String.format("%09d", rand.nextInt(1000000000));
        final var holderUri2 = "http://localhost:" + port + "/holders/" + bsn2;
        final var holderRequestCreate2 = new AccountHolderRequestDto(null, "Jan Jansen");
        // Maak de holder 2 aan
        var requestUpdate2 = new HttpEntity<>(holderRequestCreate2);
        var response2 = this.restTemplate.exchange(holderUri2, HttpMethod.PUT, requestUpdate2, AccountHolderDto.class);
        assertThat(response2.hasBody()).isTrue();
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Account
        final var accountCreateUri = "http://localhost:" + port + "/holders/" + bsn1 + "/accounts/";
        final var accountRequestCreate = new AccountRequestDto(null, BigDecimal.valueOf(100.00));
        // Maak het account aan
        var requestCreate = new HttpEntity<>(accountRequestCreate);
        var response3 = this.restTemplate.exchange(accountCreateUri, HttpMethod.POST, requestCreate, AccountDto.class);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response3.getHeaders().containsKey("Location")).isTrue();
        assertThat(response3.hasBody()).isTrue();
        assertThat(response3.getBody().isActive()).isTrue();
        final var accountUri = response3.getHeaders().get("Location").get(0);
        final var addHolderUri = accountUri + "/holders/" + bsn2;
        final var gettHoldersUri = accountUri + "/holders/";
        var response4 = this.patchRestTemplate.exchange(addHolderUri, HttpMethod.PUT, null, AccountDto.class);
        assertThat(response4.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        final var removeHolderUri = accountUri + "/holders/" + bsn1;

        //When
        var response5 = this.patchRestTemplate.exchange(removeHolderUri, HttpMethod.DELETE, null, Void.class);
        assertThat(response5.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response5.hasBody()).isFalse();

        //Then
        var response6 = this.restTemplate.getForEntity(gettHoldersUri, AccountHolderDto[].class);
        assertThat(response6.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response6.hasBody()).isTrue();
        assertThat(response6.getBody().length).isEqualTo(1);
        assertThat(response6.getBody()[0].getBsn()).isEqualTo(bsn2);
    }
}

