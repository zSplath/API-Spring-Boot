package med.clift.api.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();

        return ResponseEntity.badRequest().body(erros.stream().map(ValidationDTO::new).toList());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity tratarErroBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErroDTO("Credenciais inválidas"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity tratarErroAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErroDTO("Falha na autenticação"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroDTO> tratarErroJsonInvalido() {
        return ResponseEntity.badRequest().body(new ErroDTO("JSON inválido"));
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<ErroDTO> tratarErroValidacaoRegrasNegocio(ValidacaoException e) {
        return ResponseEntity.badRequest().body(new ErroDTO(e.getMessage()));
    }

    private record ValidationDTO (String campo, String mensagem) {
        public ValidationDTO (FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    private record ErroDTO (String erro) {
    }
}
