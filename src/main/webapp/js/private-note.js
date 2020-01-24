function random_string(C) {
    if (C === null) {
        C = 16
    }
    var B = "abcdefghijklmnopqrstuvwxyz";
    var D = "";
    for (var A = 0; A < C; A++) {
        pos = Math.floor(Math.random() * B.length);
        D += B.charAt(pos)
    }
    return D
}

function encrypt(key, value) {
	var result = sjcl.encrypt(key, value);
	console.println(result);
}