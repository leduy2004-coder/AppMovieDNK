// routes/movies.js
const express = require('express');
const axios = require('axios');
const router = express.Router();
const sql = require('mssql');
const { connectToDatabase } = require('../config/dbConfig');



// Route để update thông tin điểm danh của khach hang 
router.post('/update', async (req, res) => {
    const { maKH, diemDanh } = req.body; 
    let pool;
    try {
        pool = await connectToDatabase();
        let result = await pool.request()
            .input('maKH', sql.NVarChar, maKH)
            .input('diemDanh', sql.Int, diemDanh)
            .input('ngayDiemDanhCuoi', sql.Date, new Date())
            .query('UPDATE KhachHang SET diemDanh = @diemDanh, ngayDiemDanhCuoi = @ngayDiemDanhCuoi WHERE maKH = @maKH');
           
        if (result.rowsAffected[0] > 0) {

            res.json({ message: 'Cập nhật điểm danh thành công!' });
        } else {
            res.status(404).send('Khách hàng không tìm thấy');
        }
    } catch (err) {
        console.error('Lỗi khi cập nhật điểm danh:', err);
        res.status(500).send('Lỗi khi cập nhật điểm danh');
    }
});



module.exports = router;
