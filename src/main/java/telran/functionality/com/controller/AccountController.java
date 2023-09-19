package telran.functionality.com.controller;
/**
 * Class AccountController - Rest controller, to give answer to user's request. It contains methods to work with transactions, accounts and agreements.
 *
 * @author Olena Averchenko
 */

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import telran.functionality.com.converter.Converter;
import telran.functionality.com.dto.*;
import telran.functionality.com.entity.Account;
import telran.functionality.com.entity.Agreement;
import telran.functionality.com.entity.ClientData;
import telran.functionality.com.entity.Transaction;
import telran.functionality.com.enums.Status;
import telran.functionality.com.exceptions.ForbiddenAccessException;
import telran.functionality.com.service.AccountService;
import telran.functionality.com.service.AgreementService;
import telran.functionality.com.service.ClientDataService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Controller", description = "Rest controller, which contains methods to work with transactions, accounts and agreements")
public class AccountController {

    private final AccountService accountService;
    private final AgreementService agreementService;
    private final Converter<Account, AccountDto, AccountCreateDto> accountConverter;
    private final Converter<Agreement, AgreementDto, AgreementCreateDto> agreementConverter;
    private final Converter<Transaction, TransactionDto, TransactionCreateDto> transactionConverter;
    private final ClientDataService clientDataService;


