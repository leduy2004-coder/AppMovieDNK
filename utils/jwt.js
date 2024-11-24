const jwt = require('jsonwebtoken');


const secretKey = 'your_secret_key'; 

// Hàm để tạo token
const generateToken = (user) => {
    const payload = {
        id: user.id,
        username: user.username
    };
    const options = {
        expiresIn: '1h' // Thời gian hết hạn của token
    };
    return jwt.sign(payload, secretKey, options);
};

// Ví dụ sử dụng
const user = { id: 1, username: 'exampleUser' }; // Dữ liệu người dùng từ database
const token = generateToken(user);
console.log('Generated JWT:', token);
