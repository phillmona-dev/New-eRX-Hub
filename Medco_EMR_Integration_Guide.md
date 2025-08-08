# MEDCO EMR INTEGRATION GUIDE
## Out-of-Stock Prescription Management API

---

### **OVERVIEW**
Medco's Out-of-Stock Prescription API enables EMR systems and pharmacies to seamlessly handle medication shortages. When prescribed medications are unavailable, this API automatically notifies patients via SMS with alternative pharmacy locations and provides tracking capabilities.

**Base URL:** `http://your-server:8300`  
**API Version:** v1  
**Authentication:** API Key Required

---

### **AUTHENTICATION**
**API Key:** `hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d`  
**Header:** `X-API-Key: hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d`

---

### **PRIMARY ENDPOINT**
**URL:** `POST /api/v1/prescription-out-of-stock/incoming`  
**Purpose:** Submit out-of-stock prescription data

### **REQUEST FORMAT**
```json
{
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
    "sex": "F",
    "houseNumber": "123",
    "insuranceNumber": "INS123456",
    "kebele": "Kebele 01",
    "woredaId": "W001",
    "paymentTypeId": "CASH",
    "patientTypeId": "REGULAR"
  },
  "prescriptionDetails": [
    {
      "medicationName": "Amoxicillin 500mg",
      "quantity": 21,
      "numberOfDuration": 7,
      "administrationId": "ORAL",
      "frequencyTypeId": "TID",
      "itemUnitId": "TABLET",
      "additionalNote": "Take with food"
    }
  ],
  "prescriptionDiagnosis": [
    {
      "diagnosisTypeId": "INFECTION",
      "additionalInfo": "Upper respiratory tract infection"
    }
  ],
  "prescriptionDate": "2024-01-15T10:30:00",
  "prescriptionNumber": "RX-2024-001",
  "institutionId": "PHARMACY-001",
  "prescriptionUUID": "550e8400-e29b-41d4-a716-446655440000"
}
```

### **REQUIRED FIELDS**
| Field | Description |
|-------|-------------|
| `prescriber.firstName` | Prescriber's first name |
| `prescriber.lastName` | Prescriber's last name |
| `prescriber.qualification` | Medical qualification |
| `prescriber.registrationNumber` | License number |
| `patient.firstName` | Patient's first name |
| `patient.lastName` | Patient's last name |
| `patient.phoneNumber` | **Critical:** Patient phone (+251...) |
| `patient.age` | Patient age |
| `patient.sex` | Gender (M/F/Other) |
| `prescriptionDetails[].medicationName` | Medication name |
| `prescriptionDetails[].quantity` | Quantity prescribed |
| `prescriptionDate` | Prescription date (ISO 8601) |
| `prescriptionNumber` | Unique prescription number |
| `institutionId` | Your pharmacy identifier |

---

### **SUCCESS RESPONSE**
```json
{
  "success": true,
  "message": "Prescription processed successfully. Patient notified.",
  "prescriptionId": "12345",
  "uniqueIdentifier": "RX-2024-001234",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### **ERROR RESPONSES**
**401 Unauthorized:** `"Invalid API Key"`  
**400 Bad Request:**
```json
{
  "statusCode": 400,
  "timestamp": "2024-01-15T10:30:00Z",
  "message": "Validation failed: Patient phone number is required"
}
```

---

### **INTEGRATION EXAMPLES**

#### **cURL**
```bash
curl -X POST "http://your-server:8300/api/v1/prescription-out-of-stock/incoming" \
  -H "Content-Type: application/json" \
  -H "X-API-Key: hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d" \
  -d @prescription.json
```

#### **JavaScript/Node.js**
```javascript
const axios = require('axios');

const submitPrescription = async (data) => {
  try {
    const response = await axios.post(
      'http://your-server:8300/api/v1/prescription-out-of-stock/incoming',
      data,
      {
        headers: {
          'Content-Type': 'application/json',
          'X-API-Key': 'hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d'
        }
      }
    );
    return response.data;
  } catch (error) {
    console.error('API Error:', error.response?.data);
    throw error;
  }
};
```

#### **Python**
```python
import requests

