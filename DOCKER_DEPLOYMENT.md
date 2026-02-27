# Docker Deployment Guide

## Prerequisites
- Docker installed
- Docker Compose installed

## Quick Start

### 1. Build and Run with Docker Compose
```bash
docker-compose up -d
```

### 2. Stop Services
```bash
docker-compose down
```

### 3. View Logs
```bash
docker-compose logs -f app
```

## Manual Docker Commands

### Build Image
```bash
docker build -t cci-conference:latest .
```

### Run Container
```bash
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATA_MONGODB_URI=mongodb://admin:password@mongodb:27017/conference?authSource=admin \
  -e JWT_SECRET=your-secret-key \
  -e PAYSTACK_SECRET_KEY=your-paystack-key \
  -e PAYSTACK_BASE_CALLBACK_URL=http://localhost:8080/callback \
  --name cci-conference-app \
  cci-conference:latest
```

## Environment Variables

Create a `.env` file in the project root:

```env
# MongoDB
MONGO_USERNAME=admin
MONGO_PASSWORD=your-secure-password
MONGO_DATABASE=conference

# JWT
JWT_SECRET=your-jwt-secret-key-min-256-bits

# Paystack
PAYSTACK_SECRET_KEY=sk_test_your_paystack_secret_key
PAYSTACK_BASE_CALLBACK_URL=http://localhost:8080/callback
```

## Access Application

- **API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **MongoDB:** localhost:27017

## Production Deployment

### Using Docker Compose
```bash
docker-compose -f docker-compose.yml up -d
```

### Push to Registry
```bash
docker tag cci-conference:latest your-registry/cci-conference:latest
docker push your-registry/cci-conference:latest
```

## Troubleshooting

### Check Container Status
```bash
docker ps
```

### View Application Logs
```bash
docker logs cci-conference-app
```

### Access Container Shell
```bash
docker exec -it cci-conference-app sh
```

### Restart Services
```bash
docker-compose restart
```

### Clean Up
```bash
docker-compose down -v  # Remove volumes
docker system prune -a  # Clean all unused resources
```
