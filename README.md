# private-note

## Design
On submission:
* a secretId is generated (16 chars using private-note.js:random_string())
* value is encrypted using secretId AES 256 with library sjcl.js
* secretId is hashed (SHA-1) using private-note.js:sha1()
* secretIdHash and encryptedValue submitted to web api (HTTP POST)
* web api calls lambda that 
** stores encryptedValue in values S3 bucket with key = secretIdHash 
** create FIFO SQS queue with name=secretIdHash. If sqs queue exists throw exception.
** place string "1" on queue 
** display view link using secretId in url

On view:
* secretId is hashed (SHA-1)
* secretIdHash is submitted to web api (HTTP GET)
* web api calls lambda that
** connects to SQS queue with name=secretIdHash
** reads first message from SQS queue
** deletes SQS queue
** return encryptedValue from S3 bucket with key = secretIdHash 
* decrypt encryptedValue in browser client using sjcl.js:decrypt() 
* display decrypted value 



