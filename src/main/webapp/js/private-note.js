function random_string(C) {
	
    if (C === null) {
        C = 16
    }
    var B = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    var D = "";
    for (var A = 0; A < C; A++) {
        pos = Math.floor(Math.random() * B.length);
        D += B.charAt(pos)
    }
    return D
}

function encrypt(key, value) {
	return btoa(sjcl.encrypt(key, value));
}

function decrypt(password, value) {
    return sjcl.decrypt(password, atob(value));
} 