def submit_prescription(prescription_data):
    url = "http://your-server:8300/api/v1/prescription-out-of-stock/incoming"
    headers = {
        "Content-Type": "application/json",
        "X-API-Key": "hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d"
    }
    
    response = requests.post(url, headers=headers, json=prescription_data)
    response.raise_for_status()
    return response.json()
```

#### **C#**
```csharp
using System.Net.Http;
using System.Text;
using Newtonsoft.Json;

public async Task<string> SubmitPrescription(object prescriptionData)
{
    using var client = new HttpClient();
    client.DefaultRequestHeaders.Add("X-API-Key", "hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d");
    
    var json = JsonConvert.SerializeObject(prescriptionData);
    var content = new StringContent(json, Encoding.UTF8, "application/json");
    
    var response = await client.PostAsync(
        "http://your-server:8300/api/v1/prescription-out-of-stock/incoming", 
        content
    );
    
    return await response.Content.ReadAsStringAsync();
}
```

---

### **SYSTEM WORKFLOW**
1. **Receive:** API receives prescription data
2. **Validate:** System validates all required fields
3. **Process:** Creates patient record (if new) and prescription entry
4. **Check Stock:** Queries connected pharmacies for medication availability
5. **Notify Patient:** Sends SMS with unique tracking ID and alternative pharmacy locations
6. **Track:** Prescription stored for future reference and status tracking

### **PATIENT SMS EXAMPLE**
```
Your prescription has been recorded as out of stock.
Your unique identifier is: RX-2024-001234.

Medicine availability:
- Amoxicillin 500mg: Available at City Pharmacy (2.5km away)
  Contact: +251911555000

For assistance, call: +251911234567
```

---

### **TESTING**
**Test Environment:** `http://test-server:8300`  
**Test API Key:** `test_hc_7f9a3b2e4d5c1f8e6a0d9b7c5e3f1a2d`

**Test Data:**
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

### **BEST PRACTICES**
- **Phone Format:** Always use international format (+251...)
- **Error Handling:** Implement retry logic for 5xx errors
- **Timeouts:** Set appropriate request timeouts (30-60 seconds)
- **Logging:** Log all API interactions for debugging
- **Security:** Store API keys securely (environment variables)
- **Validation:** Validate data before sending to API

### **RATE LIMITS**
- **Per Minute:** 100 requests
- **Per Day:** 10,000 requests
- **Burst:** 10 requests per second

---

### **ADDITIONAL ENDPOINTS**

#### **Search Prescriptions**
**URL:** `GET /api/v1/prescription-out-of-stock/search`  
**Parameters:** `identifier`, `phoneNumber`, `patientName`, `prescriptionDateStart`, `prescriptionDateEnd`

#### **Manual Entry (No API Key Required)**
**URL:** `POST /api/v1/prescription-out-of-stock`  
**Use:** Alternative submission method with simplified patient/medicine structure

---

### **TROUBLESHOOTING**
| Issue | Solution |
|-------|----------|
| 401 Unauthorized | Verify API key in X-API-Key header |
| 400 Bad Request | Check required fields and data format |
| Network Timeout | Implement retry with exponential backoff |
| SMS Not Received | Verify phone number format and validity |

---

### **SUPPORT & CONTACT**
**Technical Support:** api-support@medco.com  
**Phone:** +251-11-XXX-XXXX  
**Production API Key Request:** Contact Medco with pharmacy details

---

### **INTEGRATION CHECKLIST**
- [ ] Obtain production API key from Medco
- [ ] Test with provided test environment and data
- [ ] Implement authentication with X-API-Key header
- [ ] Map your EMR fields to API structure
- [ ] Implement error handling and retry logic
- [ ] Test patient phone number validation
- [ ] Set up request logging and monitoring
- [ ] Train staff on new out-of-stock workflow
- [ ] Go live with production endpoint

---

**Document Version:** 1.0 | **Date:** January 2024  
**Medco Development Team** | **api-support@medco.com**
