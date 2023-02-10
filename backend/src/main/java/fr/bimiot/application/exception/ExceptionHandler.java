package fr.bimiot.application.exception;

import fr.bimiot.application.exception.type.BaseException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ExceptionHandler extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions errorAttributeOptions) {
        Throwable error = getError(webRequest);
        boolean hasStackTrace = errorAttributeOptions.isIncluded(ErrorAttributeOptions.Include.STACK_TRACE);
        return switch (error.getClass().getSimpleName()) {
            case "DomainException" -> handleRecoverableException((BaseException) error, hasStackTrace);
            default -> handleGenericException(error, hasStackTrace);
        };
    }

    private Map<String, Object> handleGenericException(Throwable error, boolean includeStackTrace) {
        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("code", "400");
        errorDetails.put("type", error.getMessage());
        errorDetails.put("message", error.getMessage());

        if (includeStackTrace) {
            errorDetails.put("trace", this.getStackTrace(error));
        }

        return errorDetails;
    }

    private Map<String, Object> handleRecoverableException(BaseException error, boolean includeStackTrace) {
        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("code", error.getCode() != null ? error.getCode() : "400");
        errorDetails.put("type", error.getClass().getSimpleName());
        errorDetails.put("message", error.getMessage());
        if (includeStackTrace) {
            errorDetails.put("trace", this.getStackTrace(error));
        }
        return errorDetails;
    }

    private String getStackTrace(Throwable error) {
        StringWriter stackTrace = new StringWriter();
        error.printStackTrace(new PrintWriter(stackTrace));
        stackTrace.flush();
        return stackTrace.toString();
    }
}
