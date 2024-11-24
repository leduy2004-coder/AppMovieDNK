// routes/movies.js
const express = require('express');
const axios = require('axios');
const router = express.Router();
const sql = require('mssql');
const { connectToDatabase } = require('../config/dbConfig');
const axiosRetry = require('axios-retry');


// Route để tổng sô vé trong từng tháng trong năm
router.get('/getTicketByYear', async (req, res) => {
    const { year } = req.query;
    let pool;
    try {
        pool = await connectToDatabase();

        let result = await pool.request()
            .input('nam', sql.NVarChar(50), year) 
            .query('SELECT * FROM fThongKeTungThangTrongNam(@nam)'); // Gọi hàm SQL

        res.json(result.recordset);
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu :', err);
        res.status(500).send('Lỗi khi lấy dữ liệu');
    } 
});

// Route để tổng sô vé trong từng tháng trong năm
router.get('/getTopCustomersByYear', async (req, res) => {
    const { year } = req.query;
    let pool;
    try {
        pool = await connectToDatabase();

        let result = await pool.request()
            .input('Year', sql.Int(50), year) 
            .query('SELECT * FROM dbo.GetTopCustomersByYear(@Year)'); 

        res.json(result.recordset);
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu :', err);
        res.status(500).send('Lỗi khi lấy dữ liệu');
    } 
});

// Route để tổng sô vé trong năm
router.get('/getSumTicketByYear', async (req, res) => {
    const { year } = req.query;
    let pool;
    try {
        pool = await connectToDatabase();

        let result = await pool.request()
            .input('nam', sql.Int(50), year) 
            .query('SELECT dbo.ftongVeTrongNam(@nam) AS TongSoLuongVe'); 

        res.json(result.recordset[0]);
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu :', err);
        res.status(500).send('Lỗi khi lấy dữ liệu');
    } 
});
// Route để tổng sô phim trong năm
router.get('/getSumMovieByYear', async (req, res) => {
    const { year } = req.query;
    let pool;
    try {
        pool = await connectToDatabase();

        let result = await pool.request()
            .input('nam', sql.Int(50), year) 
            .query('SELECT dbo.fSoLuongPhimDaChieuTrongNam(@nam) AS SoLuongPhim'); 

        res.json(result.recordset[0]);
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu :', err);
        res.status(500).send('Lỗi khi lấy dữ liệu');
    } 
});
// Route để tổng doanh thu trong năm
router.get('/getSumTurnoverByYear', async (req, res) => {
    const { year } = req.query;
    let pool;
    try {
        pool = await connectToDatabase();

        let result = await pool.request()
            .input('nam', sql.Int(50), year) 
            .query('SELECT dbo.fTongDoanhThuTheoNam(@nam) AS TongDoanhThu'); 

        res.json(result.recordset[0]);
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu :', err);
        res.status(500).send('Lỗi khi lấy dữ liệu');
    } 
});
module.exports = router;
