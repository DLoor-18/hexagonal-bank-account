package ec.com.sofka.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

@Schema(description = "Object representing the data to create a user.")
public class UserRequestDTO implements Serializable {

    @Schema(description = "First name of user", example = "Juan")
    @NotNull(message = "firstName cannot be null")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+$", message = "Incorrect firstName format")
    private String firstName;

    @Schema(description = "Last name of user", example = "Zambrano")
    @NotNull(message = "lastName cannot be null")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+$", message = "Incorrect lastName format")
    private String lastName;

    @Schema(description = "Identification code of user", example = "1000000000")
    @NotNull(message = "ci cannot be null")
    @Pattern(regexp = "^[0-9]+$", message = "Incorrect ci format")
    @Size(min = 10, max = 10, message = "Incorrect ci length")
    private String ci;

    @Schema(description = "Email of user", example = "user@gmail.com")
    @NotNull(message = "email cannot be null")
    @Email( message = "Invalid email")
    private String email;

    @Schema(description = "Password of user", example = "User123.")
    @NotNull(message = "password cannot be null")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).+$", message = "Incorrect password format")
    @Size(min = 8, max = 16, message = "Incorrect password length")
    private String password;

    @Schema(description = "Status of user", example = "ACTIVE")
    @NotNull(message = "status cannot be null")
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "Incorrect status")
    private String status;


    public UserRequestDTO(String firstName, String lastName, String ci, String email, String password, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ci = ci;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}