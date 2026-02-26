# CCI Conference Center - Frontend MVP

## Project Overview
Build a modern, responsive web application for **Christ Cosmopolitan Incorporated (CCI) Conference Center** - a conference management system that allows church members and non-members to register, browse conferences, and make payments via Paystack integration.

## Branding
- **Organization:** Christ Cosmopolitan Incorporated (CCI)
- **Application Name:** CCI Conference Center
- **Purpose:** Manage church conferences, events, and member registrations
- **Target Users:** CCI church members, non-members, and administrators

## Tech Stack
- React with TypeScript
- Tailwind CSS for styling
- React Router for navigation
- Axios for API calls
- JWT authentication
- Shadcn/ui components

## API Base URL
- Development: `http://localhost:8080`
- Production: `https://backend.mandmservicescorp.org/shortly`

## Payment System Overview

### How Payments Work
The system uses Paystack for payment processing. Users can top up their account balance, which can then be used for conference registrations or other purposes.

**Key Points:**
- Users have an account balance (stored in `balance` field)
- Top-ups are processed through Paystack
- Conference registrations deduct from balance (implementation may vary)
- All transactions are recorded with status tracking
- CCI members get discounted conference prices

**Transaction Types:**
- `TOPUP`: Account balance top-up via Paystack
- Other types may be added for conference payments

**Transaction Statuses:**
- `PENDING`: Payment initiated but not completed
- `SUCCESS`: Payment completed successfully
- `FAILED`: Payment failed

**Important Notes:**
- The `InitiatePaymentRequest` only requires `amount` field (email is extracted from authenticated user)
- Payment verification automatically updates user balance on success
- Always verify payments after Paystack redirect to ensure balance is updated

## Core Features & Pages

### 1. Authentication Pages

#### Registration Page (`/register`)
**Form Fields:**
- Name (text, required)
- Email (email, required)
- Password (password, required)
- WhatsApp Number (text, format: +233XXXXXXXXX)
- Sex (dropdown: MALE, FEMALE, required)
- Age Group (text, required)
- CCI Member (checkbox) - Important: Affects conference pricing

**Note:** CCI Members receive discounted rates on conference registrations

**API Endpoint:** `POST /api-user/register`
**Request Body:**
```json
{
  "name": "string",
  "email": "string",
  "password": "string",
  "whatsappNumber": "string",
  "sex": "MALE" | "FEMALE",
  "ageGroup": "string",
  "isCCIMember": boolean
}
```
**Response:** `{ "message": "string" }`

#### Login Page (`/login`)
**Form Fields:**
- Email (email, required)
- Password (password, required)

**API Endpoint:** `POST /api-user/login`
**Request Body:**
```json
{
  "email": "string",
  "password": "string"
}
```
**Response:**
```json
{
  "user": {
    "id": "string",
    "name": "string",
    "email": "string",
    "whatsappNumber": "string",
    "sex": "MALE" | "FEMALE",
    "ageGroup": "string",
    "isCCIMember": boolean,
    "userRole": "USER" | "ADMIN",
    "balance": number,
    "isActivated": boolean
  },
  "token": "string"
}
```
**Action:** Store token in localStorage and redirect to dashboard

#### Forgot Password Flow
**Step 1 - Request Reset (`/forgot-password`):**
- API: `POST /api-user/request-forget-password`
- Body: `{ "email": "string" }`

**Step 2 - Verify Code (`/verify-code`):**
- API: `POST /api-user/verify-identity`
- Body: `{ "email": "string", "code": "string" }`

**Step 3 - Reset Password (`/reset-password`):**
- API: `POST /api-user/reset-password`
- Body: `{ "email": "string", "newPassword": "string" }`

### 2. Public Pages

#### Home/Landing Page (`/`)
- Hero section with CCI Conference Center branding
- Welcome message about Christ Cosmopolitan Incorporated
- Featured upcoming conferences
- Call-to-action buttons (Register, Browse Conferences)
- About CCI section
- Benefits of CCI membership (discounted conference rates)

#### Conferences List Page (`/conferences`)
**Features:**
- Display all conferences in a grid/card layout
- Show: name, description, dates, location, prices
- Pagination (30 items per page)
- Filter/sort by date

**API Endpoint:** `GET /api-user/confrences?page=0&size=30&sort=startDate,desc`
**Response:**
```json
{
  "content": [
    {
      "id": "string",
      "name": "string",
      "description": "string",
      "startDate": number (timestamp),
      "endDate": number (timestamp),
      "location": "string",
      "cciPrice": number,
      "nonCciPrice": number,
      "isActive": boolean
    }
  ],
  "totalPages": number,
  "totalElements": number
}
```