    @Operation(
            summary = "Getting accounts",
            description = "It allows us to get the list of all existing in database accounts",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Empty list of accounts")}
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping
    public List<AccountDto> getAll() {
        return accountService.getAll().stream()
                .map(accountConverter::toDto)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Getting agreements",
            description = "It allows us to get the list of all existing in database agreements",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Empty list of agreements")}
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/allAgreements")
    public List<AgreementDto> getAllAgreements() {
        return agreementService.getAll().stream()
                .map(agreementConverter::toDto)
                .toList();
    }


    @Operation(
            summary = "Getting the account",
            description = "It allows us to get a certain account by its id, if it exists",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Account doesn't exist")}
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/client/{id}")
    public AccountDto getById(@PathVariable(name = "id") @Parameter(description = "Identifier of the account") UUID id) {
        getAccess(id);
        return accountConverter.toDto(accountService.getById(id));
    }

    @Operation(
            summary = "Saving the account",
            description = "It allows us to save a new account",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping("/client")
    public AccountDto save(@RequestBody @Parameter(description = "New account to save") AccountCreateDto accountCreateDto) {
        return accountConverter.toDto(accountService.save(accountConverter.toEntity(accountCreateDto)));
    }


    @Operation(
            summary = "Update status of the account",
            description = "It allows us to update status of the account",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Account doesn't exist"),
                    @ApiResponse(responseCode = "403", description = "Account has not been activated yet. There is no active agreement."),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/updateStatus/{id}/{newStatus}")
    public AccountDto updateStatus(@PathVariable(name = "id") @Parameter(description = "Identifier of the account") UUID id,
                                   @PathVariable(name = "newStatus") @Parameter(description = "New status of the account") Status newStatus) {
        return accountConverter.toDto(accountService.changeStatus(id, newStatus));
    }


    @Operation(
            summary = "Deleting the account",
            description = "It allows us to delete the account by its id, if it exists, and if the balance is empty",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Account doesn't exist"),
                    @ApiResponse(responseCode = "403", description = "Unable to delete bank account. It belongs to the bank."),
                    @ApiResponse(responseCode = "403", description = "Not empty balance"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable(name = "id") @Parameter(description = "Identifier of the account") UUID id) {
        accountService.delete(id);
    }


    @Operation(
            summary = "Getting the balance",
            description = "It allows us to get the balance information about account by its id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Account doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/client/getBalanceOf/{id}")
    public BalanceDto getBalanceOf(@PathVariable(name = "id") @Parameter(description = "Identifier of the account") UUID id) {
        getAccess(id);
        return accountService.getBalanceOf(id);
    }


    @Operation(
            summary = "Getting the history of transaction",
            description = "It allows us to get the history of transactions by the account id, if there is at least one of them stored in database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Account doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/client/getHistoryOf/{id}")
    public List<TransactionDto> getHistoryOfTransactionsByAccountId(@PathVariable(name = "id") @Parameter(description = "Identifier of the account") UUID id) {
        getAccess(id);
        return accountService.getHistoryOfTransactionsByAccountId(id).stream()
                .map(transactionConverter::toDto).collect(Collectors.toList());
    }


    @Operation(
            summary = "Transferring money",
            description = "It allows us to transfer money between two accounts,if there is no other restrains",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Account doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data."),
                    @ApiResponse(responseCode = "451", description = "Account is not allowed to sent money."),
                    @ApiResponse(responseCode = "403", description = "Unable to transfer negative amount"),
                    @ApiResponse(responseCode = "409", description = "There is not enough money to transfer")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/client/transferMoneyBetweenAccounts")
    public UUID transferMoneyBetweenAccounts(@RequestBody @Parameter(description = "Data to transfer money between accounts as a0 new transaction") TransactionCreateDto transactionCreateDto) {
        getAccess(transactionCreateDto.getDebitAccountId());
        return accountService.transferMoneyBetweenAccounts(transactionCreateDto).getId();
    }


    @Operation(
            summary = "Withdraw money",
            description = "It allows us to withdraw money from the account, if there is no other restrains",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Account doesn't exist"),
                    @ApiResponse(responseCode = "404", description = "Client doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data."),
                    @ApiResponse(responseCode = "403", description = "Account with id doesn't belong to the client."),
                    @ApiResponse(responseCode = "403", description = "Unable to transfer negative amount"),
                    @ApiResponse(responseCode = "409", description = "There is not enough money to transfer")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/client/withdrawMoneyFrom")
    public AccountDto withdrawMoney(@RequestParam @Parameter(description = "Identifier of the client") UUID clientId,
                                    @RequestParam @Parameter(description = "Identifier of the account, which must belong to the client") UUID accountId,
                                    @RequestParam @Parameter(description = "Sum to withdraw") double sum) {
        getAccess(accountId);
        return accountConverter.toDto(accountService.withdrawMoney(clientId, accountId, sum));
    }

    @Operation(
            summary = "Putting money",
            description = "It allows us to put money to the account, if it exists",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Account doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/client/putMoneyTo")
    public AccountDto putMoney(@RequestParam @Parameter(description = "Identifier of the account") UUID accountId,
                               @RequestParam @Parameter(description = "Sum of money to put on the account") double sum) {
        return accountConverter.toDto(accountService.putMoney(accountId, sum));
    }

    @Operation(
            summary = "Create new agreement",
            description = "It allows us to create a new agreement for the existing account, and activates the last one.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping("/agreements/createNewDeal")
    public void createNewDeal(@RequestBody @Parameter(description = "New agreement to create") AgreementCreateDto agreement) {
        agreementService.save(agreementConverter.toEntity(agreement));
    }

    @Operation(
            summary = "Inactivate the account",
            description = "It allows us to change status of the account to 'INACTIVE'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Account doesn't exist")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/inactivateAccount/{id}")
    public void inactivateAccount(@PathVariable(name = "id") @Parameter(description = "Identifier of the account") UUID id) {
        accountService.inactivateAccount(id);
    }

    @Operation(
            summary = "Account belongs to client",
            description = "It allows us to check if the account belongs to the client",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Account doesn't exist"),
                    @ApiResponse(responseCode = "404", description = "Client doesn't exist"),
                    @ApiResponse(responseCode = "403", description = "Account doesn't belong to the client.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/accountBelongsToClient/{clientId}/{accountId}")
    public boolean accountBelongsToClient(@PathVariable(name = "clientId") @Parameter(description = "Identifier of the client") UUID clientId,
                                          @PathVariable(name = "accountId") @Parameter(description = "Identifier of the account") UUID accountId) {
        return accountService.accountBelongsToClient(accountId, clientId);
    }

    @Operation(
            summary = "Inactivate the agreement",
            description = "It allows us to change status of the agreement to 'INACTIVE'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Agreement doesn't exist")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/agreements/inactivateAgreement/{id}")
    public void inactivateAccountOfAgreement(@PathVariable(name = "id") @Parameter(description = "Identifier of the agreement") long id) {
        agreementService.inactivateAgreement(id);
    }

    @Operation(
            summary = "Change status of the agreement",
            description = "It allows us to change status of the agreement.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Agreement doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/agreements/changeStatus/{id}/{status}")
    public void changeStatusOfAgreement(@PathVariable(name = "id") @Parameter(description = "Identifier of the agreement") long id,
                                        @PathVariable(name = "status") @Parameter(description = "New status for the chosen agreement") Status status) {
        agreementService.changeStatus(id, status);
    }

    @Operation(
            summary = "Change interest rate",
            description = "It allows us to change interest rate for the agreement.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfull request"),
                    @ApiResponse(responseCode = "404", description = "Agreement doesn't exist"),
                    @ApiResponse(responseCode = "400", description = "Wrong type of provided data.")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PutMapping("/agreements/changeInterestRate/{id}/{newRate}")
    public void changeInterestRateForAgreement(@PathVariable(name = "id") @Parameter(description = "Identifier of the agreement") long id,
                                               @PathVariable(name = "newRate") @Parameter(description = "New rate for the agreement") double newRate) {
        agreementService.changeInterestRate(id, newRate);
    }

    @Hidden
    public UUID getCurrentClientId() {
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) currentAuthentication.getPrincipal();
        String currentClientLogin = currentUser.getUsername();
        ClientData clientData = clientDataService.getByLogin(currentClientLogin);
        if (clientData != null) {
            return clientData.getClient().getId();
        }
        return null;
    }

    @Hidden
    public void getAccess(UUID id) {
        UUID currentClientId = getCurrentClientId();
        if (currentClientId != null) {
            if (!currentClientId.equals(accountService.getById(id).getClient().getId())) {
                throw new ForbiddenAccessException("Access to the account " + id + " with the current access is denied.");
            }
        }
    }

}
