# private-note
A private message encrypter that works like https://privnote.com.

## Design
On submission:
* a secretId is generated (16 chars using private-note.js:random_string())
* value is encrypted using secretId AES 256 with library sjcl.js
* secretId is hashed (SHA-1) using private-note.js:sha1()
* secretIdHash and encryptedValue submitted to web api (HTTP POST)
* web api calls lambda that 
    * stores encryptedValue in values S3 bucket with key = secretIdHash 
    * creates FIFO SQS queue with name=secretIdHash. If sqs queue exists throw exception.
    * calculates expiryTimeEpochMs from expiryMs (duration) using currentTime + expiryMs
    * places string expiryTimeEpochMs on queue 
* display view link using secretId in url

On view:
* secretId is extracted from url parameters
* secretId is hashed (SHA-1)
* secretIdHash is submitted to web api (HTTP GET)
* web api calls lambda that
    * connects to SQS queue with name=secretIdHash
    * reads first message (expiryTimeEpochMs) from SQS queue
    * deletes SQS queue
    * if (expiryTimeEpochMs > now) return encryptedValue from S3 bucket with key = secretIdHash
    * else throw message expired exception 
* decrypt encryptedValue in browser client using sjcl.js:decrypt() 
* display decrypted value 

Notes:

* maximum retain time for S3 bucket is 30 days

TODO
* allow user to specify TTL (Time to Live) for individual messages



