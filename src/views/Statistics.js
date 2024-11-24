import React, { useState, useEffect } from 'react';
import { Col, Row, Form, FormGroup, Label, Input } from 'reactstrap';
import TicketsChart from '~/components/TicketsChart';
import General from '~/components/General';
import TopUser from '~/components/TopUser';
import { UserNotify } from '~/components/store/NotifyContext';

import config from '~/services';

const Starter = () => {
    const currentYear = new Date().getFullYear(); // Lấy năm hiện tại
    const [selectedYear, setSelectedYear] = useState(currentYear);
    const { setInfoNotify } = UserNotify();
    const [dataTicketChart, setDataTicketChart] = useState([]);
    const [dataTopUser, setDataTopUser] = useState([]);
    const [generalData, setGeneralData] = useState({
        sumMovie: 0,
        sumTurnover: 0,
        sumTicket: 0,
    });

    // Hàm gọi API và cập nhật dữ liệu
    const fetchData = async (year) => {
        try {
            const [ticketData, topUserData, sumMovieData, sumTurnoverData, sumTicketData] = await Promise.all([
                config.getTicketByYear(year),
                config.getTopCustomersByYear(year),
                config.getSumMovieByYear(year),
                config.getSumTurnoverByYear(year),
                config.getSumTicketByYear(year),
            ]);

            if (
                ticketData.errCode ||
                topUserData.errCode ||
                sumMovieData.errCode ||
                sumTurnoverData.errCode ||
                sumTicketData.errCode
            ) {
                setInfoNotify({
                    content: 'Lỗi dữ liệu !!',
                    delay: 1500,
                    isNotify: true,
                    type: 'error',
                });
            } else {
                setDataTicketChart(ticketData);
                setDataTopUser(topUserData);

                // Cập nhật dữ liệu vào generalData
                setGeneralData({
                    sumMovie: sumMovieData.SoLuongPhim,
                    sumTurnover: sumTurnoverData.TongDoanhThu,
                    sumTicket: sumTicketData.TongSoLuongVe,
                });
            }
        } catch (error) {
            setInfoNotify({
                content: 'Lỗi khi lấy dữ liệu từ server !!',
                delay: 1500,
                isNotify: true,
                type: 'error',
            });
        }
    };
    // Gọi hàm fetchData khi component load lần đầu hoặc khi selectedYear thay đổi
    useEffect(() => {
        fetchData(selectedYear);
    }, [selectedYear]);

    const handleYearChange = (e) => {
        setSelectedYear(e.target.value); // Cập nhật năm đã chọn
    };
    return (
        <div>
            {/***Top Cards***/}

            {/***Sales & Feed***/}
            <Row className="mb-4">
                {/* ComboBox for Year */}
                <Col sm="12">
                    <Form>
                        <FormGroup>
                            <Label for="yearSelect">Chọn năm</Label>
                            <Input
                                type="select"
                                name="year"
                                id="yearSelect"
                                value={selectedYear}
                                onChange={handleYearChange}
                            >
                                <option value="2023">2023</option>
                                <option value="2024">2024</option>
                                <option value="2025">2025</option>
                                {/* Add more years as needed */}
                            </Input>
                        </FormGroup>
                    </Form>
                </Col>
            </Row>

            <Row>
                <Col sm="6" lg="6" xl="7" xxl="8">
                    <TicketsChart data={dataTicketChart} />
                </Col>
                <Col sm="6" lg="6" xl="5" xxl="4">
                    <General data={generalData} />
                </Col>
            </Row>
            {/***Table ***/}
            <Row>
                <Col lg="12">
                    <TopUser data={dataTopUser} />
                </Col>
            </Row>
        </div>
    );
};

export default Starter;
