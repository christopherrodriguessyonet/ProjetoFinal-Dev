package br.com.syonet.taskmanager.resource;

import br.com.syonet.taskmanager.dto.ErrorDTO;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        int status = 500;
        String message = "Erro interno do servidor";

        if (exception instanceof WebApplicationException) {
            status = ((WebApplicationException) exception).getResponse().getStatus();
            message = exception.getMessage();
            if (message == null || message.isEmpty()) {
                message = ((WebApplicationException) exception).getResponse().getStatusInfo().getReasonPhrase();
            }
        } else if (exception.getCause() != null) {
            message = exception.getCause().getMessage();
        } else if (exception.getMessage() != null) {
            message = exception.getMessage();
        }

        ErrorDTO error = new ErrorDTO(message, status);
        return Response.status(status)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
} 