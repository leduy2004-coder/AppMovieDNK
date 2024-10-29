// dbConfig.js
const os = require('os');
const dotenv = require('dotenv');
const sql = require('mssql');
const config = {
    // user: process.env.SQL_USER,
    // password: process.env.SQL_PASSWORD,
    // database: process.env.SQL_DATABASE,
    // server: process.env.SQL_SERVER,
    // port: parseInt(process.env.SQL_PORT),
    user: "sa",
    password: "Abc1234!@",
    database: "Cinema_version4",
    server: "LAPTOP-F30SDEST\\SQLEXPRESS",
    port: 3000,
    options: {
        encrypt: true, // Nếu bạn đang sử dụng Azure, bật tùy chọn này
        trustServerCertificate: true // Nếu sử dụng self-signed SSL
    },
    pool: {
        max: 5,                  // Giới hạn tối đa kết nối pool
        min: 1,                  // Số kết nối tối thiểu
        idleTimeoutMillis: 60000, // Thời gian chờ cho kết nối không hoạt động
    },
    connectionTimeout: 60000,   // Thời gian chờ kết nối
    requestTimeout: 60000       // Thời gian chờ cho truy vấn
};

async function connectToDatabase() {
    let connected = false;
    // while (!connected) {
        try {
            await sql.connect(config);
            connected = true;
            console.log('Kết nối đến SQL Server thành công!');
        } catch (error) {
            console.error('Không thể kết nối đến SQL Server. Chi tiết lỗi:', error.message);
            // await new Promise(resolve => setTimeout(resolve, 5000));
        }
    // }
}

connectToDatabase();

module.exports = config;