#### Conference Detail Page (`/conferences/:id`)
**Features:**
- Full conference details
- Display appropriate price (CCI member vs non-CCI)
- "Register & Pay" button (requires login)

**API Endpoint:** `GET /api-user/confrence/{id}`

### 3. Protected Pages (Require Authentication)

#### User Dashboard (`/dashboard`)
**Features:**
- Welcome message with user name
- Account balance display
- Quick stats (registered conferences, pending payments)
- Recent transactions list
- Registered conferences list
- Navigation to profile and conferences

**API Endpoint:** `GET /api-auth/me`
**Headers:** `Authorization: Bearer {token}`

**Get User Transactions:**
**API Endpoint:** `GET /api-auth/user-transactions?page=0&size=10&sort=transactionDate,desc`
**Headers:** `Authorization: Bearer {token}`
**Response:**
```json
{
  "content": [
    {
      "id": "string",
      "userInfo": {
        "userId": "string",
        "email": "string",
        "name": "string",
        "whatsappNumber": "string"
      },
      "amount": "string",
      "transactionDate": number,
      "reference": "string",
      "status": "PENDING" | "SUCCESS" | "FAILED",
      "accessCode": "string"
    }
  ],
  "totalPages": number,
  "totalElements": number
}
```

**Get User Conference Registrations:**
**API Endpoint:** `GET /api-auth/confrence-regitraion?page=0&size=50&sort=registrationDate,desc`
**Headers:** `Authorization: Bearer {token}`
**Response:**
```json
{
  "content": [
    {
      "id": "string",
      "confrenceId": "string",
      "userEmail": "string",
      "registrationDate": number,
      "registrationType": "CCIMEMBER" | "NONCCIMEMBER",
      "cost": number,
      "userInfo": {
        "name": "string",
        "userId": "string",
        "email": "string",
        "whatsappNumber": "string"
      },
      "confrenceInfo": {
        "confrenceId": "string",
        "confrenceName": "string",
        "location": "string",
        "startDate": "string",
        "endDate": "string"
      }
    }
  ],
  "totalPages": number,
  "totalElements": number
}
```

#### Conference Registration Page (`/register-conference/:conferenceId`)
**Features:**
- Display conference details (name, dates, location, description)
- Show price based on user's CCI membership status
- Registration confirmation button
- Success message after registration
- Redirect to "My Registrations" or payment page

**API Endpoint:** `POST /api-auth/register-confrence`
**Headers:** `Authorization: Bearer {token}`
**Request Body:**
```json
{
  "conferenceId": "string"
}
```
**Response:**
```json
{
  "id": "string",
  "confrenceId": "string",
  "userEmail": "string",
  "registrationDate": number,
  "registrationType": "CCIMEMBER" | "NONCCIMEMBER",
  "cost": number,
  "userInfo": {
    "name": "string",
    "userId": "string",
    "email": "string",
    "whatsappNumber": "string"
  },
  "confrenceInfo": {
    "confrenceId": "string",
    "confrenceName": "string",
    "location": "string",
    "startDate": "string",
    "endDate": "string"
  }
}
```
**Note:** Registration is separate from payment. Users register first, then can pay later.

#### Account Top-Up Page (`/top-up` or `/wallet/top-up`)
**Features:**
- Display current account balance
- Amount input field (user enters amount to top up)
- "Pay with Paystack" button
- Paystack payment integration
- Transaction history link

**API Endpoint:** `POST /api/payments/initiate`
**Headers:** `Authorization: Bearer {token}`
**Request Body:**
```json
{
  "amount": number
}
```
**Response:**
```json
{
  "status": true,
  "message": "string",
  "data": {
    "authorization_url": "string",
    "access_code": "string",
    "reference": "string"
  }
}
```
**Action:** Redirect user to `authorization_url` for Paystack payment

**Payment Verification (After Paystack Redirect):**
**API Endpoint:** `GET /api-user/payment/verify/{reference}`
**Response:**
```json
{
  "message": "Payment verification completed",
  "details": {
    "status": "success" | "failed"
  }
}
```
**Action:** 
- Called automatically after Paystack redirects back to your callback URL
- Display success/failure message to user
- Update user's balance display
- Show transaction in history

**Payment Flow:**
1. User enters amount on top-up page
2. Click "Pay with Paystack" → API creates transaction (PENDING status)
3. User redirected to Paystack payment page
4. User completes payment on Paystack
5. Paystack redirects back to your callback URL with reference
6. Your app calls verify endpoint with reference
7. If successful, user's balance is updated
8. Transaction status updated to SUCCESS/FAILED

