# EMR Integration API Documentation
## Out-of-Stock Prescription Management System

### Overview
This document provides comprehensive integration guidelines for Electronic Medical Record (EMR) systems and pharmacies to integrate with Medco's Out-of-Stock Prescription Management System. When medications are unavailable in your pharmacy, this API allows you to seamlessly notify the system and ensure patients receive alternative solutions.

---

## Base Information

**Base URL:** `http://your-server:8300`  
**API Version:** v1  
**Content-Type:** `application/json`  
**Authentication:** API Key Required

---

## Authentication

### API Key Authentication
All API calls require authentication using an API key in the request header.

**Header Name:** `X-API-Key`  
**API Key:** `hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d` *(Contact Medco for your production API key)*

#### Authentication Methods:
1. **Header (Recommended):**
   ```http
   X-API-Key: hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d
   ```

2. **Query Parameter (Alternative):**
   ```http
   POST /api/v1/prescription-out-of-stock/incoming?X-API-Key=hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d
   ```

### Authentication Errors
- **401 Unauthorized:** Invalid or missing API key
- **Response:** `"Invalid API Key"`

---

## Primary Endpoint: Submit Out-of-Stock Prescription

### Endpoint Details
**URL:** `POST /api/v1/prescription-out-of-stock/incoming`  
**Purpose:** Submit prescription data when medications are out of stock  
**Authentication:** Required (`@RequiresApiKey`)

### Request Headers
```http
Content-Type: application/json
X-API-Key: hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d
```

### Request Body Structure

```json
{
  "prescriber": {
    "firstName": "string",
    "middleName": "string",
    "lastName": "string", 
    "qualification": "string",
    "registrationNumber": "string",
    "rowGuid": "uuid"
  },
  "patient": {
    "firstName": "string",
    "middleName": "string", 
    "lastName": "string",
    "phoneNumber": "string",
    "age": 0,
    "ageType": "string",
    "weight": 0.0,
    "sex": "string",
    "houseNumber": "string",
    "cardNumber": "string",
    "insuranceNumber": "string",
    "kebele": "string",
    "rowGuid": "string",
    "sponsorName": "string",
    "woredaId": "string",
    "paymentTypeId": "string",
    "patientTypeId": "string"
  },
  "prescriptionDetails": [
    {
      "medicationName": "string",
      "numberOfDuration": 0,
      "administrationId": "string",
      "frequencyTypeId": "string", 
      "itemUnitId": "string",
      "quantity": 0,
      "additionalNote": "string",
      "orderNumber": 0
    }
  ],
  "prescriptionDiagnosis": [
    {
      "diagnosisTypeId": "string",
      "additionalInfo": "string"
    }
  ],
  "prescriptionDate": "2024-01-15T10:30:00",
  "prescriptionNumber": "string",
  "rowGuid": "string",
  "institutionId": "string",
  "prescriptionUUID": "string"
}
```

### Field Descriptions

#### Prescriber Object
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| firstName | string | Yes | Prescriber's first name |
| middleName | string | No | Prescriber's middle name |
| lastName | string | Yes | Prescriber's last name |
| qualification | string | Yes | Medical qualification (e.g., "MD", "PharmD") |
| registrationNumber | string | Yes | Medical license/registration number |
| rowGuid | UUID | No | Unique identifier for prescriber |

#### Patient Object  
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| firstName | string | Yes | Patient's first name |
| middleName | string | No | Patient's middle name |
| lastName | string | Yes | Patient's last name |
| phoneNumber | string | **Yes** | Patient's phone number (for SMS notifications) |
| age | integer | Yes | Patient's age |
| ageType | string | No | Age type (e.g., "years", "months") |
| weight | double | No | Patient's weight in kg |
| sex | string | Yes | Patient's gender ("M", "F", "Other") |
| houseNumber | string | No | House/address number |
| cardNumber | string | No | Patient card number |
| insuranceNumber | string | No | Insurance identification number |
| kebele | string | No | Administrative area |
| sponsorName | string | No | Insurance sponsor name |
| woredaId | string | No | District identifier |
| paymentTypeId | string | No | Payment method identifier |
| patientTypeId | string | No | Patient category identifier |

