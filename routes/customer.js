// routes/book.js
const express = require('express');
const router = express.Router();
const sql = require('mssql');
const { connectToDatabase } = require('../config/dbConfig');


// Route để lấy thông tin khách hàng
router.get('/getInfCustomer/:maKH', async (req, res) => {
    const { maKH } = req.params;

        try {
             let pool = await connectToDatabase();

            const result = await pool.request()
                .input('maKH', sql.VarChar(20), maKH)
                .query('SELECT * FROM KhachHang Where maKH = @maKH');
     
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

router.patch('/update/:idKH', async (req, res) => {
    const maKH = req.params.idKH; 
    const { hoTen, sdt, ngaySinh, email } = req.body;

    try {
        // Kết nối cơ sở dữ liệu
        const pool = await connectToDatabase();

        // Kiểm tra dữ liệu đầu vào
        if (!hoTen || !sdt || !ngaySinh || !email) {
            return res.status(400).json({ message: 'Dữ liệu không đầy đủ, vui lòng kiểm tra lại' });
        }

        // Thực hiện câu lệnh UPDATE trong cơ sở dữ liệu
        await pool.request()
            .input('maKH', sql.NVarChar(50), maKH) // Thêm tham số maKH
            .input('hoTen', sql.NVarChar(50), hoTen)
            .input('sdt', sql.NVarChar(50), sdt)
            .input('ngaySinh', sql.Date, ngaySinh)
            .input('email', sql.NVarChar(50), email)
            .query(`
                UPDATE KhachHang
                SET 
                    hoTen = @hoTen,
                    sdt = @sdt,
                    ngaySinh = @ngaySinh,
                    email = @email
                WHERE maKH = @maKH
            `);

        // Truy vấn lại dữ liệu vừa cập nhật để trả về
        const updatedCustomer = await pool.request()
            .input('maKH', sql.NVarChar(50), maKH)
            .query(`SELECT * FROM KhachHang WHERE maKH = @maKH`);

        // Trả về thông tin khách hàng đã cập nhật
        res.json(updatedCustomer.recordset[0]);
    } catch (err) {
        console.log('Lỗi khi cập nhật dữ liệu:', err);
        res.status(500).json({ message: 'Lỗi khi cập nhật dữ liệu vào cơ sở dữ liệu', error: err.message });
    }
});

module.exports = router; 
