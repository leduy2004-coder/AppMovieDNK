// routes/account.js
const express = require('express');
const router = express.Router();
const sql = require('mssql');
const config = require('../dbConfig'); // Import cấu hình



router.post('/login', async (req, res) => {
    const {tenTK, matKhau } = req.body;
    try {
        let pool = await sql.connect(config);
        let result = await pool.request()
            .input('tenTK', sql.VarChar, tenTK)
            .input('matKhau', sql.VarChar, matKhau)
            .query('SELECT * FROM KhachHang WHERE tenTK = @tenTK AND matKhau = @matKhau');

        if (result.recordset.length > 0) {
            // Đăng nhập thành công, trả về dữ liệu của khách hàng
            res.json(result.recordset[0]);
        } else {
            // Đăng nhập thất bại
            res.status(401).json({ message: "Sai tài khoản hoặc mật khẩu" });
        }
    } catch (err) {
        console.error('Lỗi:', err);
        res.status(500).json({ message: 'Lỗi khi xử lý yêu cầu' });
    } finally {
        await sql.close();
    }
});


// Route để lấy lịch sử khách hàng
router.get('/history/:maKH', async (req, res) => {
    const maKH = req.params.maKH; // Lấy mã khách hàng từ URL
    try {
        let pool = await sql.connect(config);
        let result = await pool.request()
        
        .input('maKH', sql.VarChar(20), maKH) // Đặt giá trị cho tham số maKH
        .query('SELECT * FROM fLichSuKH(@maKH)'); // Gọi hàm fLichSuKH

    res.json(result.recordset);
    } catch (err) {
        console.error('Lỗi khi lấy lịch sử:', err);
        res.status(500).send('Lỗi khi lấy dữ liệu lịch sử');
    } finally {
        await sql.close();
    }
});

module.exports = router; // Đảm bảo export router