#### Prescription Details Array
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| medicationName | string | **Yes** | Name of the medication |
| numberOfDuration | integer | Yes | Duration of treatment |
| administrationId | string | No | Route of administration ID |
| frequencyTypeId | string | No | Dosing frequency ID |
| itemUnitId | string | No | Unit of measurement ID |
| quantity | integer | **Yes** | Quantity prescribed |
| additionalNote | string | No | Special instructions |
| orderNumber | integer | No | Order sequence number |

#### Prescription Diagnosis Array
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| diagnosisTypeId | string | No | Type of diagnosis identifier |
| additionalInfo | string | No | Additional diagnostic information |

#### Root Level Fields
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| prescriptionDate | datetime | **Yes** | When prescription was issued (ISO 8601 format) |
| prescriptionNumber | string | Yes | Unique prescription number |
| rowGuid | string | No | Unique row identifier |
| institutionId | string | **Yes** | Your pharmacy/institution identifier |
| prescriptionUUID | string | No | Unique prescription UUID |

---

## Response Formats

### Success Response
**HTTP Status:** `200 OK`

```json
{
  "success": true,
  "message": "Prescription processed successfully. Patient notified with alternative pharmacy locations.",
  "prescriptionId": "12345",
  "uniqueIdentifier": "RX-2024-001234",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Error Responses

#### 400 Bad Request - Validation Error
```json
{
  "statusCode": 400,
  "timestamp": "2024-01-15T10:30:00Z",
  "message": "Validation failed: Patient phone number is required",
  "description": "uri=/api/v1/prescription-out-of-stock/incoming"
}
```

#### 401 Unauthorized - Invalid API Key
```json
{
  "error": "Invalid API Key"
}
```

#### 500 Internal Server Error
```json
{
  "statusCode": 500,
  "timestamp": "2024-01-15T10:30:00Z", 
  "message": "Internal server error occurred",
  "description": "uri=/api/v1/prescription-out-of-stock/incoming"
}
```

---

## Sample Integration Code

### cURL Example
```bash
curl -X POST "http://your-server:8300/api/v1/prescription-out-of-stock/incoming" \
  -H "Content-Type: application/json" \
  -H "X-API-Key: hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d" \
  -d '{
    "prescriber": {
      "firstName": "Dr. John",
      "lastName": "Smith", 
      "qualification": "MD",
      "registrationNumber": "MD12345"
    },
    "patient": {
      "firstName": "Jane",
      "lastName": "Doe",
      "phoneNumber": "+251911234567",
      "age": 35,
      "sex": "F"
    },
    "prescriptionDetails": [
      {
        "medicationName": "Amoxicillin 500mg",
        "quantity": 21,
        "numberOfDuration": 7
      }
    ],
    "prescriptionDate": "2024-01-15T10:30:00",
    "prescriptionNumber": "RX-2024-001",
    "institutionId": "PHARMACY-001"
  }'
```

### JavaScript/Node.js Example
```javascript
const axios = require('axios');

const submitOutOfStockPrescription = async (prescriptionData) => {
  try {
    const response = await axios.post(
      'http://your-server:8300/api/v1/prescription-out-of-stock/incoming',
      prescriptionData,
      {
        headers: {
          'Content-Type': 'application/json',
          'X-API-Key': 'hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d'
        }
      }
    );
    
    console.log('Success:', response.data);
    return response.data;
  } catch (error) {
    console.error('Error:', error.response?.data || error.message);
    throw error;
  }
};
```

### Python Example
```python
import requests
import json
from datetime import datetime

def submit_out_of_stock_prescription(prescription_data):
    url = "http://your-server:8300/api/v1/prescription-out-of-stock/incoming"
    headers = {
        "Content-Type": "application/json",
        "X-API-Key": "hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d"
    }
    
    try:
        response = requests.post(url, headers=headers, json=prescription_data)
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException as e:
        print(f"Error: {e}")
        raise
```

---

## System Behavior

### What Happens After Submission:

1. **Validation:** System validates the prescription data
2. **Patient Lookup:** Searches for existing patient or creates new record
3. **Stock Check:** Queries connected pharmacies for medication availability  
4. **SMS Notification:** Sends patient SMS with:
   - Unique tracking identifier
   - Alternative pharmacy locations with available medications
   - Contact information for follow-up
5. **Database Storage:** Stores prescription for tracking and reporting

### Patient Notification Example:
```
Your prescription has been recorded as out of stock. 
Your unique identifier is: RX-2024-001234. 
Please use this identifier when inquiring about your prescription.

