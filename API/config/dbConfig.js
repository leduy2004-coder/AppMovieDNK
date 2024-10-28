// dbConfig.js
const os = require('os');
const dotenv = require('dotenv');
const sql = require('mssql');
const config = {
    user: process.env.SQL_USER,
    password: process.env.SQL_PASSWORD,
    database: process.env.SQL_DATABASE,
    server: process.env.SQL_SERVER,
    port: parseInt(process.env.SQL_PORT),
    options: {
        encrypt: false,
        enableArithAbort: true,
        connectionTimeout: 30000, // 30 giây
        requestTimeout: 30000 // 30 giây
    }
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