#### User Profile Page (`/profile`)
**Features:**
- Display user information (name, email, whatsapp, age group, sex)
- Show CCI membership status
- Account balance display
- Edit profile button (optional for MVP)
- Link to transactions and registrations

**API Endpoint:** `GET /api-auth/me`
**Headers:** `Authorization: Bearer {token}`

### 4. Admin Pages (Role: ADMIN)

#### Admin Dashboard (`/admin`)
**Features:**
- Total users count
- Total conferences count
- Total registrations count
- Recent registrations
- Quick actions (Add Conference, View Users, View Registrations)

#### Manage Conferences (`/admin/conferences`)
**Features:**
- List all conferences
- Add new conference button
- Edit/Delete actions per conference
- Toggle active/inactive status button (quick toggle without opening edit form)
- Visual indicator for active/inactive status (e.g., badge, color coding)

**UI Suggestions:**
- Each conference card/row should have:
  - Active/Inactive badge (green for active, gray for inactive)
  - Toggle button (e.g., switch/toggle icon) for quick status change
  - Edit button (opens edit form with all fields)
  - Delete button (with confirmation)

**Get Conference by ID (for editing):****
- API: `GET /api-admin/confrence/{id}`
- Headers: `Authorization: Bearer {token}`
- Response:
```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "startDate": number,
  "endDate": number,
  "location": "string",
  "cciPrice": number,
  "nonCciPrice": number,
  "isActive": boolean
}
```

**Create Conference:**
- API: `POST /api-admin/confrence`
- Headers: `Authorization: Bearer {token}`
- Body:
```json
{
  "name": "string",
  "description": "string",
  "startDate": number,
  "endDate": number,
  "location": "string",
  "cciPrice": number,
  "nonCciPrice": number
}
```

**Update Conference:**
- API: `PUT /api-admin/confrence/{id}`
- Headers: `Authorization: Bearer {token}`
- Body:
```json
{
  "name": "string",
  "description": "string",
  "startDate": number,
  "endDate": number,
  "location": "string",
  "cciPrice": number,
  "nonCciPrice": number,
  "isActive": boolean
}
```
**Note:** All fields are optional in update request

**Toggle Conference Status:**
- API: `PUT /api-admin/confrence/{id}/toggle-status`
- Headers: `Authorization: Bearer {token}`
- Body: None (empty request)
- Response:
```json
{
  "message": "Conference status updated to active" | "Conference status updated to inactive"
}
```
**Note:** This endpoint toggles the conference active/inactive status. If currently active, it becomes inactive and vice versa.

**Delete Conference:**
- API: `DELETE /api-admin/confrence-delete/{id}`

#### Manage Users (`/admin/users`)
**Features:**
- Searchable user list
- Filter by name, email, whatsapp number
- Pagination
- View user transactions

**API Endpoint:** `GET /api-admin/users?name=&email=&whatsappNumber=&page=0&size=10`

**Get User Transactions:**
- API: `GET /api-admin/transactions/by-user/{email}?page=0&size=10`
- Headers: `Authorization: Bearer {token}`
- Response: Paginated list of transactions

**Get All Transactions:**
- API: `GET /api-admin/transactions-all?page=0&size=10`
- Headers: `Authorization: Bearer {token}`
- Response:
```json
{
  "content": [
    {
      "id": "string",
      "userInfo": {
        "userId": "string",
        "email": "string",
        "name": "string",
        "whatsappNumber": "string"
      },
      "amount": "string",
      "transactionDate": number,
      "reference": "string",
      "status": "PENDING" | "SUCCESS" | "FAILED",
      "accessCode": "string"
    }
  ],
  "totalPages": number,
  "totalElements": number
}
```

#### Manage Conference Registrations (`/admin/registrations`)
**Features:**
- View all conference registrations
- Filter by conference, user email, or registration type
- Pagination
- Export capabilities

**API Endpoint:** `GET /api-admin/confrence-regitration?conferenceId=&userEmail=&registrationType=&page=0&size=50`
**Headers:** `Authorization: Bearer {token}`
**Query Parameters:**
- `conferenceId` (optional): Filter by conference ID
- `userEmail` (optional): Filter by user email
- `registrationType` (optional): Filter by type (CCIMEMBER or NONCCIMEMBER)

