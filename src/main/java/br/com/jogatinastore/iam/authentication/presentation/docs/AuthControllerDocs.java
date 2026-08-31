package br.com.jogatinastore.iam.authentication.presentation.docs;

import br.com.jogatinastore.shared.exception.response.ExceptionResponse;
import br.com.jogatinastore.iam.authentication.application.dto.AccountCredentialsDTO;
import br.com.jogatinastore.iam.authentication.application.dto.RefreshTokenDTO;
import br.com.jogatinastore.iam.authentication.application.dto.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthControllerDocs {

    @Operation(
            summary = "Authenticates a user and returns a token",
            description = "Validates user credentials and generates an access token for role.",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<TokenDTO> signIn(@RequestBody @Valid AccountCredentialsDTO credentials);

    @Operation(
            summary = "Refresh token for authenticated user and returns a token",
            description = "Generates a new access token using the provided refresh token.",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<TokenDTO> refresh(@RequestBody @Valid RefreshTokenDTO refresh);
}
