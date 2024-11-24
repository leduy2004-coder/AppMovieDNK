// routes/account.js
const express = require('express');
const router = express.Router();
const sql = require('mssql');
const { connectToDatabase } = require('../config/dbConfig');
const bcrypt = require('bcrypt')


router.post('/login', async (req, res) => {
    const { tenTK, matKhau } = req.body;

    try {
        let pool = await connectToDatabase();
        
        // Lấy thông tin người dùng dựa trên tài khoản
        let results = await pool.request()
    .input('tenTK', sql.VarChar, tenTK)
    .query('SELECT * FROM KhachHang WHERE tenTK = @tenTK');

    if (results.recordset.length > 0) {
        const users = results.recordset; // Lấy danh sách tất cả người dùng

        for (let i = 0; i < users.length; i++) {
            const user = users[i];
            
            // So sánh mật khẩu nhập vào với mật khẩu đã băm
            const isMatch = await bcrypt.compare(matKhau, user.matKhau);

            if (isMatch) {
                console.log(user)
                return res.json(user); 
            }
        }

        // Nếu không có tài khoản nào khớp
        res.status(401).json({ message: "Sai tài khoản hoặc mật khẩu" });
    } else {
        // Không có người dùng trong bảng
        res.status(401).json({ message: "Không tìm thấy người dùng" });
    }

    } catch (err) {
        console.error('Lỗi:', err);
        res.status(500).json({ message: 'Lỗi khi xử lý yêu cầu' });
    } 
});


// Route để lấy lịch sử khách hàng
router.get('/history/:maKH', async (req, res) => {
    const maKH = req.params.maKH; // Lấy mã khách hàng từ URL
    try {
        let pool = await connectToDatabase();
        let result = await pool.request()
        
        .input('maKH', sql.VarChar(20), maKH) // Đặt giá trị cho tham số maKH
        .query('SELECT * FROM fLichSuKH(@maKH)'); // Gọi hàm fLichSuKH

    res.json(result.recordset);
    } catch (err) {
        console.error('Lỗi khi lấy lịch sử:', err);
        res.status(500).send('Lỗi khi lấy dữ liệu lịch sử');
    } 
});

module.exports = router;
