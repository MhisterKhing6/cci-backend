# Conference Management API - Swagger Documentation

## Accessing Swagger UI

Once the application is running, access the Swagger UI at:

**Local Development:**
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

**Production:**
- Swagger UI: `https://backend.mandmservicescorp.org/shortly/swagger-ui.html`
- OpenAPI JSON: `https://backend.mandmservicescorp.org/shortly/api-docs`

## API Endpoints Overview

### User APIs (`/api-user`)
Public endpoints for user registration, authentication, and conference browsing:
- `POST /api-user/register` - Register new user
- `POST /api-user/login` - User login (returns JWT token)
- `POST /api-user/request-forget-password` - Request password reset
- `POST /api-user/verify-identity` - Verify identity with code
- `POST /api-user/reset-password` - Reset password
- `GET /api-user/confrences` - List all conferences (paginated)
- `GET /api-user/confrence/{id}` - Get conference by ID
- `GET /api-user/health` - Health check

### Authentication APIs (`/api-auth`)
Requires JWT authentication:
- `GET /api-auth/me` - Get current user profile

### Admin APIs (`/api-admin`)
Requires ADMIN role:
- `POST /api-admin/confrence` - Create conference
- `PUT /api-admin/confrence/{id}` - Update conference
- `DELETE /api-admin/confrence-delete/{id}` - Delete conference
- `GET /api-admin/users` - Search users (with filters)

### Payment APIs (`/api/payments`)
Requires JWT authentication:
- `POST /api/payments/initiate` - Initiate Paystack payment

## Authentication

Most endpoints require JWT authentication. To use protected endpoints in Swagger:

1. Login via `/api-user/login` endpoint
2. Copy the JWT token from the response
3. Click the "Authorize" button at the top of Swagger UI
4. Enter: `Bearer <your-token>`
5. Click "Authorize"

## Configuration

Swagger configuration is in:
- `OpenApiConfig.java` - OpenAPI/Swagger configuration
- `application.yml` - Swagger UI settings

## Features

- Interactive API documentation
- Try out endpoints directly from browser
- JWT Bearer token authentication
- Request/response examples
- Schema definitions
- Organized by tags (User, Admin, Payment, Authentication)