**Response:**
```json
{
  "content": [
    {
      "id": "string",
      "confrenceId": "string",
      "userEmail": "string",
      "registrationDate": number,
      "registrationType": "CCIMEMBER" | "NONCCIMEMBER",
      "cost": number,
      "userInfo": {
        "name": "string",
        "userId": "string",
        "email": "string",
        "whatsappNumber": "string"
      },
      "confrenceInfo": {
        "confrenceId": "string",
        "confrenceName": "string",
        "location": "string",
        "startDate": "string",
        "endDate": "string"
      }
    }
  ],
  "totalPages": number,
  "totalElements": number
}
```

## Global Components

### Navigation Bar
- Logo/Brand name: "CCI Conference Center" or "CCI" logo
- Tagline: "Christ Cosmopolitan Incorporated" (optional, in smaller text)
- Links: Home, Conferences, About CCI
- If not logged in: Login, Register buttons
- If logged in: Dashboard, My Registrations, Transactions, Top Up, Profile, Logout
- If admin: Admin link (with dropdown: Dashboard, Conferences, Users, Registrations, Transactions)

### Protected Route Component
- Check for JWT token in localStorage
- Verify token validity
- Redirect to login if not authenticated

### Admin Route Component
- Check authentication
- Verify user role is ADMIN
- Redirect if not authorized

## State Management
- Use React Context or Zustand for:
  - Authentication state (user, token, isAuthenticated)
  - User profile data
  - Loading states
  - Error handling

## API Integration Setup
```typescript
// axios instance
const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle 401 errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

## Design Requirements
- Modern, clean UI with professional church/religious aesthetic
- Mobile-responsive (mobile-first approach)
- Color scheme: Professional with church-appropriate colors (consider blues, purples, golds, or whites)
- CCI branding throughout (logo, colors, typography)
- Loading states for all API calls
- Error messages displayed clearly
- Success notifications for actions
- Form validation with error messages
- Accessible (WCAG 2.1 AA)
- Professional imagery related to conferences and church events

## Key User Flows

### New User Registration & Conference Registration
1. User visits landing page
2. Clicks "Register" → fills registration form (includes CCI membership checkbox)
3. Receives success message → redirected to login
4. Logs in → redirected to dashboard
5. Browses conferences → selects conference → views details
6. Clicks "Register for Conference" → registration created with appropriate price (CCI/Non-CCI)
7. Views registration in "My Registrations"
8. Can top up account balance to pay for conference if needed

### Account Top-Up Flow
1. User navigates to "Top Up" page from navigation or dashboard
2. Views current balance
3. Enters amount to top up
4. Clicks "Pay with Paystack" → initiates payment
5. Redirected to Paystack payment page
6. Completes payment on Paystack
7. Paystack redirects back to app with reference
8. App automatically verifies payment
9. If successful, balance is updated
10. Transaction appears in transaction history
11. User can now use balance for conference payments or other purposes

### Admin Conference Management
1. Admin logs in → sees admin dashboard
2. Navigates to "Manage Conferences"
3. Clicks "Add Conference" → fills form
4. Submits → conference created
5. Can edit/delete existing conferences
6. Can view all registered users
7. Can view all conference registrations with filters
8. Can view all transactions

## Error Handling
- Display user-friendly error messages
- Handle network errors gracefully
- Show validation errors on forms
- 404 page for invalid routes
- Unauthorized access redirects

## MVP Priorities
**Phase 1 (Core):**
- Authentication (Login, Register with CCI membership option)
- Conference listing and details
- User dashboard with balance display
- Conference registration page
- Basic navigation

**Phase 2 (Essential):**
- Account top-up page with Paystack integration
- Payment verification flow
- User registrations list
- User transactions list
- Admin conference management
- Admin registrations view
- User search for admin

**Phase 3 (Nice-to-have):**
- User profile page
- Password reset flow
- Advanced filtering on registrations
- Transaction history details
- Export functionality for admin
- Email notifications display
- Analytics dashboard

## Testing Checklist
- [ ] User can register and login
- [ ] User can register with CCI membership option
- [ ] User can browse conferences
- [ ] User can view conference details with appropriate pricing (CCI/Non-CCI)
- [ ] Authenticated user can access dashboard
- [ ] Dashboard displays user's account balance
- [ ] User can register for a conference
- [ ] User can view their registrations
- [ ] User can view their transactions
- [ ] User can access top-up page
- [ ] User can initiate top-up payment via Paystack
- [ ] Payment verification works after Paystack redirect
- [ ] User balance updates after successful payment
- [ ] Admin can create/edit/delete conferences
- [ ] Admin can view users
- [ ] Admin can view all registrations with filters
- [ ] Admin can view all transactions
- [ ] Logout clears authentication
- [ ] Protected routes redirect to login
- [ ] Mobile responsive on all pages
