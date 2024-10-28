// routes/movies.js
const express = require('express');
const axios = require('axios');
const router = express.Router();
const sql = require('mssql');
const config = require('../config/dbConfig'); // Import cấu hình
const nodemailer = require('nodemailer');
const bcrypt = require('bcrypt');
const saltRounds = 10;
let verificationCode = {};  // Lưu mã xác nhận theo email

// Cấu hình transporter để gửi email
const transporter = nodemailer.createTransport({
  service: 'gmail', // Hoặc các dịch vụ khác
  auth: {
    user: 'appmoviednk@gmail.com', // Thay bằng email của bạn
    pass: 'vmqpxvjghtsuygfs'   // Thay bằng mật khẩu của bạn
  }
});




router.post('/send-verification', (req, res) => {
  const { email } = req.body;

  // Tạo mã xác nhận ngẫu nhiên
  const code = Math.floor(100000 + Math.random() * 900000);

  // Lưu mã vào đối tượng với khóa là email
  verificationCode[email] = code;

  // Gửi mã xác nhận qua email
  const mailOptions = {
    from: 'your-email@gmail.com',
    to: email,
    subject: 'Mã xác nhận đăng ký',
    text: `Mã xác nhận của bạn là: ${code}`
  };

  transporter.sendMail(mailOptions, (error, info) => {
    if (error) {
      return res.status(500).send({ message: 'Lỗi khi gửi email' });
    }
    res.send({ message: 'Mã xác nhận đã được gửi tới email!' });
  });
});

router.post('/verify-code', (req, res) => {
  const { email, code } = req.body;

  // Kiểm tra mã xác nhận có tồn tại và đúng không
  if (verificationCode[email] && verificationCode[email] === Number(code)) {
    // Nếu đúng, xóa mã đã xác nhận
    delete verificationCode[email];
    return res.send({ message: 'Mã xác nhận hợp lệ!' });
  } else {
    return res.status(400).send({ message: 'Mã xác nhận không hợp lệ hoặc đã hết hạn.' });
  }
});

router.post('/register', async (req, res) => {
  const { email, hoTen, tenTK, matKhau, sdt } = req.body;

  // Kiểm tra mã xác nhận

    try {
      const hashPassword = await bcrypt.hash(matKhau, saltRounds);
      // Kết nối đến cơ sở dữ liệu
      let pool = await sql.connect(config);

      // Thực hiện insert vào cơ sở dữ liệu
      const insertQuery = `INSERT INTO KhachHang (hoTen, email, tinhTrang, tenTK, matKhau, sdt)
                             VALUES (@hoTen, @Email, @tinhTrang, @tenTK, @matKhau, @sdt)`;

      // Khai báo các tham số
      const request = pool.request()
        .input('hoTen', sql.NVarChar(100), hoTen)
        .input('Email', sql.NVarChar(50), email)
        .input('tinhTrang', sql.Int, 1) // Giả sử trạng thái là 1
        .input('tenTK', sql.NVarChar(50), tenTK)
        .input('matKhau', sql.NVarChar(100), hashPassword)
        .input('sdt', sql.NVarChar(15), sdt)

      // Thực hiện truy vấn
      await request.query(insertQuery);

      // Xóa mã xác nhận sau khi đăng ký thành công
      delete verificationCode[email];

      res.send({ message: 'Đăng ký thành công!' });
    } catch (error) {
      console.log(error);
      res.status(500).send({ message: 'Đã xảy ra lỗi trong quá trình đăng ký!' });
    }
  
});



router.get('/get-verification-code/:email', (req, res) => {
  const { email } = req.params;

  // Kiểm tra mã xác nhận đã được tạo hay chưa
  if (verificationCode[email]) {
    res.send({ maDangKi: verificationCode[email] });
  } else {
    res.status(404).send({ message: 'Mã xác nhận không tồn tại hoặc chưa được gửi.' });
  }
});

module.exports = router;