Medicine availability:
- Amoxicillin 500mg: Available at City Pharmacy (2.5km away)
  Contact: +251911555000
  
For assistance, call: +251911234567
```

---

## Integration Best Practices

### 1. Error Handling
- Always check HTTP status codes
- Implement retry logic for 5xx errors
- Log all API interactions for debugging

### 2. Data Validation
- Ensure patient phone numbers are in international format (+251...)
- Validate required fields before submission
- Use proper datetime formatting (ISO 8601)

### 3. Security
- Store API keys securely (environment variables)
- Use HTTPS in production
- Implement request timeouts

### 4. Performance
- Implement connection pooling
- Use asynchronous requests when possible
- Cache API responses where appropriate

---

## Testing

### Test Environment
**Base URL:** `http://test-server:8300`  
**Test API Key:** `test_hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d`

### Test Data
Use the following test data for integration testing:

```json
{
  "prescriber": {
    "firstName": "Test",
    "lastName": "Doctor",
    "qualification": "MD", 
    "registrationNumber": "TEST123"
  },
  "patient": {
    "firstName": "Test",
    "lastName": "Patient",
    "phoneNumber": "+251911000000",
    "age": 30,
    "sex": "M"
  },
  "prescriptionDetails": [
    {
      "medicationName": "Test Medicine",
      "quantity": 10,
      "numberOfDuration": 5
    }
  ],
  "prescriptionDate": "2024-01-15T10:30:00",
  "prescriptionNumber": "TEST-001",
  "institutionId": "TEST-PHARMACY"
}
```

---

## Support and Contact

### Technical Support
- **Email:** api-support@medco.com
- **Phone:** +251-11-XXX-XXXX
- **Documentation:** [API Portal URL]

### Production API Key Request
Contact Medco technical team with:
- Pharmacy/Institution name
- Contact information  
- Integration timeline
- Expected API usage volume

---

## Changelog

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2024-01-15 | Initial API documentation |

---

## Additional API Endpoints

### Alternative Submission Endpoint (Manual Entry)
**URL:** `POST /api/v1/prescription-out-of-stock`
**Purpose:** Manual prescription entry (no API key required)
**Use Case:** For manual data entry or alternative integration methods

#### Request Body:
```json
{
  "patientDetail": {
    "patientFullName": "string",
    "gender": "string",
    "age": 0,
    "phoneNumber": "string",
    "houseNumber": "string",
    "idNumber": "string",
    "insuranceNumber": "string",
    "address": "string",
    "region": "string",
    "kebele": "string",
    "woreda": "string",
    "city": "string",
    "weight": 0.0
  },
  "medicineLists": [
    {
      "name": "string",
      "unit": "string",
      "quantity": 0,
      "description": "string",
      "totalPrice": 0.0
    }
  ]
}
```

### Search Prescriptions
**URL:** `GET /api/v1/prescription-out-of-stock/search`
**Purpose:** Search existing out-of-stock prescriptions
**Parameters:**
- `identifier` (optional): Prescription identifier
- `phoneNumber` (optional): Patient phone number
- `patientName` (optional): Patient name
- `idNumber` (optional): Patient ID number
- `prescriptionDateStart` (optional): Start date (YYYY-MM-DD)
- `prescriptionDateEnd` (optional): End date (YYYY-MM-DD)
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 10)

---

## Troubleshooting Guide

### Common Issues and Solutions

#### 1. Authentication Failures
**Problem:** Receiving 401 Unauthorized
**Solutions:**
- Verify API key is correct
- Check header name is exactly `X-API-Key`
- Ensure no extra spaces in API key
- Contact support for API key validation

#### 2. Validation Errors
**Problem:** Receiving 400 Bad Request
**Solutions:**
- Check all required fields are provided
- Validate phone number format (+251...)
- Ensure datetime is in ISO 8601 format
- Verify JSON structure matches documentation

#### 3. Network Timeouts
**Problem:** Request timeouts or connection errors
**Solutions:**
- Implement retry logic with exponential backoff
- Check network connectivity
- Verify server URL and port
- Contact support if persistent

