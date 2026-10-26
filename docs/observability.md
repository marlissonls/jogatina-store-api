# Logging

## Overview

Application logging is configured with SLF4J and Logback. The logging setup combines request-level context, application events, and exception handling to make application behavior easier to inspect.

The implementation is organized around three responsibilities:

- `LoggingFilter`: enriches log entries with HTTP request context.
- `logback-spring.xml`: configures console output, formatting, and log levels.
- Application services and exception handlers: record relevant business events and errors.

## Request Context

`LoggingFilter` extends `OncePerRequestFilter` and populates the SLF4J Mapped Diagnostic Context (MDC) with information associated with the current HTTP request.

| MDC field | Description |
|---|---|
| `correlationId` | Uses the incoming `X-Correlation-Id` header or generates a UUID when the header is absent. |
| `user` | Authenticated user's email, or `anonymous` when no recognized authenticated principal is available. |
| `method` | HTTP request method. |
| `path` | Request URI. |
| `status` | HTTP response status, added after request processing completes. |

MDC values are automatically included in log messages according to the configured Logback pattern. This avoids passing request metadata explicitly to every logging statement.

The filter removes its MDC entries in a `finally` block to prevent request context from leaking into subsequent requests handled by the same thread.

## Console Configuration

`logback-spring.xml` configures a console appender and a custom log pattern. Output includes the timestamp, severity, process ID, correlation ID, user, HTTP method, request path, thread, logger name, message, and exception details.

Colors distinguish selected fields to improve readability during local development and runtime inspection.

The root logging level is set to `INFO`. Consequently, `DEBUG` messages are suppressed unless the logging level is overridden for the relevant logger or environment.

## Application Logging

Services and exception handlers emit logs for relevant business events.

For example, the checkout workflow logs its start at `DEBUG` level and successful completion at `INFO` level. The completion event includes the customer ID, order ID, item count, and order total.

The conflict exception handler records conflicts at `WARN` level and returns an HTTP `409 Conflict` response.

These logs complement request metadata: the filter supplies HTTP context, while application-level logging records meaningful events within the business workflow.

## Considerations

- The `LoggingFilter` currently runs in the Spring Security filter chain. Its placement must be coordinated with authentication processing if logs are expected to contain the authenticated user's identity.
- The `status` field is added to the MDC after the filter chain returns. Logs emitted during request processing therefore do not automatically contain the final response status.
- The console pattern does not currently include the `status` MDC field.
- The correlation ID is not currently returned in the HTTP response, and incoming values are not validated by the filter.
- Request logging context is separate from authentication and authorization; the filter does not enforce access control.

## Tracing

Distributed tracing may be introduced to track requests as they propagate through application components and, eventually, external services.

Planned capabilities:
- Instrument HTTP requests and relevant application operations with trace and span identifiers.
- Propagate trace context across service boundaries and outbound HTTP calls.
- Capture execution duration and errors for relevant operations.
- Integrate traces with an observability backend for querying and visualization.

## Metrics

Application and HTTP metrics may be introduced to monitor performance, availability, and resource usage.

Planned capabilities:
- Collect HTTP request counts, response status codes, and latency distributions.
- Monitor error rates and identify slow endpoints.
- Measure execution time and outcomes of relevant business operations, such as checkout and stock reservation.
- Expose application and JVM metrics, including memory usage, garbage collection, and thread activity.
- Integrate metrics with a monitoring backend and dashboards.

Metrics should use bounded labels to avoid excessive time-series cardinality. Identifiers such as user IDs, order IDs, and correlation IDs should not be used as metric labels.