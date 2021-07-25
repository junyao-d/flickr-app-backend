# Flickr Appplicaion Backend
Flickr api based photo search application. 
# Tech Stacks
- Language: Java 11
- Framework: Spring Boot 2.1.3
- Dependency Manager: Maven 2.2.0
- Host Platform: GCP App Engine
- Database: Google Cloud SQL (MySQL)

# Flickr API 

BASE URL: https://www.flickr.com/services/feeds/photos_public.gne

### Create New User

* **URL**

  /flickrapi/users/register

* **Method:**

  `POST`
  
*  **URL Params**  
  
   None

* **Data Params**
    
    **Required:**
  
   `username=[string]`
   `password=[string]`

* **Success Response:**

  * **Code:** 200 OK <br />
    **Content:** `None`
 
* **Error Response:**

  * **Code:** 400 BAD_REQUEST <br />
    **Content:** `{ error : "User doesn't exist" }`

  OR

  * **Code:** 203 NON_AUTHORITATIVE_INFORMATION <br />
    **Content:** `None`


### User Authentication
Check if the request has valid access token in the header

* **URL**

  /flickrapi/users/me

* **Method:**

  `GET`
  
*  **URL Params**  
  
   None

* **Data Params**
  
   None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{"token" :  <token>}`
 
* **Error Response:**

    **Code:** 203 NON_AUTHORITATIVE_INFORMATION <br />
    **Content:** `None`


### User Login

* **URL**

  /flickrapi/users/login

* **Method:**

  `POST`
  
*  **URL Params**  
  
   None

* **Data Params**
  
    **Required:**
  
   `username=[string]`
   `password=[string]`

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{token :  <token>}`
 
* **Error Response:**

  * **Code:** 203 NON_AUTHORITATIVE_INFORMATION <br />
    **Content:** `None`


### Search Photo

* **URL**

  /flickrapi/users/login

* **Method:**

  `POST`
  
*  **URL Params**  
  
   None

* **Data Params**
  
    **Required:**
  
   `username=[string]`
   `password=[string]`

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{token :  <token>}`
 
* **Error Response:**

  * **Code:** 404 NON_AUTHORITATIVE_INFORMATION <br />
    **Content:** HTML Elements