#### 4. Missing Patient Notifications
**Problem:** Patients not receiving SMS
**Solutions:**
- Verify phone number format and validity
- Check if patient phone number is active
- Confirm SMS service is operational
- Review patient contact preferences

### HTTP Status Code Reference
- `200 OK`: Request successful
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Invalid or missing API key
- `404 Not Found`: Endpoint not found
- `500 Internal Server Error`: Server error
- `503 Service Unavailable`: Service temporarily unavailable

---

## Data Privacy and Compliance

### Data Handling
- All patient data is encrypted in transit and at rest
- Data retention follows healthcare regulations
- Access logs are maintained for audit purposes
- Patient consent is required for SMS notifications

### GDPR/Privacy Compliance
- Patient data is processed lawfully
- Data minimization principles applied
- Right to erasure supported
- Data portability available upon request

---

## Rate Limiting and Quotas

### API Limits
- **Rate Limit:** 100 requests per minute per API key
- **Daily Quota:** 10,000 requests per day
- **Burst Limit:** 10 requests per second

### Exceeding Limits
**Response:** `429 Too Many Requests`
```json
{
  "error": "Rate limit exceeded",
  "retryAfter": 60,
  "limit": 100,
  "remaining": 0
}
```

---

## Monitoring and Analytics

### Available Metrics
- API request volume and success rates
- Response time analytics
- Error rate monitoring
- Patient notification delivery status

### Reporting
- Monthly integration reports available
- Custom reporting upon request
- Real-time dashboard access for enterprise clients

---

## Migration Guide

### From Legacy Systems
1. **Assessment:** Review current prescription workflow
2. **Mapping:** Map existing data fields to API structure
3. **Testing:** Use test environment for validation
4. **Gradual Rollout:** Implement in phases
5. **Monitoring:** Track integration success metrics

### Version Upgrades
- Backward compatibility maintained for 12 months
- Deprecation notices provided 6 months in advance
- Migration guides provided for major version changes

---

## FAQ

### Q: What happens if the API is temporarily unavailable?
**A:** Implement retry logic with exponential backoff. Store failed requests locally and retry when service is restored.

### Q: Can we customize the SMS message content?
**A:** Standard message format is used for consistency. Custom messaging available for enterprise clients.

### Q: How quickly are patients notified?
**A:** SMS notifications are typically sent within 2-5 minutes of successful API submission.

### Q: Is there a sandbox environment?
**A:** Yes, test environment is available at `http://test-server:8300` with test API keys.

### Q: What data is required vs optional?
**A:** Minimum required: patient name, phone number, prescriber info, medication name, and quantity. See field tables for complete requirements.

### Q: How do we handle prescription updates?
**A:** Currently, prescriptions are immutable after submission. Contact support for special update requirements.

---

## Appendices

### Appendix A: Sample Integration Checklist
- [ ] Obtain API key from Medco
- [ ] Set up test environment
- [ ] Implement authentication
- [ ] Map data fields
- [ ] Implement error handling
- [ ] Test with sample data
- [ ] Implement retry logic
- [ ] Set up monitoring
- [ ] Train staff on new workflow
- [ ] Go live with production API key

### Appendix B: Field Mapping Template
| Your System Field | API Field | Required | Notes |
|-------------------|-----------|----------|-------|
| Doctor Name | prescriber.firstName + lastName | Yes | Split if needed |
| Patient Phone | patient.phoneNumber | Yes | Format: +251... |
| Medicine Name | prescriptionDetails[].medicationName | Yes | Exact name |
| Quantity | prescriptionDetails[].quantity | Yes | Integer value |

### Appendix C: Error Code Reference
| Code | Description | Action |
|------|-------------|--------|
| AUTH001 | Invalid API Key | Check API key |
| VAL001 | Missing required field | Review request data |
| VAL002 | Invalid phone format | Use +251... format |
| SYS001 | Database error | Retry request |
| SMS001 | SMS delivery failed | Check phone number |

---

*This documentation is maintained by Medco Development Team. For updates and additional endpoints, please refer to the API portal or contact technical support.*

**Document Version:** 1.0
**Last Updated:** January 15, 2024
**Next Review:** April 15, 2024
