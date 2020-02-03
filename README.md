# private-note
A private message encrypter that works like https://privnote.com but you own the source and the data in your own AWS account.

Features
* Deployed to *your* AWS account with CloudFormation script

## Design
On submission:
* a /*secretId* is generated (16 chars using private-note.js:random_string())
* value is encrypted using *secretId* AES 256 with library sjcl.js
* *secretId* is hashed (SHA-1) using private-note.js:sha1() and converted to hexadecimal
* *secretIdHash* and *encryptedValue* submitted to web api (HTTP POST)
* web api calls lambda that 
    * stores *encryptedValue* in values S3 bucket with key = *secretIdHash* 
    * creates FIFO SQS queue with name=secretIdHash. If sqs queue exists throw exception.
    * calculates expiryTimeEpochMs from expiryMs (duration) using currentTime + expiryMs
    * places string expiryTimeEpochMs on queue 
* display view link using *secretId* in url

On view:
* *secretId* is extracted from url parameters
* *secretId* is hashed (SHA-1)
* *secretIdHash* is submitted to web api (HTTP GET)
* web api calls lambda that
    * connects to SQS queue with name=*secretIdHash*
    * reads first message (expiryTimeEpochMs) from SQS queue
    * deletes SQS queue
    * if (expiryTimeEpochMs > now) read *encryptedValue* from S3 bucket with key = secretIdHash
    * else throw MessageExpiredException 
    * delete S3 object with key = secretIdHash
    * return encryptedValue
* decrypt *encryptedValue* in browser client using sjcl.js:decrypt() 
* display decrypted value 

Notes:

* maximum retain time for objects in S3 bucket is 30 days
* when an object is expired from the S3 bucket the corresponding SQS queue is deleted as well

TODO
* allow user to specify TTL (Time to Live) for individual messages

### Resources
* One Lambda to support `store` and `get` methods of the API 
* One S3 bucket to store encrypted values with object read and write access from the API lambda
* One S3 bucket with static html/js resources
* Dynamic SQS queue creation, read and write access from the API lambda
* Api Gateway with `store(key, value)` and `get(key)` methods that integrate with API lambda. Key will be secretIdHash and Value will be encryptedValue. SHA-1 hash is 20 bytes = 28 bytes when Base 64 encoded. Max length of Value will be order of 7MB unencoded (due to the 10MB payload limit on API Gateway).
* Api Gateway method that integrates with S3 static resources bucket

## Security considerations
Of course this application is ALL about security!

Important things to note:

* AES 256 is considered strong enough for top secret encryption by the NSA
* The server never sees the unencrypted value nor the secret key used for the encryption
* The FIFO SQS queue is exactly once delivery so only one user can see the unencrypted value before it is deleted from the queue
* Sending a one-time use link by email is problematic because a man-in-the-middle attack might intercept an email, use the link, and pass on a new link to the recipient. Ideally your communication channel will be secure enough that man-in-the-middle attacks are not possible.