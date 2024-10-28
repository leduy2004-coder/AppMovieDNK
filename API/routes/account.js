// routes/account.js
const express = require('express');
const router = express.Router();
const sql = require('mssql');
const config = require('../config/dbConfig'); // Import cấu hình



router.post('/login', async (req, res) => {
    const { tenTK, matKhau } = req.body;

    try {
        let pool = await sql.connect(config);
        
        // Lấy thông tin người dùng dựa trên tài khoản
        let result = await pool.request()
            .input('tenTK', sql.VarChar, tenTK)
            .query('SELECT * FROM KhachHang WHERE tenTK = @tenTK');

        if (result.recordset.length > 0) {
            const user = result.recordset[0]; 

            // So sánh mật khẩu nhập vào với mật khẩu đã băm trong cơ sở dữ liệu
            const isMatch = await bcrypt.compare(matKhau, user.matKhau); 

            if (isMatch) {
                res.json(user);
            } else {
                res.status(401).json({ message: "Sai tài khoản hoặc mật khẩu" });
            }
        } else {
            // Không tìm thấy người dùng
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
