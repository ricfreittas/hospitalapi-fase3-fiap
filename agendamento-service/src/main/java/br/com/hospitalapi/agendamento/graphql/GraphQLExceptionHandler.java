package br.com.hospitalapi.agendamento.graphql;

import br.com.hospitalapi.agendamento.exception.AccessDeniedBusinessException;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GraphQLExceptionHandler {

    @GraphQlExceptionHandler
    public GraphQLError handleAccessDenied(
            AccessDeniedBusinessException exception,
            DataFetchingEnvironment environment
    ) {

        return GraphQLError.newError()
                .message(exception.getMessage())
                .path(environment.getExecutionStepInfo().getPath())
                .location(environment.getField().getSourceLocation())
                .build();
    }
}