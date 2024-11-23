// routes/book.js
const express = require('express');
const router = express.Router();
const sql = require('mssql');
const { connectToDatabase } = require('../config/dbConfig');


// Route để lấy số lượng voucher và điểm của khách hàng
router.get('/getVoucherAndPoint/:maKH', async (req, res) => {
    const { maKH } = req.params;

        try {
             let pool = await connectToDatabase();

            const result = await pool.request()
                .input('maKH', sql.VarChar(20), maKH)
                .query('SELECT soLuongVoucher, diemThuong FROM KhachHang Where maKH = @maKH');
     
            res.json(result.recordset[0]);
        } catch (error) {
            console.error('Lỗi khi truy vấn:', error);
            res.status(500).send('Có lỗi xảy ra khi truy vấn cơ sở dữ liệu.');
        }
});

// Route update số điểm và voucher sau khi đặt
router.post('/updatePoint/:maKH', async (req, res) => {
    const { maKH } = req.params;
    const { diemThuong, soLuongVoucher } = req.body;

    try {
        let pool = await connectToDatabase();

        const result = await pool.request()
            .input('maKH', sql.VarChar(20), maKH)
            .input('diemThuong', sql.Int, diemThuong)
            .input('soLuongVoucher', sql.Int, soLuongVoucher)
            .query('UPDATE KhachHang SET diemThuong = @diemThuong, soLuongVoucher = @soLuongVoucher WHERE maKH = @maKH');

        if (result.rowsAffected[0] > 0) {
            res.status(200).json({ message: 'Cập nhật điểm thành công!' });
        } else {
            res.status(404).json({ message: 'Không tìm thấy khách hàng!' });
        }
    } catch (error) {
        console.error('Lỗi khi cập nhật điểm:', error);
        res.status(500).send('Có lỗi xảy ra khi xử lý yêu cầu.');
    }
});


module.exports = router; 
